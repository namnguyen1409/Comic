"use client";
import { useEffect } from "react";
import { useAppDispatch } from "@/lib/hooks";
import { hydrateAuthFromLocalStorage } from "@/lib/features/auth/authLocalStorageMiddleware";

export default function AuthHydrator() {
  const dispatch = useAppDispatch();
  useEffect(() => {
    hydrateAuthFromLocalStorage(dispatch);
  }, [dispatch]);
  return null;
}
