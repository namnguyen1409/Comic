import tempfile
import time
from io import BytesIO

from PIL import Image
from kafka import KafkaConsumer, KafkaProducer
import json, requests
from imageTag import WaifuDiffusionInterrogator


consumer = KafkaConsumer(
    'media.to.ai',
    bootstrap_servers=['localhost:9092'],
    value_deserializer=lambda x: json.loads(x.decode('utf-8')),
    group_id='image-detect-service-group',
)

# Kafka Producer setup
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda x: json.dumps(x).encode('utf-8'),
)

BASE_URL = 'http://localhost:8765'

interrogator = WaifuDiffusionInterrogator()

for msg in consumer:
    if not msg.value.get('fileUrl'):
        continue

    start = time.time()
    print(">>> Starting execute...")
    print(msg.value)

    image_id = msg.value.get('id')
    image_url = str(msg.value.get('fileUrl'))

    r = requests.get(BASE_URL + image_url, allow_redirects=True)

    if r.status_code != 200:
        print(f"Không tải được ảnh: {image_url}")
        continue

    try:
        image = Image.open(BytesIO(r.content))
        ratings, tags = interrogator.interrogate(image)

        sorted_tags = sorted(tags.items(), key=lambda x: x[1], reverse=True)
        top_15_tags = sorted_tags[:15]

        filtered_tags = [(tag, conf) for tag, conf in tags.items() if conf >= 0.5]
        tag_names = [tag for tag, _ in filtered_tags]

        tag_with_conf = [{"tag": tag, "confidence": round(conf, 4)} for tag, conf in top_15_tags]
        nsfw_rating = max(ratings.items(), key=lambda x: x[1])[0]
        nsfw_confidence = ratings[nsfw_rating]

        payload = {
            "id" : image_id,
            "tags" : tag_names,
            "tagsWithConfidence" : tag_with_conf,
            "isSensitive": nsfw_rating in ("sensitive", "explicit", "questionable"),
            "nsfwRating": nsfw_rating,
            "nsfwConfidence": round(nsfw_confidence, 4)
        }
        producer.send('ai.to.media', payload)
        producer.flush()  # Gửi ngay không chờ batch
        end = time.time()
        print(f">>> Finished. Start: {start:.2f} | End: {end:.2f} | Duration: {end - start:.2f} sec")
    except Exception as _:
        print(f"Lỗi xử lý ảnh {image_url}")