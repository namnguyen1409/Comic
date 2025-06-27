import { Middleware } from '@reduxjs/toolkit';
import { login, logout, refreshToken } from './authSlice';
import { useEffect } from 'react';

// Save auth state to localStorage on login, logout, refreshToken
export const authLocalStorageMiddleware: Middleware = store => next => action => {
  const result = next(action);
  if (login.match(action)) {
    localStorage.setItem('authData', JSON.stringify(store.getState().auth));
  } else if (logout.match(action)) {
    localStorage.removeItem('authData');
  } else if (refreshToken.match(action)) {
    const auth = store.getState().auth;
    localStorage.setItem('authData', JSON.stringify(auth));
  }
  return result;
};

// Listen for localStorage changes to sync auth state across tabs
export const useAuthLocalStorageSync = (dispatch: any) => {
  useEffect(() => {
    const handler = (event: StorageEvent) => {
      if (event.key === 'authData') {
        if (event.newValue) {
          // Auth data updated in another tab
          const auth = JSON.parse(event.newValue);
          dispatch(login(auth));
        } else {
          // Auth data removed (logout)
          dispatch(logout());
        }
      }
    };
    window.addEventListener('storage', handler);
    return () => window.removeEventListener('storage', handler);
  }, [dispatch]);
};

// Hydrate Redux auth state from localStorage on first load
export const hydrateAuthFromLocalStorage = (dispatch: any) => {
  const authData = localStorage.getItem('authData');
  if (authData) {
    try {
      const auth = JSON.parse(authData);
      dispatch(login(auth));
    } catch {}
  }
};
