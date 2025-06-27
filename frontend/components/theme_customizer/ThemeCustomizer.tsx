"use client";
import { isAdvancedMode, selectActiveTheme, selectCustomThemes, selectDefaultTheme, selectTempTheme } from "@/lib/features/themes/themeSelectors";
import { discardTempTheme, saveToLocalStorage, setActiveTheme, setCurrentTheme, startEditingTheme, switchAdvancedMode, switchDarkMode, updateTempTheme } from "@/lib/features/themes/themesSlice";
import { useAppDispatch } from "@/lib/hooks";
import { Card, Col, Drawer, Input, Row, Select, Space, theme, Typography } from "antd";
import { useEffect } from "react";
import { useSelector } from "react-redux";
import CustomerSegmented from "./CustomSegmented";
import { PresetsItem } from "antd/es/color-picker/interface";
import ThemeColorGroup from "./ThemeColorGroup";
import ThemeColorGroupV2 from "./ThemeColorGroupV2";

type ThemeCustomizerProps = {
  open: boolean;
  onClose: () => void;
};

const { Title } = Typography;

const ThemeCustomizer = ({ open, onClose }: ThemeCustomizerProps) => {
  const dispatch = useAppDispatch();
  const antdToken = theme.useToken().token;

  useEffect(() => {
    const savedThemeState = localStorage.getItem("themeState");
    if (savedThemeState) {
      dispatch(setActiveTheme(JSON.parse(savedThemeState)));
    }
  }, [])


  const activeTheme = useSelector(selectActiveTheme);
  const advancedMode = useSelector(isAdvancedMode);

  const defaultThemes = useSelector(selectDefaultTheme);
  const customThemes = useSelector(selectCustomThemes);
  const tempTheme = useSelector(selectTempTheme)

  useEffect(() => {
    dispatch(saveToLocalStorage());
  }, [activeTheme, advancedMode, tempTheme]);


  const presetColors: PresetsItem[] = [
    {
      label: "Default Preset",
      colors: [
        "#1677ff",
        "#722ed1",
        "#13c2c2",
        "#52c41a",
        "#eb2f96",
        "#f5222d",
        "#fa8c16",
        "#fadb14",
        "#fa541c",
        "#2f54eb",
        "#faad14",
        "#a0d911",
        "#000000",
      ],
    }

  ]


  return (
    <Drawer
      title={
        <Row align="middle" justify="space-between">
          <Col>
            <Title level={5} style={{ margin: 0 }}>
              ThemeCustomizer
            </Title>
          </Col>
          <Col>
            <CustomerSegmented
              size="middle"
              value={advancedMode ? 'true' : 'false'}
              onChange={
                (val) => {
                  const value: boolean = (val === 'true');
                  dispatch(switchAdvancedMode(value));
                  if (value) dispatch(startEditingTheme(activeTheme.name));
                  else dispatch(discardTempTheme());
                }
              }
              options={[
                {
                  label: "Basic",
                  value: "false",
                },
                {
                  label: "Advance",
                  value: 'true',
                }
              ]}
            />
          </Col>
        </Row>
      }
      placement="right"
      onClose={onClose}
      open={open}
      width={500}

    >
      <Space direction="vertical" size="large" className="w-full">
        {
          /**
           * Warning: This is a basic theme customizer.
           */
        }
        {advancedMode && (
          <Card
            size="small"
            variant="borderless"
            style={{
              boxShadow: antdToken.boxShadow
            }}
          >
            <Title level={4} type="warning">
              With advancedMode, you can customize the theme with more options.
            </Title>

          </Card>
        )
        }

        {!advancedMode && (
          <Row align="middle" justify="space-between">
            <Col>
              <Title level={5} style={{ margin: 0 }}>
                Select Theme
              </Title>
            </Col>
            <Col>
              <Select
                style={{ width: 200 }}
                value={activeTheme.name}
                onChange={(value) => {
                  const themeToSet = customThemes[value] || defaultThemes[value];
                  if (themeToSet) {
                    dispatch(setCurrentTheme(themeToSet.name));
                  }
                }}
                options={[
                  ...Object.entries(customThemes).map(([name]) => ({
                    label: name,
                    value: name,
                  })),
                  ...Object.entries(defaultThemes).map(([name]) => ({
                    label: name,
                    value: name,
                  })),
                ]}
              />
            </Col>
          </Row>
        )}

        {
          advancedMode && tempTheme != null &&
          (
            <>
              <Row align="middle" justify="space-between">
                <Col>
                  <Title level={5} style={{ margin: 0 }}>
                    Theme name
                  </Title>
                </Col>
                <Col>
                  <Input
                    value={tempTheme.name}
                    onChange={(e) => {
                      dispatch(updateTempTheme({
                        name: e.target.value,
                      }));
                    }}
                    placeholder="Enter theme name"
                    style={{ width: 200 }}
                  />
                </Col>
              </Row>

              <ThemeColorGroup
                title="Primary Color"
                mainColor="colorPrimary"
                mapColors={['Bg', 'BgHover', 'Border', 'BorderHover', 'Hover', '', 'Active', 'TextHover', 'Text', 'TextActive']}
                state={tempTheme}
                token={antdToken}
                presetColors={presetColors}
                dispatch={dispatch}
              />

              <ThemeColorGroup
                title="Success Color"
                mainColor="colorSuccess"
                mapColors={['Bg', 'BgHover', 'Border', 'BorderHover', 'Hover', '', 'Active', 'TextHover', 'Text', 'TextActive']}
                state={tempTheme}
                token={antdToken}
                presetColors={presetColors}
                dispatch={dispatch}
              />

              <ThemeColorGroup
                title="Warning Color"
                mainColor="colorWarning"
                mapColors={['Bg', 'BgHover', 'Border', 'BorderHover', 'Hover', '', 'Active', 'TextHover', 'Text', 'TextActive']}
                state={tempTheme}
                token={antdToken}
                presetColors={presetColors}
                dispatch={dispatch}
              />

              <ThemeColorGroup
                title="Error Color"
                mainColor="colorError"
                mapColors={['Bg', 'BgHover', 'Border', 'BorderHover', 'Hover', '', 'Active', 'TextHover', 'Text', 'TextActive']}
                state={tempTheme}
                token={antdToken}
                presetColors={presetColors}
                dispatch={dispatch}
              />

              <ThemeColorGroup
                title="Info Color"
                mainColor="colorInfo"
                mapColors={['Bg', 'BgHover', 'Border', 'BorderHover', 'Hover', '', 'Active', 'TextHover', 'Text', 'TextActive']}
                state={tempTheme}
                token={antdToken}
                presetColors={presetColors}
                dispatch={dispatch}
              />

              <ThemeColorGroupV2
                title="Neutral Color"
                mainColors={['colorTextBase', 'colorBgBase']}
                mapColors={[
                  'colorText',
                  'colorTextSecondary',
                  'colorTextTertiary',
                  'colorTextQuaternary',
                  'colorBorder',
                  'colorBorderSecondary',
                  'colorFill',
                  'colorFillSecondary',
                  'colorFillTertiary',
                  'colorFillQuaternary',
                  'colorBgContainer',
                  'colorBgElevated',
                  'colorBgLayout',
                  'colorBgSpotlight',
                  'colorBgMask'
                ]}
                state={tempTheme}
                token={antdToken}
                presetColors={presetColors}
                dispatch={dispatch}
              />
            </>
          )
        }







      </Space>
    </Drawer>
  )




}

export default ThemeCustomizer;