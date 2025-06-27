import { AntdIconProps } from "@ant-design/icons/lib/components/AntdIcon";
import { Segmented } from "antd";
import { SizeType } from "antd/es/config-provider/SizeContext";
import { SegmentedOptions } from "antd/es/segmented";
import { memo, useCallback, useEffect, useState } from "react";


type CustomerSegmentedProps = {
    size?: SizeType;
    options: SegmentedOptions<any>;
    onChange: (value: string) => void;
    value?: string;
    [key: string]: any; // Allow additional props
}

const CustomerSegmented = memo(
  ({
    size,
    options,
    onChange,
    value,
    ...props
  }: CustomerSegmentedProps) => {
    const [localTempValue, setLocalTempValue] = useState<string>(value || "");

    useEffect(() => {
      setLocalTempValue(value || "");
    }, [value]);

    const handleChange = useCallback(
      (newValue: string) => {
        setLocalTempValue(newValue);
        onChange(newValue);
      },
      [onChange]
    );

    return (
      <Segmented 
        size={size}
        options={options}
        value={localTempValue}
        onChange={handleChange}
        {...props}
      />
    );
  }
);

export default CustomerSegmented;