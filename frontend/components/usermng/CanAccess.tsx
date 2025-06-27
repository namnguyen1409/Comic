"use client";

import { useCanAccessAny } from "@/hooks/useCanAccessAny";

export interface UrlMethod {
  url: string;
  method: string;
}

const CanAccess = ({
  urlMethods,
  children
}: {
  urlMethods?: UrlMethod[];
  children: React.ReactNode
}) => {

  if (!urlMethods || urlMethods.length === 0) {
    return <>{children}</>;
  }

  const canAccess = useCanAccessAny(urlMethods);
  if (!canAccess) {
    return null;
  }

  return <>{children}</>;

}

export default CanAccess
