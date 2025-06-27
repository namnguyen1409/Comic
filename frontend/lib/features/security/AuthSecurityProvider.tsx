"use client";
import React, { useEffect, useState, useRef, createContext, useContext } from "react";
import { jwtDecode } from "jwt-decode";
import { useAppDispatch, useAppSelector } from "@/lib/hooks";
import { setEndpoints, setMyPermissions, setMyRoles } from "../security/securitySlice";
import axiosPublic, { apiPublicCall } from "@/utils/axiosPublic";
import { ApiResponse, EndpointResponse, refreshTokenResponse } from "@/types/api.response";
import { App, notification, message } from "antd";
import { DecodedJwtToken } from "@/types/api";
import { AuthState, logout, refreshToken } from "../auth/authSlice";
import { selectIsAuthenticated, selectToken, selectExpiresAt, selectLoginLogId } from '../auth/authSelector';

interface AuthContextType {
    isAuthenticated: boolean;
    token: string | null;
    expiresAt: Date | null;
    loginLogId: string | null;
    login: (authState: AuthContextType) => void;
    logout: () => void;
    refreshToken: (token: string) => void;
}

const AuthSecurityContext = createContext<AuthContextType | undefined>(undefined);

export const useAuthSecurity = () => {
    const context = useContext(AuthSecurityContext);
    if (!context) {
        throw new Error("useAuthSecurity must be used within an AuthSecurityProvider");
    }
    return context;
};

