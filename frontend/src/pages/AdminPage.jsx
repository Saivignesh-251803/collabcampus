import { useState, useEffect } from 'react'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function AdminPage() {
  const [stats, setStats] = useState(null)
  const [users, setUsers] = useState([])
  const [tab, setTab] = useState('overview')
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ name:'', email:'', password:'', role:'FACULTY', designation:'', departmentId:'' })
  const [depts, setDepts] = useState([])

  useEffect(() => {
    api.get('/admin/stats').then(r=>setStats(r.data))
    api.get('/users').then(r=>setUsers(r.data))
    api.get('/departments').then(r=>setDepts(r.data))
  }, [])

  const createUser = async e => {
    e.preventDefault()
    try {
      await api.post('/users', form)
      toast.success('User created'); setModal(false)
      api.get('/users').then(r=>setUsers(r.data))
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const toggleUser = async id => {
    await api.put(`/users/${id}/toggle`)
    api.get('/users').then(r=>setUsers(r.data))
    toast.success('Status updated')
  }

  const resetPw = async id => {
    const pw = prompt('New password (min 6 chars):')
    if (!pw || pw.length < 6) return toast.error('Too short')
    await api.put(`/users/${id}/reset-password`, { password: pw })
    toast.success('Password reset')
  }

  const tabs = ['overview','users']

  return (
    <div>
      <div className="page-header"><h1>Admin Panel</h1><p>Full system control — KNR UNIVERSITY (KNRU)</p></div>

      <div style={{ display:'flex', gap:8, marginBottom:24 }}>
        {tabs.map(t=>(
          <button key={t} className={`btn ${tab===t?'btn-primary':'btn-outline'}`} onClick={()=>setTab(t)} style={{ textTransform:'capitalize' }}>{t}</button>
        ))}
      </div>

      {tab === 'overview' && stats && (
        <div>
          <div className="grid-4" style={{ marginBottom:24 }}>
            {[
              {label:'Total Students',value:stats.totalStudents,color:'#4f46e5'},
              {label:'Total Faculty',value:stats.totalFaculty,color:'#059669'},
              {label:'Total Courses',value:stats.totalCourses,color:'#d97706'},
              {label:'Departments',value:stats.totalDepartments,color:'#7c3aed'},
            ].map(s=>(
              <div key={s.label} className="card" style={{ textAlign:'center', borderTop:`3px solid ${s.color}` }}>
                <div style={{ fontSize:36, fontWeight:700, color:s.color }}>{s.value}</div>
                <div style={{ fontSize:13, color:'#64748b', marginTop:4 }}>{s.label}</div>
              </div>
            ))}
          </div>
          {(stats.pendingAppeals>0||stats.pendingNotices>0) && (
            <div className="card" style={{ borderLeft:'4px solid #f59e0b', background:'#fffbeb' }}>
              <div style={{ fontSize:14, fontWeight:600, color:'#92400e', marginBottom:10 }}>Action Required</div>
              <div style={{ display:'flex', gap:12, flexWrap:'wrap' }}>
                {stats.pendingAppeals>0&&<a href="/assignments" style={{ color:'#4f46e5', fontSize:13 }}>📋 {stats.pendingAppeals} pending grade appeals</a>}
                {stats.pendingNotices>0&&<a href="/notices" style={{ color:'#4f46e5', fontSize:13 }}>📢 {stats.pendingNotices} notices awaiting approval</a>}
              </div>
            </div>
          )}
        </div>
      )}

      {tab === 'users' && (
        <div>
          <div style={{ display:'flex', justifyContent:'flex-end', marginBottom:16 }}>
            <button className="btn btn-primary" onClick={()=>setModal(true)}>+ Create User</button>
          </div>
          <div className="card" style={{ padding:0, overflow:'hidden' }}>
            <table className="table">
              <thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Department</th><th>Status</th><th>Actions</th></tr></thead>
              <tbody>
                {users.map(u=>(
                  <tr key={u.id}>
                    <td style={{ fontWeight:500 }}>{u.name}</td>
                    <td style={{ color:'#64748b' }}>{u.email}</td>
                    <td><span className={`badge ${u.role==='ADMIN'?'badge-danger':u.role==='FACULTY'?'badge-primary':'badge-gray'}`}>{u.role}</span></td>
                    <td style={{ fontSize:13, color:'#64748b' }}>{u.department?.name||'—'}</td>
                    <td><span className={`badge ${u.active?'badge-success':'badge-danger'}`}>{u.active?'Active':'Inactive'}</span></td>
                    <td>
                      <div style={{ display:'flex', gap:6 }}>
                        <button className="btn btn-sm btn-outline" onClick={()=>toggleUser(u.id)}>{u.active?'Suspend':'Activate'}</button>
                        <button className="btn btn-sm btn-outline" onClick={()=>resetPw(u.id)}>Reset PW</button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {modal && (
        <div className="modal-overlay" onClick={()=>setModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>Create User</h2>
            <form onSubmit={createUser}>
              <div className="form-group"><label>Full Name</label><input className="form-control" required value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/></div>
              <div className="form-group"><label>Email</label><input className="form-control" type="email" required value={form.email} onChange={e=>setForm({...form,email:e.target.value})}/></div>
              <div className="form-group"><label>Password</label><input className="form-control" type="password" required minLength={6} value={form.password} onChange={e=>setForm({...form,password:e.target.value})}/></div>
              <div className="form-group"><label>Role</label>
                <select className="form-control" value={form.role} onChange={e=>setForm({...form,role:e.target.value})}>
                  <option>FACULTY</option><option>STUDENT</option>
                </select>
              </div>
              <div className="form-group"><label>Designation</label><input className="form-control" value={form.designation} onChange={e=>setForm({...form,designation:e.target.value})} placeholder="e.g. Assistant Professor"/></div>
              <div className="form-group"><label>Department</label>
                <select className="form-control" value={form.departmentId} onChange={e=>setForm({...form,departmentId:e.target.value})}>
                  <option value="">Select (optional)</option>
                  {depts.map(d=><option key={d.id} value={d.id}>{d.name}</option>)}
                </select>
              </div>
              <div style={{ display:'flex', gap:8, justifyContent:'flex-end' }}>
                <button type="button" className="btn btn-outline" onClick={()=>setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Create</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
