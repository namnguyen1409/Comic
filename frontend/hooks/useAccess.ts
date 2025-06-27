// hooks/useAccess.ts
import { canAccessEndpoint } from "@/lib/features/security/securitySelector";
import { useSelector } from "react-redux";

import { RootState } from "@/lib/store";

export function useAccess(uri: string, method: string = "GET") {
    return useSelector((state: RootState) => canAccessEndpoint(state, uri, method));
}
