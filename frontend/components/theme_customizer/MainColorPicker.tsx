import { memo, useCallback, useState } from "react";
import { Row, Col, Typography, ColorPicker } from "antd";
import { PresetsItem, SingleValueType } from "antd/es/color-picker/interface";
import { ColorValueType } from "antd/lib/color-picker/interface";
import { AggregationColor } from "antd/es/color-picker/color";

const { Text, Link } = Typography;


export type ColorPickerProps = {
    title?: string;
    tempValue?: any; // This can be a string or any other type based on your temp value structure
    tokenValue?: any; // This can be a string or any other type based on your token structure
    onChangeComplete: (color: string | undefined) => void;
    onReset: () => void;
    presetColors?: PresetsItem[];
    [key: string]: any;
}

const MainColorPicker = memo(
    ({
        title,
        tempValue,
        tokenValue,
        onChangeComplete,
        onReset,
        presetColors,
        ...props
    }: ColorPickerProps) => {
        const [localTempValue, setLocalTempValue] = useState<string | undefined>(undefined);

        const handleChange = useCallback(
            (color: AggregationColor) => {
                setLocalTempValue(color.toHexString());
            },
            [setLocalTempValue]
        )

        const handleChangeComplete = useCallback(
            (color: AggregationColor) => {
                onChangeComplete(color.toHexString());
                setLocalTempValue(undefined);
            },
            [setLocalTempValue, onChangeComplete]
        );

        const handleReset = useCallback(() => {
            setLocalTempValue(undefined);
            onReset();
        }, [setLocalTempValue, onReset]);

        return (
            <Row align="middle" justify="space-between" className="mb-2">
                <Col>
                    <Text strong>
                        {title || "Main Color"}
                        {tempValue && (
                            <Link className="ml-2" onClick={handleReset}>
                                Reset
                            </Link>
                        )}
                    </Text>
                </Col>
                <Col>
                    <ColorPicker
                        style={{ padding: "4px" }}
                        defaultValue={tokenValue}
                        value={localTempValue || tempValue || tokenValue}
                        presets={presetColors}
                        onChange={handleChange}
                        onChangeComplete={handleChangeComplete}
                        showText
                        {...props}
                    />
                </Col>
            </Row>
        );
    }
)

export default MainColorPicker;