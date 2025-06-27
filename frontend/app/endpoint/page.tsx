"use client";
import EditEndpointDrawer from "@/components/endpointmng/EditEndpointDrawer";
import ReusableTable from "@/components/usermng/ReusableTable";
import { useAccess } from "@/hooks/useAccess";
import { PermissionResponse, EndpointResponse } from "@/types/api.response";
import { EditOutlined, EyeOutlined } from "@ant-design/icons";
import { Button, FloatButton, Result, Row, Select, Space, Tag, Tooltip } from "antd";
import Title from "antd/es/typography/Title";
import { useRouter } from "next/navigation";
import { useState } from "react";


const methodColorsMap: Record<string, string> = {
    GET: 'green',
    POST: 'blue',
    PUT: 'orange',
    DELETE: 'red',
    PATCH: 'purple',
}


const Page = () => {

    const [createEndpointDrawerOpen, setCreateEndpointDrawerOpen] = useState(false)
    const [editEndpointDrawerOpen, setEditEndpointDrawerOpen] = useState(false)
    const [selectedEndpointId, setSelectedEndpointId] = useState<string | null>(null)
    const [showEndpointDetails, setShowEndpointDetails] = useState(false)
    const [showEndpointLoginHistory, setShowEndpointLoginHistory] = useState(false)
    const [reloadState, setReloadState] = useState(false)

    const [visibleColumns, setVisibleColumns] = useState<string[]>(
        localStorage.getItem('endpointTableColumns')?.split(',') || []
    )

    const handleVisibleColumnsChange = (value: string[]) => {
        setVisibleColumns(value)
        localStorage.setItem('endpointTableColumns', value.join(','))
    }


    const router = useRouter()

    const canAccessEndpoints = useAccess("/endpoints", "GET");
    const canCreateEndpoint = useAccess("/endpoints/create", "POST");
    const canEditEndpoint = useAccess("/endpoints/{id}", "PUT");
    const canViewEndpointDetails = useAccess("/endpoints/{id}", "GET");

    if (!canAccessEndpoints) {
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

    const endpointColumns = [
        {
            title: 'Endpoint URI',
            dataIndex: 'uri',
            key: 'uri',
            sorter: true,
            filter: {
                type: 'text',
                placeholder: 'Search by endpoint uri',
                by: ['uri']
            },
            render: (uri: string) => (
                <Tooltip title={uri}>
                    <Tag>
                        {uri}
                    </Tag>
                </Tooltip>
            )
        },
        {
            title: 'Endpoint method',
            dataIndex: 'method',
            key: 'method',
            sorter: true,
            filter: {
                type: 'text',
                placeholder: 'Search by endpoint method',
                by: ['method']
            },
            render: (method: string) => (
                <Tag color={methodColorsMap[method] || 'default'}>
                    {method}
                </Tag>
            )
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
            title: 'roles',
            dataIndex: 'roles',
            key: 'roles',
            render: (roles: { id: string, name?: string, code?: string }[]) => roles.map(role => (
                <Tooltip key={role.id} title={role.name || role.code}>
                    <Tag key={role.id}>
                        {role.code}
                    </Tag>
                </Tooltip>
            )),
        },
        {
            title: 'revokedRoles',
            dataIndex: 'revokedRoles',
            key: 'revokedRoles',
            render: (revokedRoles: { id: string, name?: string, code?: string }[]) => revokedRoles.map(revokedRole => (
                <Tooltip key={revokedRole.id} title={revokedRole.name || revokedRole.code}>
                    <Tag key={revokedRole.id}>
                        {revokedRole.code}
                    </Tag>
                </Tooltip>
            )),

        }
        ,
        {
            title: "isPublic",
            dataIndex: "isPublic",
            key: "isPublic",
            render: (isPublic: boolean) => (
                <Tag color={isPublic ? 'green' : 'red'}>
                    {isPublic ? 'Yes' : 'No'}
                </Tag>
            ),

        }
        ,
        {
            title: 'Action',
            key: 'action',
            fixed: 'right' as const,
            render: (_: any, record: EndpointResponse) => (
                <Space size='middle'>
                    {canEditEndpoint && (
                        <Tooltip title='Chỉnh sửa endpoint'>
                            <Button
                                icon={<EditOutlined />}
                                onClick={() => {
                                    setSelectedEndpointId(record.id)
                                    setEditEndpointDrawerOpen(true)
                                }}
                            />
                        </Tooltip>
                    )}
                    {canViewEndpointDetails && (
                        <Tooltip title='Xem thông tin chi tiết'>
                            <Button
                                icon={<EyeOutlined />}
                                onClick={() => {
                                    setSelectedEndpointId(record.id)
                                    setShowEndpointDetails(true)
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
                Endpoint Management
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
                {endpointColumns.map((column) => (
                    <Select.Option key={column.key} value={column.key}>
                        {column.title}
                    </Select.Option>
                ))}
            </Select>

            <ReusableTable<EndpointResponse>
                reloadState={reloadState}
                apiUrl='/endpoints'
                columns={endpointColumns}
                scroll={{ x: 'max-content' }}
                visibleColumns={visibleColumns}
                style={{ width: '100%' }}
                defaultSortBy="uri"
            />

            {
                canCreateEndpoint && (
                    <FloatButton
                        type='primary'
                        onClick={() => setCreateEndpointDrawerOpen(true)}
                        className='mb-4'
                    >
                        Create Endpoint
                    </FloatButton>
                )
            }



            <EditEndpointDrawer
                open={editEndpointDrawerOpen}
                onClose={() => setEditEndpointDrawerOpen(false)}
                onUpdate={() => {
                    setEditEndpointDrawerOpen(false)
                    setReloadState(!reloadState)
                }}
                endpointId={selectedEndpointId || ''}
            />


        </Row>
    )
}

export default Page