const AuthSecurityProvider = ({ children }: { children: React.ReactNode }) => {
    const dispatch = useAppDispatch();
    const [api, contextHolder] = notification.useNotification();
    // Use Redux state for auth
    const isAuthenticated = useAppSelector(selectIsAuthenticated);
    const token = useAppSelector(selectToken);
    const expiresAt = useAppSelector(selectExpiresAt);
    const loginLogId = useAppSelector(selectLoginLogId);
    const [sync, setSync] = useState(false);
    const retryCountRef = useRef(0);
    const maxRetries = 5;

    // --- Auth logic ---
    const init = async () => {
        const AuthState = localStorage.getItem("authData");
        if (!AuthState) {
            dispatch(logout());
            return;
        }
        const authData: AuthState = JSON.parse(AuthState);
        if (!authData.token) {
            dispatch(logout());
            return;
        }
        try {
            const decoded: DecodedJwtToken = jwtDecode(authData.token);
            const now = Date.now() / 1000;
            // --- Fix: ensure expiresAt is a Date object ---
            let expiresAtValue = expiresAt;
            if (expiresAtValue && typeof expiresAtValue === 'string') {
                expiresAtValue = new Date(expiresAtValue);
            }
            if (expiresAtValue && expiresAtValue instanceof Date && !isNaN(expiresAtValue.getTime()) && expiresAtValue.getTime() < Date.now()) {
                dispatch(logout());
                return;
            }
            if (decoded.exp && (decoded.exp < now || decoded.exp < now + 150)) {
                const response: ApiResponse<refreshTokenResponse | null> = await apiPublicCall(
                    "/auth/refresh-token",
                    "POST"
                );
    
                if (response.code === 200 && response.data) {
                    dispatch(refreshToken({ token: response.data.token }));
                } else {
                    message.error(response.message || "Token mới không hợp lệ. Vui lòng đăng nhập lại.");
                    dispatch(logout());
                }
            }
            // Security logic: roles/permissions
            const scopeArr = decoded.scope.split(" ");
            const roles: string[] = scopeArr.filter((s: string) => s.startsWith("role_")).map((s: string) => s.replace("role_", ""));
            const permissions = scopeArr.filter((s: string) => !s.startsWith("role_"));
            dispatch(setMyRoles(roles));
            dispatch(setMyPermissions(permissions));
        } catch (err) {
            console.error("Token decode failed:", err);
            message.error("Token không hợp lệ. Vui lòng đăng nhập lại.");
            dispatch(logout());
        }
    };

    const login = (authState: AuthContextType) => {
        init();
    };

    const handleLogout = () => {
        setSync(true);
        dispatch(logout());
    };

    const handleRefreshToken = (newToken: string) => {
        dispatch(refreshToken({ token: newToken }));
    };

    // --- Security logic ---
    const initFromToken = () => {
        // Use authData from localStorage, not access_token
        const authDataStr = localStorage.getItem("authData");
        if (!authDataStr) return;
        try {
            const authData: AuthState = JSON.parse(authDataStr);
            if (!authData.token) return;
            const decodedJwtToken : DecodedJwtToken = jwtDecode(authData.token);
            const scopeArr = decodedJwtToken.scope.split(" ");
            const roles = scopeArr.filter((role: string) => role.startsWith("role_")).map((role: string) => role.replace("role_", ""));
            const permissions = scopeArr.filter((permission: string) => !permission.startsWith("role_"));
            dispatch(setMyRoles(roles));
            dispatch(setMyPermissions(permissions));
        } catch (error) {
            console.error("Error decoding token:", error);
            localStorage.removeItem("authData");
            dispatch(setMyRoles([]));
            dispatch(setMyPermissions([]));
        }
    };

    const loadEndpoints = async () => {
        try {
            const data: ApiResponse<EndpointResponse[] | null> = await apiPublicCall<EndpointResponse[], null>(
                "/endpoints/all",
                "GET",
            );
            if (!data || !data.data) {
                throw new Error("No data received from endpoints API");
            }
            dispatch(setEndpoints(data.data));
        } catch (e) {
            console.error("Error loading endpoints:", e);
            api.error({
                message: "Lỗi tải endpoints",
                description: "Không thể tải danh sách endpoints từ server",
                duration: 5,
                showProgress: true,
            });
        }
    };

    const connectSee = () => {
        const evt = new EventSource(`${process.env.NEXT_PUBLIC_API_URL}/sse/endpoint-updates`);
        evt.onopen = () => {
            api.success({
                message: "Kết nối thành công",
                description: "Đã kết nối đến server thành công",
                duration: 5,
            });
            retryCountRef.current = 0;
        };
        evt.onerror = () => {
            api.error({
                message: "Lỗi kết nối",
                description: `Đang thử lại kết nối đến server ${retryCountRef.current + 1} lần`,
                duration: 5,
                showProgress: true,
            });
            evt.close();
            if (retryCountRef.current < maxRetries) {
                retryCountRef.current++;
                const retryDelay = Math.min(5000 * retryCountRef.current, 30000);
                setTimeout(connectSee, retryDelay);
            }
        };
        evt.addEventListener("endpoints-update", () => {
            loadEndpoints();
        });
        evt.addEventListener("my-roles-update", () => {
            initFromToken();
        });
        return evt;
    };

    const syncSecurity = () => {
        initFromToken();
        loadEndpoints();
    };

    const handleOffline = () => {
        setSync(true);
        api.error({
            message: "Mất kết nối",
            description: "Bạn đang offline, vui lòng kiểm tra mạng.",
            duration: 5,
        });
    };

    useEffect(() => {
        init();
        const evt = connectSee();
        syncSecurity();
        const onlineHandler = () => {
            if (sync) {
                api.success({
                    message: "Kết nối lại",
                    description: "Bạn đã kết nối lại với internet.",
                    duration: 5,
                });
                syncSecurity();
                setSync(false);
            }
        };
        window.addEventListener("online", onlineHandler);
        window.addEventListener("offline", handleOffline);
        return () => {
            evt?.close();
            window.removeEventListener("online", onlineHandler);
            window.removeEventListener("offline", handleOffline);
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [sync]);

    const contextValue: AuthContextType = {
        isAuthenticated,
        token,
        expiresAt,
        loginLogId,
        login,
        logout: handleLogout,
        refreshToken: handleRefreshToken,
    };

    return (
        <AuthSecurityContext.Provider value={contextValue}>
            <App>
                {contextHolder}
                {children}
            </App>
        </AuthSecurityContext.Provider>
    );
};

export default AuthSecurityProvider;
