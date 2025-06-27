import { Theme, updateTempTheme } from "@/lib/features/themes/themesSlice";
import { Dispatch } from "@reduxjs/toolkit";
import { Card, Collapse, GlobalToken } from "antd";
import { PresetsItem } from "antd/es/color-picker/interface";
import MainColorPicker from "./MainColorPicker";
import { useId } from "react";
import DetailColorPicker from "./DetailColorPicker";


type MappedColor = "Bg" | "BgHover" | "Border" |
    "BorderHover" | "Hover" | "" | "Active" |
    "TextHover" | "Text" | "TextActive";

type ThemeColorGroupProps = {
    title?: string;
    mainColor?: string;
    mapColors?: MappedColor[];
    state: Theme; // Replace with actual state type
    token: GlobalToken; // Replace with actual token type
    presetColors?: PresetsItem[];
    dispatch: Dispatch;
}

const ThemeColorGroup = ({
    title,
    mainColor,
    mapColors,
    state,
    token,
    presetColors = [],
    dispatch
}: ThemeColorGroupProps) => {

    return (
        <Card
            size="default"
            title={title || "Theme Colors"}
            variant="borderless"
            style={{
                boxShadow: token.boxShadow,
            }}
        >
            <MainColorPicker
                title={mainColor || "Main Color"}
                tempValue={state[mainColor as keyof Theme]}
                tokenValue={token[mainColor as keyof GlobalToken]}
                onChangeComplete={(color) => {
                    dispatch(updateTempTheme({
                        [mainColor as keyof Theme]: color,
                    }));
                }}
                onReset={() => {
                    dispatch(updateTempTheme({
                        [mainColor as keyof Theme]: undefined,
                    }));
                }}
                presetColors={presetColors}
            />
            <Collapse
                bordered={false}
                style={{
                    boxShadow: token.boxShadow,
                }}
                items={
                    [
                        {
                            key: useId(),
                            label: "Mapped Colors",
                            children: mapColors?.map((color) => (
                                <DetailColorPicker
                                    key={useId()}
                                    title={mainColor + color}
                                    tempValue={state[mainColor + color as keyof Theme]}
                                    tokenValue={token[mainColor + color as keyof GlobalToken]}
                                    onChangeComplete={(colorValue) => {
                                        dispatch(updateTempTheme({
                                            [mainColor + color as keyof Theme]: colorValue,
                                        }));
                                    }}
                                    onReset={() => {
                                        dispatch(updateTempTheme({
                                            [mainColor + color as keyof Theme]: undefined,
                                        }));
                                    }}
                                    presetColors={presetColors}
                                />
                            ))
                        }
                    ]
                }



            />


        </Card>
    )

}

export default ThemeColorGroup;