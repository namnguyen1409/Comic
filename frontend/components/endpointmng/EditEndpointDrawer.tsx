import { PermissionResponse, EndpointResponse, RoleResponse } from "@/types/api.response"
import axiosInstance from "@/utils/axiosInstance"
import { Button, Drawer, Form, Input, message, Switch } from "antd"
import { Select } from "antd/lib"
import { useCallback, useEffect, useState } from "react"

interface EditEndpointDrawerProps {
    open: boolean
    onClose: () => void
    onUpdate: () => void
    endpointId: string
}


interface EndpointEditForm {
    name: string
    description: string
    permissionIds: string[]
    roleIds: string[]
    revokedRoleIds: string[]
    isPublic: boolean
}

const EditEndpointDrawer: React.FC<EditEndpointDrawerProps> = ({ open, onClose, onUpdate, endpointId }) => {
    const [form] = Form.useForm()
    const [loading, setLoading] = useState(false)
    const [endpoint, setEndpoint] = useState<EndpointResponse | null>(null)
    const [roles, setRoles] = useState<RoleResponse[]>([])
    const [permissions, setPermissions] = useState<PermissionResponse[]>([])

    const fetchEndpoint = useCallback(async () => {
        try {
            const response = await axiosInstance.get(`/endpoints/${endpointId}`)
            const endpointData = response.data.data
            setEndpoint(endpointData)
            form.setFieldsValue({
                ...endpointData,
                roleIds: endpointData.roles.map((r: EndpointResponse) => r.id),
                permissionIds: endpointData.permissions.map((p: PermissionResponse) => p.id),
                revokedRoleIds: endpointData.revokedRoles.map((r: EndpointResponse) => r.id)
            })
        } catch (error) {
            console.error('Error fetching role:', error)
            message.error('Failed to fetch role details. Please try again.')
        }
    }, [endpointId, form])

    const fetchRoles = useCallback(async () => {
        try {
            const response = await axiosInstance.get('/roles/all')
            setRoles(response.data.data)
        } catch (error) {
            console.error('Error fetching roles:', error)
            message.error('Failed to fetch roles. Please try again.')
        }
    }, [])


    const fetchPermissions = useCallback(async () => {
        try {
            const response = await axiosInstance.get('/permission/all')
            setPermissions(response.data.data)
        }
        catch (error) {
            console.error('Error fetching permissions:', error)
        }
    }, [])

    useEffect(() => {
        if (endpointId) {
            fetchEndpoint()
        }
        fetchRoles()
        fetchPermissions()
    }, [open, endpointId, fetchEndpoint, fetchPermissions])

    const handleSubmit = async (values: EndpointEditForm) => {
        setLoading(true)
        try {
            await axiosInstance.put(`/endpoints/${endpointId}`, {
                ...values,
                permissionIds: values.permissionIds || [],
                roleIds: values.roleIds || [],
                revokedRoleIds: values.revokedRoleIds || []
            })
            message.success('Endpoint updated successfully!')
            onUpdate()
            onClose()
        } catch (error) {
            console.error('Error updated role:', error)
            message.error('Failed to create role. Please try again.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <Drawer title={`Edit endpoint: ${endpoint?.uri}`} width={480} onClose={onClose} open={open} footer={null}>

            <Form
                form={form}
                layout="vertical"
                onFinish={handleSubmit}
                autoComplete="off"
                initialValues={{ code: '', name: '', description: '' }}
            >
                <Form.Item
                    label="Endpoint URI"
                    name="uri"
                    // cannot change the endpoint URI
                >
                    <Input readOnly />
                </Form.Item>

                <Form.Item
                    label="Endpoint Method"
                    name="method"
                    // cannot change the endpoint method
                    rules={[{ required: true, message: 'Please select an endpoint method!' }]}
                >
                    <Select
                        disabled
                        options={[
                            { label: 'GET', value: 'GET' },
                            { label: 'POST', value: 'POST' },
                            { label: 'PUT', value: 'PUT' },
                            { label: 'DELETE', value: 'DELETE' },
                            { label: 'PATCH', value: 'PATCH' }
                        ]}
                        placeholder="Select endpoint method"
                        allowClear
                        style={{ width: '100%' }}
                        filterOption={(input, option) =>
                            option ? (option.label.toLowerCase().includes(input.toLowerCase()) || option.value.toLowerCase().includes(input.toLowerCase()))
                            : false
                        }
                        showSearch
                        notFoundContent="No methods found"
                    />
                </Form.Item>

                <Form.Item
                    label="Description"
                    name="description"
                >
                    <Input.TextArea rows={4} />
                </Form.Item>

                <Form.Item
                    label="Permissions"
                    name="permissionIds"
                >
                    <Select
                        mode="multiple"
                        options={permissions.map(p => ({ label: p.name, value: p.id }))}
                        placeholder="Select permissions"
                        allowClear
                        style={{ width: '100%' }}
                        filterOption={(input, option) =>
                            option ? (option.label.toLowerCase().includes(input.toLowerCase()) || option.value.toLowerCase().includes(input.toLowerCase()))
                            : false 
                        }
                        showSearch
                        notFoundContent="No permissions found"
                    />
                </Form.Item>

                <Form.Item
                    label="Roles"
                    name="roleIds"
                >
                    <Select
                        mode="multiple"
                        options={roles.map(r => ({ label: r.name || r.code, value: r.id }))}
                        placeholder="Select roles"
                        allowClear
                        style={{ width: '100%' }}
                        filterOption={(input, option) =>
                            option ? (option.label.toLowerCase().includes(input.toLowerCase()) || option.value.toLowerCase().includes(input.toLowerCase()))
                            : false
                        }
                        showSearch
                        notFoundContent="No roles found"
                    />
                </Form.Item>
                <Form.Item
                    label="Revoked Roles"
                    name="revokedRoleIds"
                >
                    <Select
                        mode="multiple"
                        options={roles.map(r => ({ label: r.name || r.code, value: r.id }))}
                        placeholder="Select revoked roles"
                        allowClear
                        style={{ width: '100%' }}
                        filterOption={(input, option) =>
                            option ? (option.label.toLowerCase().includes(input.toLowerCase()) || option.value.toLowerCase().includes(input.toLowerCase()))
                            : false
                        }
                        showSearch
                        notFoundContent="No roles found"
                    />
                </Form.Item>
                <Form.Item
                    label="Public Endpoint"
                    name="isPublic"
                    valuePropName="checked"
                >
                    <Switch checkedChildren="Yes" unCheckedChildren="No" defaultChecked />
                </Form.Item>

                

                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Save Changes
                    </Button>
                </Form.Item>
            </Form>

        </Drawer>
    )


}

export default EditEndpointDrawer