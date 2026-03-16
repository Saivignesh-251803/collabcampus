import axios from 'axios'

const api = axios.create({
  baseURL: `${import.meta.env.VITE_API_URL}/api`
})

api.interceptors.request.use(config => {
  const token = localStorage.getItem('cc_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  res => res,
  async err => {
    if (err.response?.status === 401) {
      const refresh = localStorage.getItem('cc_refresh')
      if (refresh) {
        try {
          const res = await axios.post(`${import.meta.env.VITE_API_URL}/api/auth/refresh`, { refreshToken: refresh })
          localStorage.setItem('cc_token', res.data.accessToken)
          err.config.headers.Authorization = `Bearer ${res.data.accessToken}`
          return api(err.config)
        } catch { localStorage.clear(); window.location.href = '/login' }
      }
    }
    return Promise.reject(err)
  }
)

export default api
