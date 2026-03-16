import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function NoticeBoardPage() {
  const { user } = useAuth()
  const [notices, setNotices] = useState([])
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ title:'', content:'', priority:'GENERAL' })

  const load = () => api.get('/notices').then(r=>setNotices(r.data)).catch(()=>{})
  useEffect(()=>{ load() },[])

  const handleCreate = async e => {
    e.preventDefault()
    try { await api.post('/notices', form); toast.success('Notice posted'); setModal(false); load() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const handleDelete = async id => {
    try { await api.delete('/notices/'+id); toast.success('Deleted'); load() }
    catch { toast.error('Error') }
  }

  const colors = { URGENT:'#fee2e2', IMPORTANT:'#fef3c7', GENERAL:'#f0f9ff' }
  const badgeClass = { URGENT:'badge-danger', IMPORTANT:'badge-warning', GENERAL:'badge-primary' }

  return (
    <div>
      <div className="page-header" style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
        <div><h1>Notice Board</h1><p>Official announcements from KNRU</p></div>
        {(user?.role === 'ADMIN' || user?.role === 'FACULTY') && (
          <button className="btn btn-primary" onClick={()=>setModal(true)}>+ Post Notice</button>
        )}
      </div>

      <div style={{ display:'flex', flexDirection:'column', gap:12 }}>
        {notices.map(n => (
          <div key={n.id} className="card" style={{ background: colors[n.priority] || 'white', borderLeft:`4px solid ${n.priority==='URGENT'?'#ef4444':n.priority==='IMPORTANT'?'#f59e0b':'#4f46e5'}` }}>
            <div style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
              <div style={{ flex:1 }}>
                <div style={{ display:'flex', alignItems:'center', gap:8, marginBottom:6 }}>
                  <span className={`badge ${badgeClass[n.priority]}`}>{n.priority}</span>
                  <h3 style={{ fontSize:15, fontWeight:600 }}>{n.title}</h3>
                  {!n.approved && <span className="badge badge-warning">Pending Approval</span>}
                </div>
                <p style={{ fontSize:14, color:'#334155', lineHeight:1.6 }}>{n.content}</p>
                <p style={{ fontSize:12, color:'#94a3b8', marginTop:8 }}>
                  Posted by {n.postedBy?.name} · {n.createdAt ? new Date(n.createdAt).toLocaleDateString() : ''}
                </p>
              </div>
              {user?.role === 'ADMIN' && (
                <div style={{ display:'flex', gap:6, flexShrink:0 }}>
                  {!n.approved && <button className="btn btn-sm btn-primary" onClick={()=>api.put('/notices/'+n.id+'/approve').then(()=>{toast.success('Approved');load()})}>Approve</button>}
                  <button className="btn btn-sm btn-danger" onClick={()=>handleDelete(n.id)}>Del</button>
                </div>
              )}
            </div>
          </div>
        ))}
        {notices.length === 0 && <div className="card"><p style={{ color:'#94a3b8' }}>No notices yet.</p></div>}
      </div>

      {modal && (
        <div className="modal-overlay" onClick={()=>setModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>Post Notice</h2>
            <form onSubmit={handleCreate}>
              <div className="form-group"><label>Title</label><input className="form-control" required value={form.title} onChange={e=>setForm({...form,title:e.target.value})}/></div>
              <div className="form-group"><label>Content</label><textarea className="form-control" rows={4} required value={form.content} onChange={e=>setForm({...form,content:e.target.value})}/></div>
              <div className="form-group"><label>Priority</label>
                <select className="form-control" value={form.priority} onChange={e=>setForm({...form,priority:e.target.value})}>
                  <option>GENERAL</option><option>IMPORTANT</option><option>URGENT</option>
                </select>
              </div>
              <div style={{ display:'flex', gap:8, justifyContent:'flex-end' }}>
                <button type="button" className="btn btn-outline" onClick={()=>setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Post</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
