import { useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { useNavigate, Link } from 'react-router-dom'
import toast from 'react-hot-toast'

export default function LoginPage() {
  const [form, setForm] = useState({ email: '', password: '' })
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async e => {
    e.preventDefault()
    setLoading(true)
    try {
      await login(form.email, form.password)
      toast.success('Welcome back!')
      navigate('/dashboard')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Invalid credentials')
    } finally { setLoading(false) }
  }

  return (
    <div style={{ minHeight:'100vh', display:'flex', alignItems:'center', justifyContent:'center', background:'linear-gradient(135deg,#1e1b4b 0%,#312e81 100%)' }}>
      <div style={{ background:'white', borderRadius:16, padding:'40px', width:'100%', maxWidth:400, boxShadow:'0 20px 60px rgba(0,0,0,.3)' }}>
        <div style={{ textAlign:'center', marginBottom:28 }}>
          <div style={{ width:56, height:56, background:'#4f46e5', borderRadius:14, display:'inline-flex', alignItems:'center', justifyContent:'center', fontSize:22, fontWeight:800, color:'white', marginBottom:12 }}>CC</div>
          <h1 style={{ fontSize:22, fontWeight:700 }}>CollabCampus</h1>
          <p style={{ fontSize:13, color:'#64748b', marginTop:4 }}>KNR UNIVERSITY (KNRU)</p>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email address</label>
            <input className="form-control" type="email" required value={form.email} onChange={e=>setForm({...form,email:e.target.value})} placeholder="your@email.com"/>
          </div>
          <div className="form-group">
            <label>Password</label>
            <input className="form-control" type="password" required value={form.password} onChange={e=>setForm({...form,password:e.target.value})} placeholder="••••••••"/>
          </div>
          <button type="submit" className="btn btn-primary" style={{ width:'100%', padding:'10px', marginTop:8 }} disabled={loading}>
            {loading ? 'Signing in...' : 'Sign in'}
          </button>
        </form>
        <p style={{ textAlign:'center', marginTop:20, fontSize:13, color:'#64748b' }}>
          No account? <Link to="/register" style={{ color:'#4f46e5', fontWeight:500 }}>Register here</Link>
        </p>
      </div>
    </div>
  )
}
