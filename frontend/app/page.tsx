'use client';
import { Button, Alert, Card, Typography, Space } from "antd";
import { useRouter } from 'next/navigation';

const { Title } = Typography;

export default function Home() {
  const router = useRouter();
  return (
    <div style={{ padding: '20px' }}>
      <Card>
        <Title level={2}>Welcome to the Comic System</Title>
        <Space direction="vertical" style={{ width: '100%' }}>
          <Alert
            message="Comic System"
            description="This is a simple comic management system built with Next.js and Ant Design."
            type="info"
            showIcon
          />
          <Button type="primary" onClick={() => router.push('/login')}>
            Login
          </Button>
        </Space>
      </Card>
    </div>
  );
}
