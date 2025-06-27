"use client";
import { useAccess } from '@/hooks/useAccess';
import axiosInstance from '@/utils/axiosInstance'
import { Button, Drawer, Form, Input, message } from 'antd'


interface ChangePasswordDrawerProps {
  open: boolean
  onClose: () => void
  onChangePassword: () => void
}

interface ChangePasswordForm {
  oldPassword: string
  newPassword: string
  confirmNewPassword: string
}

const ChangePasswordDrawer: React.FC<ChangePasswordDrawerProps> = ({ open, onClose, onChangePassword }) => {


  const canAccess = useAccess("/profile/password", "PUT")
  if (!canAccess) {
    return null
  }


  const [form] = Form.useForm()

  const handleSubmit = async (values: ChangePasswordForm) => {
    try {
      await axiosInstance.put('/profile/password', values)
      message.success('Đổi mật khẩu thành công')
      onChangePassword()
      onClose()
    } catch (error) {
      message.error('Lỗi khi đổi mật khẩu')
      console.error('Error changing password:', error)
    }
  }

  return (
    <Drawer title='Đổi mật khẩu' width={480} onClose={onClose} open={open}>
      <Form layout='vertical' form={form} onFinish={handleSubmit}>
        <Form.Item name='oldPassword' label='Mật khẩu cũ' rules={[{ required: true }]}>
          <Input.Password />
        </Form.Item>
        <Form.Item name='newPassword' label='Mật khẩu mới' rules={[{ required: true }]}>
          <Input.Password />
        </Form.Item>
        <Form.Item name='confirmNewPassword' label='Xác nhận mật khẩu mới' rules={[{ required: true }]}>
          <Input.Password />
        </Form.Item>
        <Button type='primary' htmlType='submit'>
          Đổi mật khẩu
        </Button>
      </Form>
    </Drawer>
  )
}

export default ChangePasswordDrawer
