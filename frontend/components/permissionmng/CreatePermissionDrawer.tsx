import axiosInstance from "@/utils/axiosInstance"
import { Button, Drawer, Form, Input, message } from "antd"

import { useState } from "react"


interface CreatePermissionDrawerProps {
    open: boolean
    onClose: () => void
    onCreated: () => void
}


interface PermissionCreationForm {
    code: string
    name: string
    description: string
}

const CreatePermissionDrawer: React.FC<CreatePermissionDrawerProps> = ({ open, onClose, onCreated }) => {
    const [form] = Form.useForm()
    const [loading, setLoading] = useState(false)

    const handleSubmit = async (values: PermissionCreationForm) => {
        setLoading(true)
        try {
            await axiosInstance.post('/permission/create', {
                ...values
            })
            message.success('Permission created successfully!')
            onCreated()
            onClose()
        } catch (error) {
            console.error('Error creating permission:', error)
            message.error('Failed to create permission. Please try again.')
        } finally {
            setLoading(false)
        }
    }


    return (
        <Drawer title="Create Permission" width={480} onClose={onClose} open={open}>
            <Form
                form={form}
                layout="vertical"
                onFinish={handleSubmit}
                autoComplete="off"
                initialValues={{ code: '', name: '', description: '' }}
            >
                <Form.Item
                    label="Permission Code"
                    name="code"
                    rules={[{ required: true, message: 'Please input the permission code!' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label="Permission Name"
                    name="name"
                    rules={[{ required: true, message: 'Please input the permission name!' }]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label="Description"
                    name="description"
                >
                    <Input.TextArea rows={4} />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Create Permission
                    </Button>
                </Form.Item>
            </Form>
        </Drawer>
    )




}

export default CreatePermissionDrawer