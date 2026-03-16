import React, { createContext, useContext, useState, useEffect } from 'react'
import api from '../services/api'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const stored = localStorage.getItem('cc_user')
    const token = localStorage.getItem('cc_token')
    if (stored && token) setUser(JSON.parse(stored))
    setLoading(false)
  }, [])

  const login = async (email, password) => {
    const res = await api.post('/auth/login', { email, password })
    const { accessToken, refreshToken, user: u } = res.data
    localStorage.setItem('cc_token', accessToken)
    localStorage.setItem('cc_refresh', refreshToken)
    localStorage.setItem('cc_user', JSON.stringify(u))
    setUser(u)
    return u
  }

  const logout = () => {
    localStorage.clear()
    setUser(null)
  }

  const register = async (name, email, password) => {
    await api.post('/auth/register', { name, email, password })
  }

  return (
    <AuthContext.Provider value={{ user, login, logout, register, loading }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
