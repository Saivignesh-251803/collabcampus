import { useState } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function ProfilePage() {
  const { user } = useAuth()
  const [form, setForm] = useState({ name: user?.name||'', phone:'', designation:'' })
  const [loading, setLoading] = useState(false)

  const handleUpdate = async e => {
    e.preventDefault()
    setLoading(true)
    try {
      await api.put('/users/me', form)
      toast.success('Profile updated!')
    } catch { toast.error('Update failed') }
    finally { setLoading(false) }
  }

  const roleColor = { ADMIN:'#dc2626', FACULTY:'#4f46e5', STUDENT:'#059669' }

  return (
    <div style={{ maxWidth:520 }}>
      <div className="page-header"><h1>My Profile</h1><p>Manage your account details</p></div>
      <div className="card" style={{ marginBottom:20, display:'flex', alignItems:'center', gap:16 }}>
        <div style={{ width:64, height:64, borderRadius:'50%', background:'#4f46e5', display:'flex', alignItems:'center', justifyContent:'center', fontSize:24, fontWeight:700, color:'white', flexShrink:0 }}>
          {user?.name?.[0]?.toUpperCase()}
        </div>
        <div>
          <div style={{ fontSize:18, fontWeight:700 }}>{user?.name}</div>
          <div style={{ fontSize:14, color:'#64748b' }}>{user?.email}</div>
          <span style={{ display:'inline-block', marginTop:4, padding:'2px 10px', borderRadius:12, fontSize:12, fontWeight:600, background: roleColor[user?.role]+'20', color:roleColor[user?.role] }}>{user?.role}</span>
        </div>
      </div>
      <div className="card">
        <h2 style={{ fontSize:16, fontWeight:600, marginBottom:20 }}>Update Details</h2>
        <form onSubmit={handleUpdate}>
          <div className="form-group"><label>Full Name</label><input className="form-control" required value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/></div>
          <div className="form-group"><label>Phone</label><input className="form-control" value={form.phone} onChange={e=>setForm({...form,phone:e.target.value})} placeholder="+91 XXXXXXXXXX"/></div>
          <div className="form-group"><label>Designation</label><input className="form-control" value={form.designation} onChange={e=>setForm({...form,designation:e.target.value})} placeholder="e.g. Student / Professor"/></div>
          <button type="submit" className="btn btn-primary" disabled={loading}>{loading?'Saving...':'Save Changes'}</button>
        </form>
      </div>
    </div>
  )
}
