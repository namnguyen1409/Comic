"use client";

import { useEffect, useState, useCallback, useMemo } from 'react'
import { Card, Avatar, Descriptions, Spin, Typography, Tag, Button, Popconfirm } from 'antd'
import { UserOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'
import { useRouter } from 'next/navigation'
import axiosInstance from '@/utils/axiosInstance'
import EditProfileDrawer from '@/components/usermng/EditProfileDrawer'
import ChangePasswordDrawer from '@/components/usermng/ChangePasswordDrawer'
import CanAccess from '@/components/usermng/CanAccess'


const { Title } = Typography

type Profile = {
  id: string
  username: string
  firstName: string
  lastName: string
  email: string
  phone: string
  gender: boolean
  birthday: string
  address: string
  isDeleted: boolean
  roles: string[]
  revokedPermissions: string[]
}

const Page = () => {
  const [profile, setProfile] = useState<Profile | null>(null)
  const [drawerOpen, setDrawerOpen] = useState(false)
  const [changePasswordOpen, setChangePasswordOpen] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const router = useRouter();

  const fetchProfile = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await axiosInstance.get('/profile')
      setProfile(response.data.data)
    } catch (err) {
      setError('Failed to fetch profile.')
      setProfile(null)
      console.error('Error fetching profile:', err)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchProfile()
  }, [fetchProfile])

  const formattedBirthday = useMemo(() =>
    profile?.birthday ? dayjs(profile.birthday).format('DD/MM/YYYY') : '', [profile?.birthday]
  )

  if (loading) {
    return (
      <div className='flex items-center justify-center min-h-screen'>
        <Spin size='large' />
      </div>
    )
  }

  if (error) {
    return (
      <div className='flex items-center justify-center min-h-screen'>
        <span className='text-red-500'>{error}</span>
        <Button type='link' onClick={fetchProfile}>Retry</Button>
      </div>
    )
  }

  return (
    <div className='flex items-center justify-center w-full'>
      {profile && (
        <Card
          className='w-full max-w-3xl shadow-lg rounded-xl !mt-4 !mb-4'
          title={
            <div className='flex items-center gap-4 h-24'>
              <Avatar size={64} icon={<UserOutlined />} />
              <div>
                <Title level={4} className='!mb-0'>
                  {profile.firstName} {profile.lastName}
                </Title>
                <span className='text-gray-500'>@{profile.username}</span>
              </div>
            </div>
          }
        >
          <Descriptions column={1} layout='vertical' bordered size='middle' className='rounded-md'>
            <Descriptions.Item label='Email'>{profile.email}</Descriptions.Item>
            <Descriptions.Item label='Phone'>{profile.phone}</Descriptions.Item>
            <Descriptions.Item label='Gender'>{profile.gender ? 'Male' : 'Female'}</Descriptions.Item>
            <Descriptions.Item label='Birthday'>{formattedBirthday}</Descriptions.Item>
            <Descriptions.Item label='Address'>{profile.address}</Descriptions.Item>
            <Descriptions.Item label='Roles'>
              {profile.roles.length > 0 ? (
                profile.roles.map((role: any, idx) => (
                  <Tag color='blue' key={`${role.id || role.code || idx}`}>
                    {role.name || role.code || String(role)}
                  </Tag>
                ))
              ) : (
                <span>No roles</span>
              )}
            </Descriptions.Item>
            <Descriptions.Item label='Revoked Permissions'>
              {profile.revokedPermissions.length > 0 ? (
                profile.revokedPermissions.map((permission) => (
                  <Tag color='red' key={permission}>
                    {permission}
                  </Tag>
                ))
              ) : (
                <span>No revoked permissions</span>
              )}
            </Descriptions.Item>
          </Descriptions>
          <EditProfileDrawer
            open={drawerOpen}
            onClose={() => setDrawerOpen(false)}
            profile={profile}
            onUpdated={fetchProfile}
          />
          <ChangePasswordDrawer
            open={changePasswordOpen}
            onClose={() => setChangePasswordOpen(false)}
            onChangePassword={() => setChangePasswordOpen(false)}
          />

          <div className='flex gap-2 mt-4'>
            <Button
              type='primary'
              onClick={() => setDrawerOpen(true)}
            >
              Edit Profile
            </Button>
            <Button
              type='default'
              onClick={() => setChangePasswordOpen(true)}
            >
              Change Password
            </Button>
            <CanAccess urlMethods={
              [{
                url: '/profile',
                method: 'DELETE'
              }]
            }>
              <Popconfirm
                title='Are you sure to delete your account?'
                onConfirm={async () => {
                  try {
                    await axiosInstance.delete('/profile')
                    router.push('/login')
                  } catch (error) {
                    setError('Error deleting account.')
                    console.error('Error deleting account:', error)
                  }
                }}
                okText='Yes'
                cancelText='No'
              >
                <Button danger>
                  Delete Account
                </Button>
              </Popconfirm>
            </CanAccess>
          </div>
        </Card>
      )}
    </div>
  )
}

export default Page
