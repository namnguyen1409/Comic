// lib/features/themes/themesSlice.ts

import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface Theme {
  name: string;
  sizeMode?: "compact" | "default";
  darkMode?: boolean;

  colorPrimary?: string;
  colorPrimaryBg?: string;
  colorPrimaryBgHover?: string;
  colorPrimaryBorder?: string;
  colorPrimaryBorderHover?: string;
  colorPrimaryHover?: string;
  colorPrimaryActive?: string;
  colorPrimaryTextHover?: string;
  colorPrimaryText?: string;
  colorPrimaryTextActive?: string;

  colorSuccess?: string;
  colorSuccessBg?: string;
  colorSuccessBgHover?: string;
  colorSuccessBorder?: string;
  colorSuccessBorderHover?: string;
  colorSuccessHover?: string;
  colorSuccessActive?: string;
  colorSuccessTextHover?: string;
  colorSuccessText?: string;
  colorSuccessTextActive?: string;

  colorWarning?: string;
  colorWarningBg?: string;
  colorWarningBgHover?: string;
  colorWarningBorder?: string;
  colorWarningBorderHover?: string;
  colorWarningHover?: string;
  colorWarningActive?: string;
  colorWarningTextHover?: string;
  colorWarningText?: string;
  colorWarningTextActive?: string;

  colorError?: string;
  colorErrorBg?: string;
  colorErrorBgHover?: string;
  colorErrorBorder?: string;
  colorErrorBorderHover?: string;
  colorErrorHover?: string;
  colorErrorActive?: string;
  colorErrorTextHover?: string;
  colorErrorText?: string;
  colorErrorTextActive?: string;

  colorInfo?: string;
  colorInfoBg?: string;
  colorInfoBgHover?: string;
  colorInfoBorder?: string;
  colorInfoBorderHover?: string;
  colorInfoHover?: string;
  colorInfoActive?: string;
  colorInfoTextHover?: string;
  colorInfoText?: string;
  colorInfoTextActive?: string;

  colorLink?: string;
  colorLinkHover?: string;
  colorLinkActive?: string;

  colorTextBase?: string;
  colorBgBase?: string;

  colorText?: string;
  colorTextSecondary?: string;
  colorTextTertiary?: string;
  colorTextQuaternary?: string;

  colorBorder?: string;
  colorBorderSecondary?: string;

  colorFill?: string;
  colorFillSecondary?: string;
  colorFillTertiary?: string;
  colorFillQuaternary?: string;

  colorBgContainer?: string;
  colorBgElevated?: string;
  colorBgLayout?: string;
  colorBgSpotlight?: string;
  colorBgMask?: string;

  fontSize?: number;
  fontSizeSM?: number;
  fontSizeLG?: number;
  fontSizeXL?: number;
  fontSizeHeading1?: number;
  fontSizeHeading2?: number;
  fontSizeHeading3?: number;
  fontSizeHeading4?: number;
  fontSizeHeading5?: number;
  lineHeight?: number;
  lineHeightSM?: number;
  lineHeightLG?: number;
  lineHeightHeading1?: number;
  lineHeightHeading2?: number;
  lineHeightHeading3?: number;
  lineHeightHeading4?: number;
  lineHeightHeading5?: number;

  sizeStep?: number;
  sizeUnit?: number;

  marginXXS?: number;
  marginXS?: number;
  marginSM?: number;
  margin?: number;
  marginMD?: number;
  marginLG?: number;
  marginXL?: number;
  marginXXL?: number;

  paddingXXS?: number;
  paddingXS?: number;
  paddingSM?: number;
  padding?: number;
  paddingMD?: number;
  paddingLG?: number;
  paddingXL?: number;

  borderRadius?: number;
  borderRadiusXS?: number;
  borderRadiusSM?: number;
  borderRadiusLG?: number;

  boxShadow?: string;
  boxShadowSecondary?: string;

  wireframe?: boolean;

  fontFamily?: string;
}

