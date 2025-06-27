"use client";
import CreatePermissionDrawer from "@/components/permissionmng/CreatePermissionDrawer";
import ReusableTable from "@/components/usermng/ReusableTable";
import { useAccess } from "@/hooks/useAccess";
import { PermissionResponse } from "@/types/api.response";
import { Button, Result, Row, Select } from "antd";
import Title from "antd/es/typography/Title";
import { useRouter } from "next/navigation";
import { useState } from "react";

const Page = () => {

    const [createPermissionDrawerOpen, setCreatePermissionDrawerOpen] = useState(false)
    const [editPermissionDrawerOpen, setEditPermissionDrawerOpen] = useState(false)
    const [selectedPermissionId, setSelectedPermissionId] = useState<string | null>(null)
    const [showPermissionDetails, setShowPermissionDetails] = useState(false)
    const [showPermissionLoginHistory, setShowPermissionLoginHistory] = useState(false)
    const [reloadState, setReloadState] = useState(false)

    const [visibleColumns, setVisibleColumns] = useState<string[]>(
        localStorage.getItem('PermissionTableColumns')?.split(',') || []
    )

    const handleVisibleColumnsChange = (value: string[]) => {
        setVisibleColumns(value)
        localStorage.setItem('PermissionTableColumns', value.join(','))
    }


    const router = useRouter()

    const canAccessPermission = useAccess("/permission", "GET");
    const canCreatePermission = useAccess("/permission/create", "POST");

    if (!canAccessPermission) {
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

    const PermissionColumns = [
        {
            title: 'Permission Name',
            dataIndex: 'name',
            key: 'name',
            sorter: true,
            filter: {
                type: 'text',
                placeholder: 'Search by Permission name',
                by: ['name']
            }
        },
        {
            title: 'Permission Code',
            dataIndex: 'code',
            key: 'code',
            sorter: true,
            filter: {
                type: 'text',
                placeholder: 'Search by Permission code',
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
    ]

    return (
        <Row justify='space-between' align='middle' style={{ marginBottom: 16 }}>
            <Title 
                level={2}
                className="mb-4 text-center"
            
            >
                Permission Management
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
                {PermissionColumns.map((column) => (
                    <Select.Option key={column.key} value={column.key}>
                        {column.title}
                    </Select.Option>
                ))}
            </Select>

            <ReusableTable<PermissionResponse>
                reloadState={reloadState}
                apiUrl='/permission'
                columns={PermissionColumns}
                rowKey='id'
                scroll={{ x: 'max-content' }}
                visibleColumns={visibleColumns}
                style={{ width: '100%' }}
            />

            {
                canCreatePermission && (
                    <Button
                        type='primary'
                        onClick={() => setCreatePermissionDrawerOpen(true)}
                        className='mb-4'
                    >
                        Create Permission
                    </Button>
                )
            }

            <CreatePermissionDrawer
                open={createPermissionDrawerOpen}
                onClose={() => setCreatePermissionDrawerOpen(false)}
                onCreated={() => setReloadState(!reloadState)}
            />

        </Row>
    )
}

export default Page
