import { useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { useNavigate, Link } from 'react-router-dom'
import toast from 'react-hot-toast'

export default function RegisterPage() {
  const [form, setForm] = useState({ name:'', email:'', password:'' })
  const [loading, setLoading] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async e => {
    e.preventDefault()
    setLoading(true)
    try {
      await register(form.name, form.email, form.password)
      toast.success('Registered! Please login.')
      navigate('/login')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Registration failed')
    } finally { setLoading(false) }
  }

  return (
    <div style={{ minHeight:'100vh', display:'flex', alignItems:'center', justifyContent:'center', background:'linear-gradient(135deg,#1e1b4b 0%,#312e81 100%)' }}>
      <div style={{ background:'white', borderRadius:16, padding:'40px', width:'100%', maxWidth:400, boxShadow:'0 20px 60px rgba(0,0,0,.3)' }}>
        <h1 style={{ fontSize:22, fontWeight:700, marginBottom:6 }}>Create Account</h1>
        <p style={{ fontSize:13, color:'#64748b', marginBottom:24 }}>Join CollabCampus — KNRU</p>
        <form onSubmit={handleSubmit}>
          <div className="form-group"><label>Full Name</label><input className="form-control" required value={form.name} onChange={e=>setForm({...form,name:e.target.value})} placeholder="Your full name"/></div>
          <div className="form-group"><label>Email</label><input className="form-control" type="email" required value={form.email} onChange={e=>setForm({...form,email:e.target.value})} placeholder="your@email.com"/></div>
          <div className="form-group"><label>Password</label><input className="form-control" type="password" required minLength={6} value={form.password} onChange={e=>setForm({...form,password:e.target.value})} placeholder="Min 6 characters"/></div>
          <button type="submit" className="btn btn-primary" style={{ width:'100%', padding:'10px', marginTop:8 }} disabled={loading}>{loading?'Creating...':'Create Account'}</button>
        </form>
        <p style={{ textAlign:'center', marginTop:20, fontSize:13, color:'#64748b' }}>Have an account? <Link to="/login" style={{ color:'#4f46e5', fontWeight:500 }}>Sign in</Link></p>
      </div>
    </div>
  )
}
