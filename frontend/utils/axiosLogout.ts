import axios from 'axios'


export const axiosLogout = async () => {
  try {
    console.log('Logging out...')

    const authData = localStorage.getItem('authData');
    const access_token = authData ? JSON.parse(authData).token : null;
    
    await axios.post(
      `${process.env.NEXT_PUBLIC_API_URL}/auth/logout`,
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization' : `Bearer ${access_token}`
        },
        withCredentials: true
      }
    )
    localStorage.removeItem('authData')
  } catch (err) {
    console.error('Logout failed:', err)
  }
}
