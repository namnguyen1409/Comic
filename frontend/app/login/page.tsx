"use client";

import { Button, Card, Checkbox, Col, Form, Input, message, notification, Row, Typography } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useRouter } from 'next/navigation'

import { useEffect, useState } from 'react'
import { LoginRequest } from '@/types/api.request';
import { ApiResponse, LoginResponse } from '@/types/api.response';
import { apiPublicCall } from '@/utils/axiosPublic';
import { useRolePermission } from '@/hooks/useRolePermission';
import { useAuthSecurity } from '@/lib/features/security/AuthSecurityProvider';

const { Title, Text } = Typography

const Page = () => {
    const { login } = useAuthSecurity();
    const [loading, setLoading] = useState(false)
    const router = useRouter()

    useEffect(() => {
        const rolePermissions = useRolePermission()
        if (rolePermissions && rolePermissions.length > 0) {
            router.push('/home')
        }
    }, [])

    const handleLogin = async (values: any) => {
        const loginRequest: LoginRequest = {
            username: values.username,
            password: values.password,
            rememberMe: values.rememberMe,
        }
        setLoading(true)
        try {
            const response: ApiResponse<LoginResponse | null> = await apiPublicCall<LoginResponse, LoginRequest>(
                '/auth/login',
                'POST',
                loginRequest,
            )
            console.log('Login API response:', response);
            if (!response || typeof response !== 'object') {
                console.error('Login response is null, undefined, or not an object')
                message.error('Login failed. Please check your credentials.')
                return
            }
            if (response.code === 200 && response.data) {
                const authData = {
                    isAuthenticated: true,
                    token: response.data.token,
                    expiresAt: new Date(response.data.expiresAt),
                    loginLogId: response.data.loginLogId,
                    login: () => {},
                    logout: () => {},
                    refreshToken: () => {},
                };
                localStorage.setItem('authData', JSON.stringify(authData));
                login(authData);
                message.success('Login successful!')
                router.push('/home')
            } else {
                notification.error({
                    message: response.code,
                    description: response.message,
                    placement: 'topRight',
                    duration: 3,
                } as any)
            }
        } catch (error) {
            console.error('Login error:', error)
            message.error('Login failed. Please check your credentials.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <Card className='max-w-5xl mx-auto my-12 p-0 overflow-hidden shadow-lg rounded-xl border'>
            <Row className='min-h-[500px]'>
                {/* Form Section */}
                <Col xs={24} md={12} className='flex items-center justify-center'>
                    <div className='w-full max-w-sm p-8'>
                        <div className='text-center mb-6'>
                            <Title level={2} className='!mb-2'>
                                Sign in
                            </Title>
                            <Text type='secondary'>Please enter your details below to sign in.</Text>
                        </div>

                        <Form name='login_form' layout='vertical' onFinish={handleLogin} className='w-full'>
                            <Form.Item name='username' label='Username' rules={[{ required: true }]}>
                                <Input prefix={<UserOutlined />} placeholder='Username' className='rounded-md' />
                            </Form.Item>

                            <Form.Item name='password' label='Password' rules={[{ required: true }]}>
                                <Input.Password prefix={<LockOutlined />} placeholder='Password' className='rounded-md' />
                            </Form.Item>
                            <Form.Item name='rememberMe' valuePropName='checked'>
                                <Checkbox className='text-sm'>
                                    Remember me
                                </Checkbox>
                            </Form.Item>

                            <Form.Item>
                                <Button
                                    type='primary'
                                    htmlType='submit'
                                    block
                                    loading={loading}
                                    className='rounded-md bg-blue-500 hover:bg-blue-600'
                                >
                                    Log in
                                </Button>
                                <div className='text-center mt-4'>
                                    <Text type='secondary'>Don't have an account?</Text>{' '}
                                    <a className='text-blue-500 hover:underline' onClick={() => router.push('/register')}>
                                        Register
                                    </a>
                                </div>
                            </Form.Item>
                        </Form>
                    </div>
                </Col>

                {/* Image Section */}
                <Col xs={0} md={12} className='hidden md:block'>
                    <img
                        src='https://images.unsplash.com/photo-1534239697798-120952b76f2b?auto=format&fit=crop&w=1280&q=80'
                        alt='Login'
                        className='w-full h-full object-cover'
                    />
                </Col>
            </Row>
        </Card>
    )
}

export default Page
