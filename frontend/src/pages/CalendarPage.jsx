import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function CalendarPage() {
  const { user } = useAuth()
  const [events, setEvents] = useState([])
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ title:'', description:'', type:'GENERAL', eventDate:'', endDate:'' })

  const load = () => api.get('/events').then(r=>setEvents(r.data)).catch(()=>{})
  useEffect(()=>{ load() },[])

  const handleCreate = async e => {
    e.preventDefault()
    try { await api.post('/events', form); toast.success('Event added'); setModal(false); load() }
    catch (err) { toast.error('Error') }
  }

  const typeColors = { EXAM:'#fee2e2', HOLIDAY:'#d1fae5', ASSIGNMENT_DEADLINE:'#fef3c7', CLUB_EVENT:'#e0e7ff', GENERAL:'#f1f5f9' }
  const typeBadge = { EXAM:'badge-danger', HOLIDAY:'badge-success', ASSIGNMENT_DEADLINE:'badge-warning', CLUB_EVENT:'badge-primary', GENERAL:'badge-gray' }

  const upcoming = events.filter(e => new Date(e.eventDate) >= new Date()).slice(0,20)
  const past = events.filter(e => new Date(e.eventDate) < new Date()).slice(0,5)

  return (
    <div>
      <div className="page-header" style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
        <div><h1>Academic Calendar</h1><p>Upcoming events and deadlines</p></div>
        {(user?.role === 'ADMIN') && <button className="btn btn-primary" onClick={()=>setModal(true)}>+ Add Event</button>}
      </div>

      <h3 style={{ fontSize:14, fontWeight:600, color:'#64748b', marginBottom:12, textTransform:'uppercase', letterSpacing:.5 }}>Upcoming</h3>
      <div style={{ display:'flex', flexDirection:'column', gap:8, marginBottom:24 }}>
        {upcoming.map(e => (
          <div key={e.id} className="card" style={{ background:typeColors[e.type]||'white', display:'flex', alignItems:'center', gap:16 }}>
            <div style={{ textAlign:'center', minWidth:48 }}>
              <div style={{ fontSize:20, fontWeight:700, color:'#1e293b' }}>{new Date(e.eventDate).getDate()}</div>
              <div style={{ fontSize:11, color:'#64748b' }}>{new Date(e.eventDate).toLocaleString('en',{month:'short'})}</div>
            </div>
            <div style={{ flex:1 }}>
              <div style={{ display:'flex', alignItems:'center', gap:8 }}>
                <span className={`badge ${typeBadge[e.type]}`}>{e.type?.replace('_',' ')}</span>
                <span style={{ fontSize:14, fontWeight:500 }}>{e.title}</span>
              </div>
              {e.description && <p style={{ fontSize:12, color:'#64748b', marginTop:4 }}>{e.description}</p>}
            </div>
            {user?.role === 'ADMIN' && <button className="btn btn-sm btn-danger" onClick={()=>api.delete('/events/'+e.id).then(()=>{toast.success('Deleted');load()})}>Del</button>}
          </div>
        ))}
        {upcoming.length === 0 && <div className="card"><p style={{ color:'#94a3b8' }}>No upcoming events.</p></div>}
      </div>

      {past.length > 0 && <>
        <h3 style={{ fontSize:14, fontWeight:600, color:'#94a3b8', marginBottom:12, textTransform:'uppercase', letterSpacing:.5 }}>Past Events</h3>
        <div style={{ display:'flex', flexDirection:'column', gap:8, opacity:.6 }}>
          {past.map(e=>(
            <div key={e.id} className="card" style={{ display:'flex', alignItems:'center', gap:16 }}>
              <div style={{ textAlign:'center', minWidth:48 }}>
                <div style={{ fontSize:20, fontWeight:700 }}>{new Date(e.eventDate).getDate()}</div>
                <div style={{ fontSize:11, color:'#64748b' }}>{new Date(e.eventDate).toLocaleString('en',{month:'short'})}</div>
              </div>
              <div style={{ flex:1 }}><span style={{ fontSize:14 }}>{e.title}</span></div>
            </div>
          ))}
        </div>
      </>}

      {modal && (
        <div className="modal-overlay" onClick={()=>setModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>Add Event</h2>
            <form onSubmit={handleCreate}>
              <div className="form-group"><label>Title</label><input className="form-control" required value={form.title} onChange={e=>setForm({...form,title:e.target.value})}/></div>
              <div className="form-group"><label>Description</label><input className="form-control" value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/></div>
              <div className="form-group"><label>Type</label>
                <select className="form-control" value={form.type} onChange={e=>setForm({...form,type:e.target.value})}>
                  <option>GENERAL</option><option>EXAM</option><option>HOLIDAY</option><option>ASSIGNMENT_DEADLINE</option><option>CLUB_EVENT</option>
                </select>
              </div>
              <div className="grid-2">
                <div className="form-group"><label>Date</label><input className="form-control" type="date" required value={form.eventDate} onChange={e=>setForm({...form,eventDate:e.target.value})}/></div>
                <div className="form-group"><label>End Date</label><input className="form-control" type="date" value={form.endDate} onChange={e=>setForm({...form,endDate:e.target.value})}/></div>
              </div>
              <div style={{ display:'flex', gap:8, justifyContent:'flex-end' }}>
                <button type="button" className="btn btn-outline" onClick={()=>setModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Add</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
