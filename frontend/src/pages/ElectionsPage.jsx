import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function ElectionsPage() {
  const { user } = useAuth()
  const [elections, setElections] = useState([])
  const [results, setResults] = useState({})
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ title:'', description:'', nominationStart:'', nominationEnd:'', votingStart:'', votingEnd:'' })

  const load = () => api.get('/elections').then(r=>setElections(r.data)).catch(()=>{})
  useEffect(()=>{ load() },[])

  const loadResults = id => api.get(`/elections/${id}/results`).then(r=>setResults(prev=>({...prev,[id]:r.data}))).catch(()=>{})

  const handleCreate = async e => {
    e.preventDefault()
    try {
      await api.post('/elections', form)
      toast.success('Election created'); setModal(false); load()
    } catch (err) { toast.error('Error') }
  }

  const handleVote = async (electionId, candidateId) => {
    try { await api.post(`/elections/${electionId}/vote`, { candidateId }); toast.success('Vote cast!'); load() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const handleNominate = async (electionId) => {
    const manifesto = prompt('Enter your manifesto (brief statement):')
    if (!manifesto) return
    try { await api.post(`/elections/${electionId}/nominate`, { manifesto }); toast.success('Nominated!'); load() }
    catch (err) { toast.error(err.response?.data?.message || 'Already nominated') }
  }

  const statusColor = { UPCOMING:'badge-gray', NOMINATIONS_OPEN:'badge-primary', VOTING_OPEN:'badge-warning', COMPLETED:'badge-success' }

  return (
    <div>
      <div className="page-header" style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
        <div><h1>Student Council Elections</h1><p>Nominate and vote for your representatives</p></div>
        {user?.role === 'ADMIN' && <button className="btn btn-primary" onClick={()=>setModal(true)}>+ Create Election</button>}
      </div>

      <div style={{ display:'flex', flexDirection:'column', gap:16 }}>
        {elections.map(e => (
          <div key={e.id} className="card">
            <div style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between', marginBottom:12 }}>
              <div>
                <div style={{ display:'flex', alignItems:'center', gap:8, marginBottom:4 }}>
                  <h3 style={{ fontSize:16, fontWeight:600 }}>{e.title}</h3>
                  <span className={`badge ${statusColor[e.status]}`}>{e.status?.replace('_',' ')}</span>
                </div>
                <p style={{ fontSize:13, color:'#64748b' }}>{e.description}</p>
              </div>
              <div style={{ display:'flex', gap:6 }}>
                {e.status === 'NOMINATIONS_OPEN' && user?.role === 'STUDENT' && (
                  <button className="btn btn-outline btn-sm" onClick={()=>handleNominate(e.id)}>Nominate Me</button>
                )}
                {e.status === 'COMPLETED' && (
                  <button className="btn btn-outline btn-sm" onClick={()=>loadResults(e.id)}>View Results</button>
                )}
                {user?.role === 'ADMIN' && (
                  <select className="form-control" style={{ padding:'4px 8px', fontSize:12, width:'auto' }}
                    onChange={ev=>api.put(`/elections/${e.id}/status`,{status:ev.target.value}).then(()=>{toast.success('Status updated');load()})}>
                    <option value="">Update Status</option>
                    <option value="NOMINATIONS_OPEN">Open Nominations</option>
                    <option value="VOTING_OPEN">Open Voting</option>
                    <option value="COMPLETED">Complete</option>
                  </select>
                )}
              </div>
            </div>

            {results[e.id] && (
              <div style={{ background:'#f8fafc', borderRadius:8, padding:12, marginTop:8 }}>
                <div style={{ fontSize:13, fontWeight:600, marginBottom:8 }}>Results</div>
                {results[e.id].results?.map((r,i) => (
                  <div key={r.candidateId} style={{ display:'flex', alignItems:'center', gap:10, marginBottom:6 }}>
                    <span style={{ fontSize:16, width:24 }}>{i===0?'🥇':i===1?'🥈':'🥉'}</span>
                    <span style={{ fontSize:13, flex:1 }}>{r.name}</span>
                    <span style={{ fontSize:13, fontWeight:600, color:'#4f46e5' }}>{r.votes} votes</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
        {elections.length === 0 && <div className="card"><p style={{ color:'#94a3b8' }}>No elections yet.</p></div>}
      </div>

      {modal && (
        <div className="modal-overlay" onClick={()=>setModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()} style={{ maxWidth:540 }}>
            <h2>Create Election</h2>
            <form onSubmit={handleCreate}>
              <div className="form-group"><label>Title</label><input className="form-control" required value={form.title} onChange={e=>setForm({...form,title:e.target.value})}/></div>
              <div className="form-group"><label>Description</label><input className="form-control" value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/></div>
              <div className="grid-2">
                <div className="form-group"><label>Nomination Start</label><input className="form-control" type="datetime-local" required value={form.nominationStart} onChange={e=>setForm({...form,nominationStart:e.target.value})}/></div>
                <div className="form-group"><label>Nomination End</label><input className="form-control" type="datetime-local" required value={form.nominationEnd} onChange={e=>setForm({...form,nominationEnd:e.target.value})}/></div>
                <div className="form-group"><label>Voting Start</label><input className="form-control" type="datetime-local" required value={form.votingStart} onChange={e=>setForm({...form,votingStart:e.target.value})}/></div>
                <div className="form-group"><label>Voting End</label><input className="form-control" type="datetime-local" required value={form.votingEnd} onChange={e=>setForm({...form,votingEnd:e.target.value})}/></div>
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
