import { createSlice, PayloadAction } from "@reduxjs/toolkit";



export interface AuthState {
    isAuthenticated: boolean;
    token: string | null;
    expiresAt: Date | null; // thời gian hết hạn của phiên đăng nhập
    loginLogId: string | null;
}

const initialAuthState: AuthState = {
    isAuthenticated: false,
    token: null,
    expiresAt: null,
    loginLogId: null,
}

const authSlice = createSlice({
    name: 'auth',
    initialState: initialAuthState,
    reducers: {
        login: (_state, action: PayloadAction<AuthState>) => {
            return action.payload;
        },
        logout: () => {
            return initialAuthState;
        },
        refreshToken: (state, action: PayloadAction<{ token: string }>) => {
            state.token = action.payload.token;
        }
    }
})

export const { login, logout, refreshToken } = authSlice.actions;
export default authSlice.reducer;