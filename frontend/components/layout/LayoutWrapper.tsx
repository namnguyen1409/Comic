"use client";
// components/layout/LayoutWrapper.tsx
import { Button, Grid, Layout, Menu, Modal, theme } from "antd";
import { Content } from "antd/lib/layout/layout";
import { useState } from "react";
import Sider from "antd/es/layout/Sider";

import { HistoryOutlined, LogoutOutlined, MenuFoldOutlined, MenuUnfoldOutlined, UserAddOutlined, UserOutlined, UserSwitchOutlined } from "@ant-design/icons";
import { axiosLogout } from "@/utils/axiosLogout";
import CustomHeader from "./CustomHeader";
import { Footer } from "antd/es/layout/layout";


import { useRouter, usePathname } from "next/navigation";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "@/lib/features/auth/authSlice";
import { useAccess } from "@/hooks/useAccess";
import { selectIsAuthenticated } from "@/lib/features/auth/authSelector";
import { useAuthSecurity } from "@/lib/features/security/AuthSecurityProvider";
import Title from "antd/es/typography/Title";
const { useBreakpoint } = Grid;


export default function LayoutWrapper({ children }: { children: React.ReactNode }) {

  const dispatch = useDispatch();
  const [collapsed, setCollapsed] = useState(false)
  const router = useRouter();
  const pathname = usePathname();
  const {
    token: { colorBgContainer, borderRadiusLG }
  } = theme.useToken()

  const [isLogout, setIsLogout] = useState(false);

  const { isAuthenticated } = useAuthSecurity();
  // DEBUG: Log isAuthenticated to verify value
  // Remove this after debugging
  console.log('isAuthenticated:', isAuthenticated);

  const getSelectedKey = () => {
    if (pathname.includes('/home')) return '1'
    if (pathname.includes('/login-history')) return '2'
    if (pathname.includes('/users')) return '3'
    if (pathname.includes('/roles')) return '4'
    if (pathname.includes('/permission')) return '5'
    if (pathname.includes('/endpoint')) return '6'
    return '1'
  }

  const screens = useBreakpoint();

  // FIX: Call useAccess at the top level, not inside render
  const canAccessProfile = useAccess("/profile", "GET");
  const canAccessLoginHistory = useAccess("/login-history", "GET");
  const canAccessUsers = useAccess("/users", "GET");
  const canAccessRoles = useAccess("/roles", "GET");
  const canAccessPermission = useAccess("/permission", "GET");
  const canAccessEndpoints = useAccess("/endpoints", "GET");

  // Build menu items array outside of JSX
  const menuItems = [
    ...(canAccessProfile
      ? [{
        key: '1',
        onClick: () => router.push('/home'),
        icon: <UserOutlined />,
        label: 'Profile'
      }]
      : []),
    ...(canAccessLoginHistory
      ? [{
        key: '2',
        onClick: () => router.push('/login-history'),
        icon: <HistoryOutlined />,
        label: 'Login History'
      }]
      : []),

    ...(canAccessUsers
      ? [
        {
          key: '3',
          onClick: () => router.push('/users'),
          icon: <UserAddOutlined />,
          label: 'Users Management'
        }
      ]
      : []
    ),
    ...(canAccessRoles
      ? [
        {
          key: '4',
          onClick: () => router.push('/roles'),
          icon: <UserSwitchOutlined />,
          label: 'Roles Management'
        }
      ]
      : []),
    ...(canAccessPermission
      ? [
        {
          key: '5',
          onClick: () => router.push('/permission'),
          icon: <UserSwitchOutlined />,
          label: 'Permission Management'
        }
      ]
      : []),
    ...(canAccessEndpoints
      ? [
        {
          key: '6',
          onClick: () => router.push('/endpoint'),
          icon: <UserSwitchOutlined />,
          label: 'Endpoint Management'
        }
      ]
      : []),
    {
      key: '7',
      icon: <LogoutOutlined />,
      onClick: () => setIsLogout(true),
      label: 'Logout'
    }
  ];

  return (
    <Layout className='!min-h-screen'>

      {
        screens.md && isAuthenticated && (
          <>
            <Sider trigger={null} collapsible collapsed={collapsed} width={240}
              style={{
                borderRight: 0,
                backgroundColor: colorBgContainer,
                borderRadius: borderRadiusLG
              }}
            >
              <Title
                level={3}
                className="mb-4 mt-4 text-center"
              
              >{collapsed ? <UserSwitchOutlined /> : 'USER MANAGE'}</Title>
              <Menu
                mode='inline'
                selectedKeys={[getSelectedKey()]}
                items={menuItems}
              />
            </Sider>

            <Modal
              open={isLogout}
              onCancel={() => setIsLogout(false)}
              onOk={() => {
                axiosLogout()
                dispatch(logout())
                router.push('/login')
              }}
              title='Confirm Logout'
              okText='Logout'
              cancelText='Cancel'
            >
              <p>Are you sure you want to logout?</p>
            </Modal>
          </>
        )
      }
      <Layout>
        <CustomHeader>
          {screens.md && isAuthenticated && (
            <Button
              type='text'
              icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
              onClick={() => setCollapsed(!collapsed)}
              style={{
                fontSize: '16px',
                width: 64,
                height: 64
              }}
            />
          )}

          {
            isAuthenticated && (
              <h1>
                WELLCOME
              </h1>
            )
          }

        </CustomHeader>
        <Content
          style={{
            margin: '24px',
            padding: 24,
            minHeight: 'calc(100vh - 200px)',
            background: colorBgContainer,
            borderRadius: borderRadiusLG
          }}
        >
          <div className='flex-1 flex items-center justify-center'>
            {children}
          </div>
        </Content>
        <Footer className='text-center'>
          User Management System ©2025 Created by Namnguyen
        </Footer>
      </Layout>
    </Layout>
  );
}
