import { PermissionResponse, RoleResponse } from "@/types/api.response"
import axiosInstance from "@/utils/axiosInstance"
import { Button, Drawer, Form, Input, message } from "antd"
import { Select } from "antd/lib"
import { useCallback, useEffect, useState } from "react"

interface EditRoleDrawerProps {
    open: boolean
    onClose: () => void
    onUpdate: () => void
    roleId: string
}


interface RoleEditForm {
    name: string
    description: string
    permissionIds: string[]
}

const EditRoleDrawer: React.FC<EditRoleDrawerProps> = ({ open, onClose, onUpdate, roleId }) => {
    const [form] = Form.useForm()
    const [loading, setLoading] = useState(false)
    const [role, setRole] = useState<any>(null)
    const [permissions, setPermissions] = useState<PermissionResponse[]>([])

    const fetchRole = useCallback(async () => {
        try {
            const response = await axiosInstance.get(`/roles/${roleId}`)
            const roleData = response.data.data
            setRole(roleData)
            form.setFieldsValue({
                ...roleData,
                permissionIds: roleData.permissions.map((p: PermissionResponse) => p.id)
            })
        } catch (error) {
            console.error('Error fetching role:', error)
            message.error('Failed to fetch role details. Please try again.')
        }
    }, [roleId, form])


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
        if (roleId) {
            fetchRole()
        }
        fetchPermissions()
    }, [open, roleId, fetchRole, fetchPermissions])

    const handleSubmit = async (values: RoleEditForm) => {
        setLoading(true)
        try {
            await axiosInstance.put(`/roles/${roleId}`, {
                ...values,
                permissionIds: values.permissionIds || []
            })
            message.success('Role updated successfully!')
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
        <Drawer title={`Edit role: ${roleId}`} width={480} onClose={onClose} open={open} footer={null}>

            <Form
                form={form}
                layout="vertical"
                onFinish={handleSubmit}
                autoComplete="off"
                initialValues={{ code: '', name: '', description: '' }}
            >
                <Form.Item
                    label="Role Name"
                    name="name"
                    rules={[{ required: true, message: 'Please input the role name!' }]}
                >
                    <Input />
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
                    rules={[{ required: true, message: 'Please select at least one permission!' }]}
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

                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Save Changes
                    </Button>
                </Form.Item>
            </Form>

        </Drawer>
    )


}

export default EditRoleDrawer