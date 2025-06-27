import { Middleware } from '@reduxjs/toolkit';
import { setActiveTheme, saveToLocalStorage, setCurrentTheme, saveTempThemeAs, switchAdvancedMode, switchDarkMode } from './themesSlice';
import { useEffect } from 'react';

// Save theme state to localStorage on relevant actions
export const themeLocalStorageMiddleware: Middleware = store => next => action => {
  const result = next(action);
  // List actions that should trigger saving to localStorage
  if (
    setActiveTheme.match(action) ||
    setCurrentTheme.match(action) ||
    saveTempThemeAs.match(action) ||
    switchAdvancedMode.match(action) ||
    switchDarkMode.match(action) ||
    saveToLocalStorage.match(action)
  ) {
    const state = store.getState().themes;
    localStorage.setItem('themeState', JSON.stringify(state));
  }
  return result;
};

// Listen for localStorage changes to sync theme state across tabs
export const useThemeLocalStorageSync = (dispatch: any) => {
  useEffect(() => {
    const handler = (event: StorageEvent) => {
      if (event.key === 'themeState') {
        if (event.newValue) {
          // Theme state updated in another tab
          const themeState = JSON.parse(event.newValue);
          dispatch(setActiveTheme(themeState));
        }
      }
    };
    window.addEventListener('storage', handler);
    return () => window.removeEventListener('storage', handler);
  }, [dispatch]);
};
