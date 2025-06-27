import { RootState } from "@/lib/store";


export const selectIsAuthenticated = (state: RootState): boolean =>
    state.auth.isAuthenticated;

export const selectToken = (state: RootState): string | null =>
    state.auth.token;
export const selectExpiresAt = (state: RootState): Date | null =>
    state.auth.expiresAt;
export const selectLoginLogId = (state: RootState): string | null =>
    state.auth.loginLogId;
export const selectAuthState = (state: RootState) => ({
    isAuthenticated: state.auth.isAuthenticated,
    token: state.auth.token,
    expiresAt: state.auth.expiresAt,
    loginLogId: state.auth.loginLogId,
});
