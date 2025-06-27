import { Theme, updateTempTheme } from "@/lib/features/themes/themesSlice";
import { Dispatch } from "@reduxjs/toolkit";
import { Card, Collapse, GlobalToken } from "antd";
import { PresetsItem } from "antd/es/color-picker/interface";
import MainColorPicker from "./MainColorPicker";
import { useId } from "react";
import DetailColorPicker from "./DetailColorPicker";

type MappedColor = "colorTextBase" |
    "colorBgBase" |
    "colorText" |
    "colorTextSecondary" |
    "colorTextTertiary" |
    "colorTextQuaternary" |
    "colorBorder" |
    "colorBorderSecondary" |
    "colorFill" |
    "colorFillSecondary" |
    "colorFillTertiary" |
    "colorFillQuaternary" |
    "colorBgContainer" |
    "colorBgElevated" |
    "colorBgLayout" |
    "colorBgSpotlight" |
    "colorBgMask";

type ThemeColorGroupV2Props = {
    title?: string;
    mainColors?: MappedColor[];
    mapColors?: MappedColor[];
    state: Theme; // Replace with actual state type
    token: GlobalToken; // Replace with actual token type
    presetColors?: PresetsItem[];
    dispatch: Dispatch;
}


const ThemeColorGroupV2 = ({
    title,
    mainColors,
    mapColors,
    state,
    token,
    presetColors = [],
    dispatch
}: ThemeColorGroupV2Props) => {

    return (
        <Card
            size="default"
            title={title || "Theme Colors"}
            variant="borderless"
            style={{
                boxShadow: token.boxShadow,
            }}
        >

            {mainColors?.map((mainColor) => (
                <MainColorPicker
                    key={useId()}
                    title={mainColor}
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
            ))
            }

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
                                    title={color}
                                    tempValue={state[color as keyof Theme]}
                                    tokenValue={token[color as keyof GlobalToken]}
                                    onChangeComplete={(colorValue) => {
                                        dispatch(updateTempTheme({
                                            [color as keyof Theme]: colorValue,
                                        }));
                                    }}
                                    onReset={() => {
                                        dispatch(updateTempTheme({
                                            [color as keyof Theme]: undefined,
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

export default ThemeColorGroupV2;