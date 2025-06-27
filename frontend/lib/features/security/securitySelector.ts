import { UrlMethod } from "@/components/usermng/CanAccess";
import { RootState } from "@/lib/store";


export function canAccessEndpoint(
    state: RootState,
    uri: string,
    method: string
):boolean {
    console.log('endpoints:', state.security.endpoints);

    const endpoints = state.security.endpoints.find(
        (endpoint) => endpoint.uri === uri && endpoint.method === method
    )
    if (!endpoints) {
        return false;
    }
    const { roles, revokedRoles } = endpoints;
    const userRoles = state.security.myRoles;
    const userPermissions = state.security.myPermissions;
    const hasRole = roles.some(role => userRoles.includes(role.code)) || roles.length === 0;
    const hasRevokedRole = revokedRoles.some(role => userRoles.includes(role.code));
    const hasPermission = endpoints.permissions.some(permission =>
        userPermissions.includes(permission.code)
    ) || endpoints.permissions.length === 0;
    return (hasRole || hasPermission) && !hasRevokedRole; 
}


export function canAccessAnyEndpoint(state: RootState, urlMethods: UrlMethod[] = []) {
  return urlMethods.some(({ url, method }) =>
    canAccessEndpoint(state, url, method)
  );
}