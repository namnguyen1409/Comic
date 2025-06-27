'use client';
import { useState } from 'react'

import { FloatButton, Button, Space, Tooltip, Select, Row, Result, Tag } from 'antd'
import { ClockCircleOutlined, EditOutlined, EyeOutlined, UserAddOutlined } from '@ant-design/icons'
import ReusableTable from '@/components/usermng/ReusableTable'
import CreateUserDrawer from '@/components/usermng/CreateUserDrawer'
import EditUserDrawer from '@/components/usermng/EditUserDrawer'
import ShowUserDetailModel from '@/components/usermng/ShowUserDetailModel'
import ShowUserLoginHistoryModel from '@/components/usermng/ShowUserLoginHistoryModel'
import { useAccess } from '@/hooks/useAccess';
import { useRouter } from 'next/navigation';
import { RoleResponse } from '@/types/api.response';
import axiosInstance from '@/utils/axiosInstance';
import Title from 'antd/es/typography/Title';


interface User {
  id: string
  username: string
  firstName: string
  lastName: string
  email: string
  phone: string
  gender: boolean
  birthday: string
  address: string
  roles: string[]
}

const Page = () => {
  const [createUserDrawerOpen, setCreateUserDrawerOpen] = useState(false)
  const [editUserDrawerOpen, setEditUserDrawerOpen] = useState(false)
  const [selectedUserId, setSelectedUserId] = useState<string | null>(null)
  const [showUserDetails, setShowUserDetails] = useState(false)
  const [showUserLoginHistory, setShowUserLoginHistory] = useState(false)
  const [reloadState, setReloadState] = useState(false)

  const [visibleColumns, setVisibleColumns] = useState<string[]>(
    localStorage.getItem('userTableColumns')?.split(',') || []
  )
  const handleVisibleColumnsChange = (value: string[]) => {
    setVisibleColumns(value)
    localStorage.setItem('userTableColumns', value.join(','))
  }
  const router = useRouter()

  const canAccessUsers = useAccess('/users', 'GET')
  const canCreateUser = useAccess('/users/create', 'POST')
  const canViewUserDetails = useAccess('/users/{id}', 'GET')
  const canEditUser = useAccess('/users/{id}', 'PUT')
  const canViewUserLoginHistory = useAccess('/users/login-history/{id}', 'GET')


  if (!canAccessUsers) {
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

  const userColumns = [
    {
      title: 'Username',
      dataIndex: 'username',
      key: 'username',
      sorter: true,
      filter: {
        type: 'text',
        placeholder: 'Search by username',
        by: ['username']
      }
    },
    {
      title: 'First Name',
      dataIndex: 'firstName',
      key: 'firstName',
      sorter: true,
      filter: {
        type: 'text',
        placeholder: 'Search by first name',
        by: ['firstName']
      }
    },
    {
      title: 'Last Name',
      dataIndex: 'lastName',
      key: 'lastName',
      sorter: true,
      filter: {
        type: 'text',
        placeholder: 'Search by last name',
        by: ['lastName']
      }
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
      sorter: true,
      filter: {
        type: 'text',
        placeholder: 'Search by email',
        by: ['email']
      }
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone',
      sorter: true,
      filter: {
        type: 'text',
        placeholder: 'Search by phone',
        by: ['phone']
      }
    },
    {
      title: 'Gender',
      dataIndex: 'gender',
      key: 'gender',
      render: (gender: boolean) => (gender ? 'Male' : 'Female'),
      sorter: true,
      filter: {
        type: 'select',
        options: [
          {
            label: 'male',
            value: true
          },
          {
            label: 'female',
            value: false
          }
        ],
        placeholder: 'Choose gender',
        by: ['gender']
      }
    },
    {
      title: 'Birthday',
      dataIndex: 'birthday',
      key: 'birthday',
      sorter: true,
      filter: {
        type: 'date',
        placeholder: 'Search by birthday',
        by: ['birthdayFrom', 'birthdayTo'],
        format: 'YYYY-MM-DD',
        viewFormat: 'DD/MM/YYYY'
      }
    },
    {
      title: 'Address',
      dataIndex: 'address',
      key: 'address',
      sorter: true
    },
    {
      title: 'Roles',
      dataIndex: 'roles',
      key: 'roles',
      render: (roles: RoleResponse[]) => roles.map(role => (
        <Tooltip key={role.id} title={role.name || role.code}>
          <Tag key={role.id}>
            {role.code}
          </Tag>
        </Tooltip>
      )),
      sorter: true,
      filter: {
        type: 'select',
        options: (async () => {
          const response = await axiosInstance.get('/roles/all')
          const roles: RoleResponse[] = await response.data;
          return roles.map(role => ({
            label: role.name || role.code,
            value: role.id
          }))
        }
        ),
        placeholder: 'Select roles',
        by: ['roles'],
        multiple: true
      }
    },
    {
      title: 'Revoked Permissions',
      dataIndex: 'revokedPermissions',
      key: 'revokedPermissions',
      render: (revokedPermissions: string[]) => revokedPermissions.join(', ')
    },
    {
      title: 'Action',
      key: 'action',
      fixed: 'right' as const,
      render: (_: any, record: User) => (
        <Space size='middle'>
          {canEditUser && (
            <Tooltip title='Chỉnh sửa người dùng'>
              <Button
                icon={<EditOutlined />}
                onClick={() => {
                  setSelectedUserId(record.id)
                  setEditUserDrawerOpen(true)
                }}
              />
            </Tooltip>
          )}
          {canViewUserDetails && (
            <Tooltip title='Xem thông tin chi tiết'>
              <Button
                icon={<EyeOutlined />}
                onClick={() => {
                  setSelectedUserId(record.id)
                  setShowUserDetails(true)
                }}
              />
            </Tooltip>
          )}
          {canViewUserLoginHistory && (
            <Tooltip title='Xem lịch sử đăng nhập'>
              <Button
                icon={<ClockCircleOutlined />}
                onClick={() => {
                  setSelectedUserId(record.id)
                  setShowUserLoginHistory(true)
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
        User Management
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
        {userColumns.map((column) => (
          <Select.Option key={column.key} value={column.key}>
            {column.title}
          </Select.Option>
        ))}
      </Select>

      <ReusableTable<User>
        reloadState={reloadState}
        apiUrl='/users'
        columns={userColumns}
        rowKey='id'
        scroll={{ x: 'max-content' }}
        visibleColumns={visibleColumns}
      />

      <CreateUserDrawer
        open={createUserDrawerOpen}
        onClose={() => setCreateUserDrawerOpen(false)}
        onCreated={() => {
          setCreateUserDrawerOpen(false)
          setReloadState((prev) => !prev)
        }}
      />

      <EditUserDrawer
        open={editUserDrawerOpen}
        userId={selectedUserId || ''}
        onClose={() => {
          setEditUserDrawerOpen(false)
          setSelectedUserId(null)
        }}
        onCreated={() => {
          setEditUserDrawerOpen(false)
          setReloadState((prev) => !prev)
        }}
      />

      <ShowUserDetailModel
        open={showUserDetails}
        userId={selectedUserId || ''}
        onClose={() => setShowUserDetails(false)}
      />
      <ShowUserLoginHistoryModel
        open={showUserLoginHistory}
        userId={selectedUserId || ''}
        onClose={() => setShowUserLoginHistory(false)}
      />

      {canCreateUser && (
        <FloatButton
          shape='square'
          type='primary'
          style={{ insetInlineEnd: 24 }}
          icon={<UserAddOutlined />}
          onClick={() => {
            setCreateUserDrawerOpen(true)
          }}
          tooltip={<span className='text-sm'>Create User</span>}
        />
      )}
    </Row>
  )
}

export default Page
