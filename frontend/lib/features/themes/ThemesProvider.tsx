'use client';
import { useSelector } from 'react-redux'
import { selectActiveTheme } from './themeSelectors'
import { ConfigProvider, theme as antdTheme } from 'antd'
import { Theme } from './themesSlice'
import React, { useEffect, useMemo, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { LoadingOutlined } from '@ant-design/icons';
import App from 'antd/lib/app/App';

const ThemesProvider = ({ children }: { children: React.ReactNode }) => {
  const activeTheme: Theme = useSelector(selectActiveTheme);
  const [isReady, setIsReady] = useState(false);

  useEffect(() => {
    const timer = setTimeout(() => setIsReady(true), 800); // 0.8s delay
    return () => clearTimeout(timer);
  }, []);

  const filteredToken = useMemo(() =>
    Object.fromEntries(
      Object.entries(activeTheme || {}).filter(([_, v]) => v !== undefined && v !== null)
    ), [activeTheme]
  );

  const algorithms = useMemo(() => {
    const arr = [];
    if (activeTheme.darkMode) arr.push(antdTheme.darkAlgorithm);
    if (activeTheme.sizeMode === 'compact') arr.push(antdTheme.compactAlgorithm);
    return arr;
  }, [activeTheme.darkMode, activeTheme.sizeMode]);

  const themeConfig = useMemo(() => ({
    token: filteredToken,
    algorithm: algorithms,
  }), [filteredToken, algorithms]);


  return (
    <App>
      <AnimatePresence mode='wait'>
        {
          !isReady ? (
            <motion.div
              key="loading"
              className={`w-screen h-screen flex flex-col items-center justify-center ${activeTheme.darkMode ? 'bg-black text-white' : 'bg-gradient-to-br from-blue-100 to-white'
                }`}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              transition={{ duration: 0.6 }}
            >
              <motion.div
                animate={{ rotate: 360 }}
                transition={{ repeat: Infinity, duration: 1, ease: 'linear' }}
                className="text-4xl mb-4"
              >
                <LoadingOutlined spin />
              </motion.div>
              <motion.span
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3 }}
                className="text-lg font-semibold"
              >
                Đang tải theme...
              </motion.span>
            </motion.div>
          ) : (
            <ConfigProvider theme={themeConfig}>
              {children}
            </ConfigProvider>
          )
        }
      </AnimatePresence>
    </App>
  );
};

export default React.memo(ThemesProvider);
