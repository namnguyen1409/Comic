"use client";
import "@ant-design/v5-patch-for-react-19";
import React, { useEffect } from "react";
import { AntdRegistry } from "@ant-design/nextjs-registry";
import ThemesProvider from "@/lib/features/themes/ThemesProvider";
import StoreProvider from "@/lib/StoreProvider";
import LayoutWrapper from "../components/layout/LayoutWrapper";
import './globals.css'
import { App as AntApp } from "antd";
import AuthSecurityProvider from "@/lib/features/security/AuthSecurityProvider";
import AuthHydrator from "@/components/AuthHydrator";

export default function RootLayout({
  children
}: Readonly<{
  children: React.ReactNode;
}>) {

  return (
    <html lang="en" suppressHydrationWarning>
      <body className="antialiased">
        <AntApp>
          <StoreProvider>
            <AuthHydrator />
            <ThemesProvider>
              <AntdRegistry>
                <AuthSecurityProvider>
                  <LayoutWrapper>
                      {children}
                  </LayoutWrapper>
                </AuthSecurityProvider>
              </AntdRegistry>
            </ThemesProvider>
          </StoreProvider>
        </AntApp>
      </body>
    </html>
  );
}
