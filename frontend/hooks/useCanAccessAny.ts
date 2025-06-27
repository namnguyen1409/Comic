import { UrlMethod } from "@/components/usermng/CanAccess";
import { canAccessAnyEndpoint } from "@/lib/features/security/securitySelector";
import { RootState } from "@/lib/store";

import { useSelector } from "react-redux";


export function useCanAccessAny(urlMethods: UrlMethod[] = []) {
  return useSelector((state: RootState) =>
    canAccessAnyEndpoint(state, urlMethods)
  );
}