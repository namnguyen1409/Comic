import axios from 'axios'

const axiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json'
  },
  timeout: 5000,
  withCredentials: true
})

let refreshTokenPromise: Promise<string> | null = null
let failedQueue: { resolve: (token: string) => void; reject: (err: any) => void }[] = []

const processQueue = (error: any, token: string | null) => {
  failedQueue.forEach(({ resolve, reject }) => {
    error ? reject(error) : resolve(token!)
  })
  failedQueue = []
}

const logoutAndRedirect = () => {
  // Just clear localStorage and redirect
  if (typeof window !== 'undefined') {
    localStorage.removeItem('authData');
    window.location.href = '/login';
  }
}

axiosInstance.interceptors.request.use(
  (config) => {
    // Get token from localStorage (single source of truth for non-React code)
    const authData = localStorage.getItem('authData');
    let token = null;
    if (authData) {
      try {
        token = JSON.parse(authData).token;
      } catch {}
    }
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    const errorCode = error?.response?.data?.code

    if (errorCode === 5001 && !originalRequest._retry) {
      originalRequest._retry = true

      // Check expiry from localStorage
      const authData = localStorage.getItem('authData');
      let expiresAt: number | null = null;
      if (authData) {
        try {
          const parsed = JSON.parse(authData);
          expiresAt = parsed.expiresAt ? new Date(parsed.expiresAt).getTime() : null;
        } catch {}
      }
      if (!authData || !expiresAt || expiresAt < Date.now()) {
        logoutAndRedirect();
        return Promise.reject(error);
      }

      if (!refreshTokenPromise) {
        refreshTokenPromise = axios
          .post(`${process.env.NEXT_PUBLIC_API_URL}/auth/refresh-token`, {}, {
            withCredentials: true
          })
          .then(({ data }) => {
            const newToken = data?.data?.token
            if (!newToken) throw new Error('Token refresh failed: empty token')
            // Update localStorage
            const authData = localStorage.getItem('authData');
            if (authData) {
              try {
                const parsed = JSON.parse(authData);
                parsed.token = newToken;
                localStorage.setItem('authData', JSON.stringify(parsed));
              } catch {}
            }
            axiosInstance.defaults.headers['Authorization'] = `Bearer ${newToken}`
            processQueue(null, newToken)
            return newToken
          })
          .catch((err) => {
            processQueue(err, null)
            const code = err?.response?.data?.code
            if (code === 1001) {
              logoutAndRedirect()
            }
            throw err
          })
          .finally(() => {
            refreshTokenPromise = null
          })
      }

      try {
        const newToken = await new Promise<string>((resolve, reject) => {
          failedQueue.push({ resolve, reject })
        })
        originalRequest.headers['Authorization'] = `Bearer ${newToken}`
        return axiosInstance(originalRequest)
      } catch (err) {
        return Promise.reject(err)
      }
    }

    return Promise.reject(error)
  }
)

export default axiosInstance