interface ThemeState {
  currentThemeName: string; // tên theme hiện tại
  currentTheme: Theme; // theme hiện tại
  themes: Record<string, Theme>; // danh sách các theme mặc định
  customThemes: Record<string, Theme>; // danh sách các theme tuỳ chỉnh
  tempTheme?: Theme; // theme tạm thời (khi người dùng đang chỉnh sửa một theme mới)
  advancedMode?: boolean; // chế độ nâng cao
  darkMode?: boolean; // chế độ tối
}

const defaultThemeState: ThemeState = {
  currentThemeName: "Light",
  currentTheme: {
    name: "Light",
  },
  themes: {
    Light: {
      name: "Light",
      darkMode: false,
    },
    Dark: {
      name: "Dark",
      darkMode: true,
    },
    "Senior Friendly": {
      name: "Senior Friendly",
      darkMode: false,
      colorPrimary: "#1976D2",
      colorSuccess: "#388E3C",
      colorWarning: "#F57C00",
      colorError: "#D32F2F",
      colorInfo: "#0288D1",
      colorBgBase: "#FFFFFF",
      colorTextBase: "#000000",
      fontFamily: "Arial",
      fontSize: 18,
    },

    "Ocean Blue": {
      name: "Ocean Blue",
      darkMode: false,
      colorPrimary: "#1E88E5",
      colorSuccess: "#43A047",
      colorWarning: "#FDD835",
      colorError: "#E53935",
      colorInfo: "#29B6F6",
      colorBgBase: "#E3F2FD",
      colorTextBase: "#0D47A1",
      fontFamily: "Segoe UI",
    },
    "Sunset Orange": {
      name: "Sunset Orange",
      darkMode: false,
      colorPrimary: "#FF7043",
      colorSuccess: "#66BB6A",
      colorWarning: "#FFA726",
      colorError: "#D84315",
      colorInfo: "#FFAB91",
      colorBgBase: "#FFF3E0",
      colorTextBase: "#BF360C",
      fontFamily: "Roboto",
    },
    "Forest Green": {
      name: "Forest Green",
      darkMode: false,
      colorPrimary: "#2E7D32",
      colorSuccess: "#81C784",
      colorWarning: "#FFEB3B",
      colorError: "#D32F2F",
      colorInfo: "#AED581",
      colorBgBase: "#E8F5E9",
      colorTextBase: "#1B5E20",
      fontFamily: "Open Sans",
    },
    "Purple Haze": {
      name: "Purple Haze",
      darkMode: false,
      colorPrimary: "#8E24AA",
      colorSuccess: "#7CB342",
      colorWarning: "#FFD600",
      colorError: "#C62828",
      colorInfo: "#BA68C8",
      colorBgBase: "#F3E5F5",
      colorTextBase: "#4A148C",
      fontFamily: "Lato",
    },
    "Classic Dark": {
      name: "Classic Dark",
      darkMode: true,
      colorPrimary: "#BB86FC",
      colorSuccess: "#03DAC5",
      colorWarning: "#FBC02D",
      colorError: "#CF6679",
      colorInfo: "#03A9F4",
      colorBgBase: "#121212",
      colorTextBase: "#E0E0E0",
      fontFamily: "Arial",
      sizeMode: "compact",
    },
    "Minty Fresh": {
      name: "Minty Fresh",
      darkMode: false,
      colorPrimary: "#00BFA5",
      colorSuccess: "#A5D6A7",
      colorWarning: "#FFF176",
      colorError: "#EF5350",
      colorInfo: "#80DEEA",
      colorBgBase: "#E0F2F1",
      colorTextBase: "#004D40",
      fontFamily: "Poppins",
    },
    "Golden Sky": {
      name: "Golden Sky",
      darkMode: false,
      colorPrimary: "#FFD700",
      colorSuccess: "#ADFF2F",
      colorWarning: "#FFB300",
      colorError: "#FF6F61",
      colorInfo: "#FFF59D",
      colorBgBase: "#FFFDE7",
      colorTextBase: "#5D4037",
      fontFamily: "Georgia",
    },
    "Neutral Gray": {
      name: "Neutral Gray",
      darkMode: false,
      colorPrimary: "#9E9E9E",
      colorSuccess: "#8BC34A",
      colorWarning: "#FFC107",
      colorError: "#E57373",
      colorInfo: "#90A4AE",
      colorBgBase: "#FAFAFA",
      colorTextBase: "#212121",
      fontFamily: "Helvetica",
    },
    "Royal Blue": {
      name: "Royal Blue",
      darkMode: false,
      colorPrimary: "#3F51B5",
      colorSuccess: "#4CAF50",
      colorWarning: "#FFC107",
      colorError: "#F44336",
      colorInfo: "#2196F3",
      colorBgBase: "#E8EAF6",
      colorTextBase: "#1A237E",
      fontFamily: "Ubuntu",
    },
    "Rose Gold": {
      name: "Rose Gold",
      darkMode: false,
      colorPrimary: "#B76E79",
      colorSuccess: "#C5E1A5",
      colorWarning: "#FFE082",
      colorError: "#EF9A9A",
      colorInfo: "#F8BBD0",
      colorBgBase: "#FFF8E1",
      colorTextBase: "#4E342E",
      fontFamily: "Montserrat",
    },
    "Cyberpunk Neon": {
      name: "Cyberpunk Neon",
      darkMode: true,
      colorPrimary: "#FF00FF",
      colorSuccess: "#00FF9F",
      colorWarning: "#FFD700",
      colorError: "#FF0033",
      colorInfo: "#00FFFF",
      colorBgBase: "#0F0F0F",
      colorTextBase: "#E0E0E0",
      fontFamily: "Orbitron",
    },
    "Deep Sea": {
      name: "Deep Sea",
      darkMode: true,
      colorPrimary: "#00796B",
      colorSuccess: "#4DB6AC",
      colorWarning: "#FFB74D",
      colorError: "#E64A19",
      colorInfo: "#4DD0E1",
      colorBgBase: "#00251A",
      colorTextBase: "#B2DFDB",
      fontFamily: "Source Sans Pro",
    },
    "Lavender Dream": {
      name: "Lavender Dream",
      darkMode: false,
      colorPrimary: "#9575CD",
      colorSuccess: "#AED581",
      colorWarning: "#FFD54F",
      colorError: "#F06292",
      colorInfo: "#CE93D8",
      colorBgBase: "#F3E5F5",
      colorTextBase: "#4A148C",
      fontFamily: "Quicksand",
    },
    "Cobalt Ice": {
      name: "Cobalt Ice",
      darkMode: false,
      colorPrimary: "#2962FF",
      colorSuccess: "#00E676",
      colorWarning: "#FFEA00",
      colorError: "#D50000",
      colorInfo: "#40C4FF",
      colorBgBase: "#E3F2FD",
      colorTextBase: "#0D47A1",
      fontFamily: "IBM Plex Sans",
    },
    "Mocha Latte": {
      name: "Mocha Latte",
      darkMode: false,
      colorPrimary: "#6D4C41",
      colorSuccess: "#A1887F",
      colorWarning: "#FFCC80",
      colorError: "#D32F2F",
      colorInfo: "#BCAAA4",
      colorBgBase: "#EFEBE9",
      colorTextBase: "#3E2723",
      fontFamily: "Merriweather",
    },
    "Blush Pink": {
      name: "Blush Pink",
      darkMode: false,
      colorPrimary: "#F48FB1",
      colorSuccess: "#81C784",
      colorWarning: "#FFF176",
      colorError: "#E57373",
      colorInfo: "#F8BBD0",
      colorBgBase: "#FFF0F5",
      colorTextBase: "#880E4F",
      fontFamily: "Nunito",
    },
    "Nordic Frost": {
      name: "Nordic Frost",
      darkMode: true,
      colorPrimary: "#81D4FA",
      colorSuccess: "#AED581",
      colorWarning: "#FFE082",
      colorError: "#EF9A9A",
      colorInfo: "#B3E5FC",
      colorBgBase: "#263238",
      colorTextBase: "#ECEFF1",
      fontFamily: "Inter",
    },
    "Vintage Paper": {
      name: "Vintage Paper",
      darkMode: false,
      colorPrimary: "#A1887F",
      colorSuccess: "#C5E1A5",
      colorWarning: "#FFCC80",
      colorError: "#EF9A9A",
      colorInfo: "#B0BEC5",
      colorBgBase: "#FFF8E1",
      colorTextBase: "#5D4037",
      fontFamily: "Playfair Display",
    },
    "Sakura Bloom": {
      name: "Sakura Bloom",
      darkMode: false,
      colorPrimary: "#EC407A",
      colorSuccess: "#66BB6A",
      colorWarning: "#FFEE58",
      colorError: "#D32F2F",
      colorInfo: "#FFCDD2",
      colorBgBase: "#FFF1F3",
      colorTextBase: "#880E4F",
      fontFamily: "Dancing Script",
    },
    "Slate Shadow": {
      name: "Slate Shadow",
      darkMode: true,
      colorPrimary: "#546E7A",
      colorSuccess: "#8BC34A",
      colorWarning: "#FFCA28",
      colorError: "#E53935",
      colorInfo: "#29B6F6",
      colorBgBase: "#263238",
      colorTextBase: "#ECEFF1",
      fontFamily: "SF Pro Text",
    },

    "Cyber Neon": {
      name: "Cyber Neon",
      darkMode: true,
      colorPrimary: "#00FFFF",
      colorSuccess: "#76FF03",
      colorWarning: "#FFD600",
      colorError: "#FF1744",
      colorInfo: "#00E5FF",
      colorBgBase: "#0D0D0D",
      colorTextBase: "#E0F7FA",
      fontFamily: "Orbitron",
    },

    "Coffee Brown": {
      name: "Coffee Brown",
      darkMode: false,
      colorPrimary: "#6D4C41",
      colorSuccess: "#A1887F",
      colorWarning: "#FFB74D",
      colorError: "#D84315",
      colorInfo: "#8D6E63",
      colorBgBase: "#EFEBE9",
      colorTextBase: "#3E2723",
      fontFamily: "Merriweather",
    },

    "Aqua Blue": {
      name: "Aqua Blue",
      darkMode: false,
      colorPrimary: "#00BCD4",
      colorSuccess: "#4DB6AC",
      colorWarning: "#FFCA28",
      colorError: "#E53935",
      colorInfo: "#4DD0E1",
      colorBgBase: "#E0F7FA",
      colorTextBase: "#006064",
      fontFamily: "Nunito",
    },

    "Charcoal Gray": {
      name: "Charcoal Gray",
      darkMode: true,
      colorPrimary: "#546E7A",
      colorSuccess: "#78909C",
      colorWarning: "#FFA000",
      colorError: "#D32F2F",
      colorInfo: "#607D8B",
      colorBgBase: "#263238",
      colorTextBase: "#ECEFF1",
      fontFamily: "Source Sans Pro",
    },

    "Ruby Red": {
      name: "Ruby Red",
      darkMode: false,
      colorPrimary: "#C2185B",
      colorSuccess: "#81C784",
      colorWarning: "#FBC02D",
      colorError: "#D32F2F",
      colorInfo: "#F06292",
      colorBgBase: "#FCE4EC",
      colorTextBase: "#880E4F",
      fontFamily: "Raleway",
    },

    "Midnight Blue": {
      name: "Midnight Blue",
      darkMode: true,
      colorPrimary: "#1A237E",
      colorSuccess: "#00C853",
      colorWarning: "#FFAB00",
      colorError: "#D50000",
      colorInfo: "#536DFE",
      colorBgBase: "#121212",
      colorTextBase: "#E3F2FD",
      fontFamily: "Cabin",
    },

    "Lime Twist": {
      name: "Lime Twist",
      darkMode: false,
      colorPrimary: "#C0CA33",
      colorSuccess: "#9CCC65",
      colorWarning: "#FFEB3B",
      colorError: "#F44336",
      colorInfo: "#CDDC39",
      colorBgBase: "#F9FBE7",
      colorTextBase: "#827717",
      fontFamily: "Kanit",
    },

    "Sapphire Night": {
      name: "Sapphire Night",
      darkMode: true,
      colorPrimary: "#3F51B5",
      colorSuccess: "#00E676",
      colorWarning: "#FFC400",
      colorError: "#FF1744",
      colorInfo: "#448AFF",
      colorBgBase: "#1C1C3C",
      colorTextBase: "#E8EAF6",
      fontFamily: "Inter",
    },

    "Peach Cream": {
      name: "Peach Cream",
      darkMode: false,
      colorPrimary: "#FFCCBC",
      colorSuccess: "#C5E1A5",
      colorWarning: "#FFF59D",
      colorError: "#FF8A65",
      colorInfo: "#FFECB3",
      colorBgBase: "#FFF3E0",
      colorTextBase: "#5D4037",
      fontFamily: "Comfortaa",
    },
  },
  customThemes: {},
};

