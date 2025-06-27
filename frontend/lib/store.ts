import { configureStore } from '@reduxjs/toolkit'
import themesReducer from './features/themes/themesSlice';
import securityReducer from './features/security/securitySlice';
import authReducer from './features/auth/authSlice';


export const makeStore = () => {
  return configureStore({
    reducer: {
        themes: themesReducer,
        security: securityReducer,
        auth: authReducer,
    },
  })
}

// Infer the type of makeStore
export type AppStore = ReturnType<typeof makeStore>
// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<AppStore['getState']>
export type AppDispatch = AppStore['dispatch']