import time
from typing import Tuple, Dict

from PIL import Image
from huggingface_hub import hf_hub_download
from onnxruntime import InferenceSession
import cv2
import pandas as pd
import numpy as np


def make_square(img, target_size):
    print("[make_square] Making image square...")
    old_size = img.shape[:2]
    desired_size = max(old_size)
    desired_size = max(desired_size, target_size)

    delta_w = desired_size - old_size[1]
    delta_h = desired_size - old_size[0]
    top, bottom = delta_h // 2, delta_h - (delta_h // 2)
    left, right = delta_w // 2, delta_w - (delta_w // 2)

    color = [255, 255, 255]
    img_out = cv2.copyMakeBorder(img, top, bottom, left, right, cv2.BORDER_CONSTANT, value=color)
    print(f"[make_square] Done. New size: {img_out.shape}")
    return img_out


def smart_resize(img, size):
    print(f"[smart_resize] Resizing image to {size}x{size}...")
    if img.shape[0] > size:
        img = cv2.resize(img, (size, size), interpolation=cv2.INTER_AREA)
    elif img.shape[0] < size:
        img = cv2.resize(img, (size, size), interpolation=cv2.INTER_CUBIC)
    else:
        print("[smart_resize] No resize needed.")
    print(f"[smart_resize] Done. New shape: {img.shape}")
    return img


class WaifuDiffusionInterrogator:
    def __init__(
        self,
        repo='SmilingWolf/wd-v1-4-vit-tagger',
        model_path='model.onnx',
        tags_path='selected_tags.csv',
        mode: str = "auto",
        local_dir='./models/wd14-vit'
    ) -> None:
        print("[init] Initializing WaifuDiffusionInterrogator...")
        self.__repo = repo
        self.__model_path = model_path
        self.__tags_path = tags_path
        self._provider_mode = mode
        self.__initialized = False
        self._model, self._tags = None, None
        self.__local_dir = local_dir
        print("[init] Init complete.")

    def _init(self) -> None:
        if self.__initialized:
            print("[_init] Already initialized.")
            return
        print("[_init] Downloading model and tag files...")
        model_path = hf_hub_download(self.__repo, filename=self.__model_path, local_dir= self.__local_dir)
        tags_path = hf_hub_download(self.__repo, filename=self.__tags_path, local_dir= self.__local_dir)

        print(f"[_init] Loading ONNX model from: {model_path}")
        self._model = InferenceSession(str(model_path))

        print(f"[_init] Loading tags from: {tags_path}")
        self._tags = pd.read_csv(tags_path)

        self.__initialized = True
        print("[_init] Initialization done.")

    def _calculation(self, image: Image.Image) -> pd.DataFrame:
        print("[_calculation] Start preprocessing and inference...")
        self._init()

        _, height, _, _ = self._model.get_inputs()[0].shape
        print(f"[_calculation] Model input height: {height}")

        image = image.convert('RGBA')
        new_image = Image.new('RGBA', image.size, 'WHITE')
        new_image.paste(image, mask=image)
        image = new_image.convert('RGB')
        image = np.asarray(image)

        print("[_calculation] Converting image to OpenCV format...")
        image = image[:, :, ::-1]

        image = make_square(image, height)
        image = smart_resize(image, height)

        print("[_calculation] Preparing image for inference...")
        image = image.astype(np.float32)
        image = np.expand_dims(image, 0)

        input_name = self._model.get_inputs()[0].name
        label_name = self._model.get_outputs()[0].name
        print("[_calculation] Running inference...")
        confidence = self._model.run([label_name], {input_name: image})[0]

        print("[_calculation] Inference complete. Mapping tags...")
        full_tags = self._tags[['name', 'category']].copy()
        full_tags['confidence'] = confidence[0]

        print("[_calculation] Tag mapping done.")
        return full_tags

    def interrogate(self, image: Image.Image) -> Tuple[Dict[str, float], Dict[str, float]]:
        print("[interrogate] Start tagging...")
        full_tags = self._calculation(image)

        print("[interrogate] Splitting ratings and tags...")
        ratings = dict(full_tags[full_tags['category'] == 9][['name', 'confidence']].values)
        tags = dict(full_tags[full_tags['category'] != 9][['name', 'confidence']].values)

        print("[interrogate] Done tagging.")
        return ratings, tags


if __name__ == "__main__":
    print(">>> Starting inference run...")
    start = time.time()
    interrogator = WaifuDiffusionInterrogator()
    ratings, tags = interrogator.interrogate(Image.open("./resource/anh-mo-ta.png"))
    print(f"ratings: {ratings}\ntags : {tags}")
    end = time.time()
    print(f">>> Finished. Start: {start:.2f} | End: {end:.2f} | Duration: {end - start:.2f} sec")
