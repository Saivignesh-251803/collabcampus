import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function DepartmentsPage() {
  const { user } = useAuth()
  const [depts, setDepts] = useState([])
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ name:'', code:'', description:'', hodName:'' })

  const load = () => api.get('/departments').then(r => setDepts(r.data)).catch(()=>{})
  useEffect(() => { load() }, [])

  const handleSubmit = async e => {
    e.preventDefault()
    try {
      await api.post('/departments', form)
      toast.success('Department created')
      setModal(false); setForm({ name:'', code:'', description:'', hodName:'' }); load()
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const handleDelete = async id => {
    if (!confirm('Delete this department?')) return
    try { await api.delete('/departments/'+id); toast.success('Deleted'); load() }
    catch (err) { toast.error('Cannot delete') }
  }

  return (
    <div>
      <div className="page-header" style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
        <div><h1>Departments</h1><p>All academic departments at KNRU</p></div>
        {user?.role === 'ADMIN' && <button className="btn btn-primary" onClick={()=>setModal(true)}>+ New Department</button>}
      </div>

      <div className="grid-3">
        {depts.map(d => (
          <div key={d.id} className="card" style={{ borderTop:'3px solid #4f46e5' }}>
            <div style={{ display:'flex', justifyContent:'space-between', alignItems:'flex-start' }}>
              <div>
                <span className="badge badge-primary" style={{ marginBottom:8 }}>{d.code}</span>
                <h3 style={{ fontSize:15, fontWeight:600, marginBottom:4 }}>{d.name}</h3>
                <p style={{ fontSize:13, color:'#64748b' }}>{d.description}</p>
                {d.hodName && <p style={{ fontSize:12, color:'#94a3b8', marginTop:6 }}>HOD: {d.hodName}</p>}
              </div>
              {user?.role === 'ADMIN' && (
                <button className="btn btn-danger btn-sm" onClick={()=>handleDelete(d.id)}>Del</button>
              )}
            </div>
          </div>
        ))}
      </div>

      {modal && (
        <div className="modal-overlay" onClick={()=>setModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>New Department</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group"><label>Name</label><input className="form-control" required value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/></div>
              <div className="form-group"><label>Code</label><input className="form-control" required value={form.code} onChange={e=>setForm({...form,code:e.target.value})} placeholder="e.g. CSE"/></div>
              <div className="form-group"><label>Description</label><input className="form-control" value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/></div>
              <div className="form-group"><label>HOD Name</label><input className="form-control" value={form.hodName} onChange={e=>setForm({...form,hodName:e.target.value})}/></div>
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
