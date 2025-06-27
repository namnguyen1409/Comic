import { memo, useCallback, useState } from "react";
import { ColorPickerProps } from "./MainColorPicker";
import { AggregationColor } from "antd/es/color-picker/color";
import { Col, ColorPicker, Row, Typography } from "antd";

const { Text, Link } = Typography;

const DetailColorPicker = memo(
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
            <Row align="middle" style={{ gap: 16, flexWrap: "nowrap" }} className="mb-2">
                <Col flex="1">
                    <Text>
                        {title || "Detail Color"}
                        {tempValue && (
                            <Link className="ml-2" onClick={handleReset}>
                                Reset
                            </Link>
                        )}
                    </Text>
                </Col>
                <Col>
                    <Text code>{localTempValue || tempValue || tokenValue}</Text>
                </Col>
                <Col>
                    <ColorPicker
                        style={{ padding: "4px" }}
                        defaultValue={tokenValue}
                        value={localTempValue || tempValue || tokenValue}
                        presets={presetColors}
                        onChange={handleChange}
                        onChangeComplete={handleChangeComplete}
                        {...props}
                    />
                </Col>
            </Row>
        )
    })

export default DetailColorPicker;