const themeState = defaultThemeState;

const themesSlice = createSlice({
  name: "themes",
  initialState: themeState,
  reducers: {
    // 0. Load initial themes from localStorage or use default themes
    setActiveTheme(state, action: PayloadAction<ThemeState>) {
      const newState = action.payload;
      state.currentThemeName = newState.currentThemeName;
      state.currentTheme = newState.currentTheme;
      state.themes = {
        ...defaultThemeState.themes,
        ...newState.themes, // Merge default themes with loaded themes
      };
      state.customThemes = newState.customThemes;
      state.tempTheme = newState.tempTheme;
    },
    // 1. Start editing a theme (clone the current theme into tempTheme)
    startEditingTheme: (state, action: PayloadAction<string>) => {
      const themeName = action.payload;
      const targetTheme =
        state.customThemes[themeName] || state.themes[themeName];
      if (targetTheme) {
        state.tempTheme = { ...targetTheme };
      } else {
        console.warn(`Theme "${themeName}" not found.`);
        throw new Error(`Theme "${themeName}" not found.`);
      }
    },
    // 2. Update tempTheme when user edit
    updateTempTheme: (state, action: PayloadAction<Partial<Theme>>) => {
      if (state.tempTheme) {
        state.tempTheme = {
          ...state.tempTheme,
          ...action.payload,
        };
      }
    },
    // 3. Save the edited theme
    saveTempThemeAs: (state, action: PayloadAction<string>) => {
      if (state.tempTheme) {
        const newName = action.payload;
        const themeToSave = {
          ...state.tempTheme,
          name: newName,
        };
        state.customThemes[newName] = themeToSave;
        state.currentTheme = themeToSave;
        state.currentThemeName = newName;
        state.tempTheme = undefined;
      }
    },
    discardTempTheme: (state) => {
      state.tempTheme = undefined;
    },
    // 5. Chuyển sang theme khác
    setCurrentTheme: (state, action: PayloadAction<string>) => {
      const themeName = action.payload;
      const foundTheme =
        state.customThemes[themeName] || state.themes[themeName];
      if (foundTheme) {
        state.currentThemeName = themeName;
        state.currentTheme = foundTheme;
        state.tempTheme = undefined;
      }
    },
    saveToLocalStorage: (state) => {
      localStorage.setItem(
        "themeState",
        JSON.stringify({
          currentThemeName: state.currentThemeName,
          currentTheme: state.currentTheme,
          themes: state.themes,
          customThemes: state.customThemes,
          tempTheme: state.tempTheme,
          advancedMode: state.advancedMode,
          darkMode: state.darkMode,
        })
      );
    },
    switchAdvancedMode: (state, action: PayloadAction<boolean>) => {
      state.advancedMode = action.payload;
    },
    switchDarkMode: (state, action: PayloadAction<boolean>) => {
      state.darkMode = action.payload;
    },
  },
});

export const {
  startEditingTheme,
  updateTempTheme,
  saveTempThemeAs,
  discardTempTheme,
  setCurrentTheme,
  setActiveTheme,
  saveToLocalStorage,
  switchAdvancedMode,
  switchDarkMode,
} = themesSlice.actions;

export default themesSlice.reducer;
