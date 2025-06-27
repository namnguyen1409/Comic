'use client';
import { SettingOutlined } from "@ant-design/icons";
import { Button, theme } from "antd";
import { Header } from "antd/es/layout/layout";
import { useState } from "react";
import ThemeCustomizer from "../theme_customizer/ThemeCustomizer";

type CustomHeaderProps = {
    children?: React.ReactNode;
};

const CustomHeader = ({children}: CustomHeaderProps) => {
    const [themeCustomizerOpen, setThemeCustomizerOpen] = useState(false);

    const antdToken = theme.useToken().token;

    const toggleThemeCustomizer = () => {
        setThemeCustomizerOpen(!themeCustomizerOpen);
    };

    return (
        <Header
        style={{
            backgroundColor: antdToken.colorBgContainer,
            boxShadow: antdToken.boxShadow,
        }}
        >
            <Button
                type="primary"
                icon={<SettingOutlined />}
                onClick={toggleThemeCustomizer}
            >
                Customize Theme
            </Button>
            <ThemeCustomizer open={themeCustomizerOpen} onClose={toggleThemeCustomizer} />
            {children}
        </Header>
    )

}

export default CustomHeader;