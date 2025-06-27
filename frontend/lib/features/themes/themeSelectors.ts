// lib/features/themes/themeSelectors.ts
'use client';
import { RootState } from "@/lib/store";
import { Theme } from "./themesSlice";



export const selectActiveTheme = (state: RootState): Theme => 
    state.themes.tempTheme ?? state.themes.currentTheme;

export const selectTempTheme = (state: RootState): Theme | undefined =>
    state.themes.tempTheme ?? undefined;

export const isAdvancedMode = (state: RootState): boolean =>
    state.themes.advancedMode ?? false;

export const isDarkMode = (state: RootState): boolean =>
    state.themes.darkMode ?? false;

export const selectDefaultTheme = (state: RootState): Record<string, Theme> => 
    state.themes.themes

export const selectCustomThemes = (state: RootState): Record<string, Theme> =>
    state.themes.customThemes;