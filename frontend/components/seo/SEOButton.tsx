'use client';

import { Button } from 'antd';
import { useEffect, useState } from 'react';

export default function SEOButton() {
  const [hasMounted, setHasMounted] = useState(false);

  useEffect(() => {
    setHasMounted(true);
  }, []);

  if (!hasMounted) {
    // Phiên bản dành cho server-side
    return <button type="button">Đọc truyện</button>; // Không dùng antd
  }

  // Phiên bản client-side dùng Ant Design
  return <Button type="primary">Đọc truyện</Button>;
}
