"use client";
import CreateRoleDrawer from "@/components/rolemng/CreateRoleDrawer";
import EditRoleDrawer from "@/components/rolemng/EditRoleDrawer";
import ReusableTable from "@/components/usermng/ReusableTable";
import { useAccess } from "@/hooks/useAccess";
import { PermissionResponse, RoleResponse } from "@/types/api.response";
import { EditOutlined, EyeOutlined } from "@ant-design/icons";
import { Button, FloatButton, Result, Row, Select, Space, Tag, Tooltip } from "antd";
import Title from "antd/es/typography/Title";
import { useRouter } from "next/navigation";
import { title } from "process";
import { useState } from "react";

const Page = () => {

    const [createRoleDrawerOpen, setCreateRoleDrawerOpen] = useState(false)
    const [editRoleDrawerOpen, setEditRoleDrawerOpen] = useState(false)
    const [selectedRoleId, setSelectedRoleId] = useState<string | null>(null)
    const [showRoleDetails, setShowRoleDetails] = useState(false)
    const [showRoleLoginHistory, setShowRoleLoginHistory] = useState(false)
    const [reloadState, setReloadState] = useState(false)

    const [visibleColumns, setVisibleColumns] = useState<string[]>(
        localStorage.getItem('roleTableColumns')?.split(',') || []
    )

    const handleVisibleColumnsChange = (value: string[]) => {
        setVisibleColumns(value)
        localStorage.setItem('roleTableColumns', value.join(','))
    }


    const router = useRouter()

    const canAccessRoles = useAccess("/roles", "GET");
    const canCreateRole = useAccess("/roles/create", "POST");
    const canEditRole = useAccess("/roles/{id}", "PUT");
    const canViewRoleDetails = useAccess("/roles/{id}", "GET");

    if (!canAccessRoles) {
        return (
            <Result status={'403'} title='403'
                subTitle='You do not have permission to access this page.'
            >
                <Button type='primary' onClick={() => router.push('/home')}>
                    Go to Home
                </Button>
            </Result>
        )
    }

    const roleColumns = [
        {
            title: 'Role Name',
            dataIndex: 'name',
            key: 'name',
            sorter: true,
            filter: {
                type: 'text',
                placeholder: 'Search by role name',
                by: ['name']
            }
        },
        {
            title: 'Role Code',
            dataIndex: 'code',
            key: 'code',
            sorter: true,
            filter: {
                type: 'text',
                placeholder: 'Search by role code',
                by: ['code']
            }
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
            sorter: true,
            filter: {
                type: 'text',
                placeholder: 'Search by description',
                by: ['description']
            }
        },
        {
            title: "Permissions",
            dataIndex: "permissions",
            key: "permissions",
            render: (permissions: PermissionResponse[]) => permissions.map(permission => (
                <Tooltip key={permission.id} title={permission.name || permission.code}>
                    <Tag key={permission.id}>
                        {permission.code}
                    </Tag>
                </Tooltip>
            )),
        },
        {
            title: 'Action',
            key: 'action',
            fixed: 'right' as const,
            render: (_: any, record: RoleResponse) => (
                <Space size='middle'>
                    {canEditRole && (
                        <Tooltip title='Chỉnh sửa role'>
                            <Button
                                icon={<EditOutlined />}
                                onClick={() => {
                                    setSelectedRoleId(record.id)
                                    setEditRoleDrawerOpen(true)
                                }}
                            />
                        </Tooltip>
                    )}
                    {canViewRoleDetails && (
                        <Tooltip title='Xem thông tin chi tiết'>
                            <Button
                                icon={<EyeOutlined />}
                                onClick={() => {
                                    setSelectedRoleId(record.id)
                                    setShowRoleDetails(true)
                                }}
                            />
                        </Tooltip>
                    )}
                </Space>
            )
        }
    ]

    return (
        <Row justify='space-between' align='middle' style={{ marginBottom: 16 }}>
            <Title 
                level={2}
                className="mb-4 text-center"
            
            >
                Role Management
            </Title>

            <Select
                mode='multiple'
                allowClear
                placeholder='Select columns to display'
                defaultValue={visibleColumns}
                style={{ width: '100%', marginBottom: 16 }}
                onChange={(value) => {
                    handleVisibleColumnsChange(value)
                }}
            >
                {roleColumns.map((column) => (
                    <Select.Option key={column.key} value={column.key}>
                        {column.title}
                    </Select.Option>
                ))}
            </Select>

            <ReusableTable<RoleResponse>
                reloadState={reloadState}
                apiUrl='/roles'
                columns={roleColumns}
                rowKey='id'
                scroll={{ x: 'max-content' }}
                visibleColumns={visibleColumns}
                style={{ width: '100%' }}
            />

            {
                canCreateRole && (
                    <FloatButton
                        type='primary'
                        onClick={() => setCreateRoleDrawerOpen(true)}
                        className='mb-4'
                    >
                        Create Role
                    </FloatButton>
                )
            }

            <CreateRoleDrawer
                open={createRoleDrawerOpen}
                onClose={() => setCreateRoleDrawerOpen(false)}
                onCreated={() => setReloadState(!reloadState)}
            />

            <EditRoleDrawer
                open={editRoleDrawerOpen}
                onClose={() => setEditRoleDrawerOpen(false)}
                onUpdate={() => {
                    setEditRoleDrawerOpen(false)
                    setReloadState(!reloadState)
                }}
                roleId={selectedRoleId || ''}
            />


        </Row>
    )
}

export default Page
