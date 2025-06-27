export interface ApiResponse<T> {
    code: number
    message: string
    data: T
}

export interface LoginResponse {
    token: string
    refreshToken?: string
    isAuthenticated?: boolean
    loginLogId: string
    expiresAt: Date
}

export interface PermissionResponse {
    id: string
    name: string
    code: string
    description: string
}

export interface refreshTokenResponse {
    token: string
    isAuthenticated: boolean
}

export interface RoleResponse {
    id: string
    name: string
    code: string
    description: string
    permissions: PermissionResponse[]
}

export interface EndpointResponse {
    id: string
    uri: string
    method: string
    description: string
    roles: RoleResponse[]
    permissions: PermissionResponse[]
    revokedRoles: RoleResponse[]
}