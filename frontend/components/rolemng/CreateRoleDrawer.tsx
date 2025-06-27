import { PermissionResponse } from "@/types/api.response"
import axiosInstance from "@/utils/axiosInstance"
import { Button, Drawer, Form, Input, message } from "antd"
import { Select } from "antd/lib"
import { useCallback, useEffect, useState } from "react"

interface CreateRoleDrawerProps {
    open: boolean
    onClose: () => void
    onCreated: () => void
}


interface RoleCreationForm {
    code: string
    name: string
    description: string
    permissionIds: string[]
}

const CreateRoleDrawer: React.FC<CreateRoleDrawerProps> = ({ open, onClose, onCreated }) => {
    const [form] = Form.useForm()
    const [loading, setLoading] = useState(false)
    const [permissions, setPermissions] = useState<PermissionResponse[]>([])


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
        fetchPermissions()
    }, [])

    const handleSubmit = async (values: RoleCreationForm) => {
        setLoading(true)
        try {
            await axiosInstance.post('/roles/create', {
                ...values,
                permissionIds: values.permissionIds || []
            })
            message.success('Role created successfully!')
            onCreated()
            onClose()
        } catch (error) {
            console.error('Error creating role:', error)
            message.error('Failed to create role. Please try again.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <Drawer title="Create Role" width={480} onClose={onClose} open={open} footer={null}>

            <Form
                form={form}
                layout="vertical"
                onFinish={handleSubmit}
                autoComplete="off"
                initialValues={{ code: '', name: '', description: '' }}
            >
                <Form.Item
                    label="Role Code"
                    name="code"
                    rules={[{ required: true, message: 'Please input the role code!' }]}
                >
                    <Input />
                </Form.Item>
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
                    /> 
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Create Role
                    </Button>
                </Form.Item>
            </Form>

        </Drawer>
    )


}

export default CreateRoleDrawer