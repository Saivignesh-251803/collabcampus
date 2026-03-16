import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function AttendancePage() {
  const { user } = useAuth()
  const [courses, setCourses] = useState([])
  const [sessions, setSessions] = useState({})
  const [reports, setReports] = useState({})
  const [qrModal, setQrModal] = useState(null)
  const [scanModal, setScanModal] = useState(false)
  const [qrInput, setQrInput] = useState('')

  useEffect(() => {
    api.get('/courses').then(r => {
      const all = r.data
      setCourses(all)
      if (user?.role === 'STUDENT') {
        all.forEach(c => {
          api.get('/attendance/report/'+c.id).then(rep => setReports(prev=>({...prev,[c.id]:rep.data}))).catch(()=>{})
        })
      }
    })
  }, [])

  const startSession = async courseId => {
    try {
      const res = await api.post('/attendance/session', { courseId })
      setQrModal(res.data)
      toast.success('Session started — QR valid for 5 minutes')
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const markAttendance = async () => {
    try {
      await api.post('/attendance/mark', { qrToken: qrInput })
      toast.success('Attendance marked!'); setScanModal(false); setQrInput('')
    } catch (err) { toast.error(err.response?.data?.message || 'Invalid or expired QR') }
  }

  const pctColor = p => p >= 75 ? '#10b981' : p >= 50 ? '#f59e0b' : '#ef4444'

  return (
    <div>
      <div className="page-header" style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
        <div><h1>Attendance</h1><p>{user?.role === 'FACULTY' ? 'Start QR sessions for your classes' : 'Your attendance records'}</p></div>
        {user?.role === 'STUDENT' && <button className="btn btn-primary" onClick={()=>setScanModal(true)}>📷 Scan QR</button>}
      </div>

      {user?.role === 'FACULTY' && (
        <div className="grid-3">
          {courses.filter(c=>c.faculty?.email===user?.email).map(c=>(
            <div key={c.id} className="card">
              <h3 style={{ fontSize:15, fontWeight:600, marginBottom:4 }}>{c.name}</h3>
              <p style={{ fontSize:13, color:'#64748b', marginBottom:16 }}>{c.code} · {c.department?.name}</p>
              <button className="btn btn-primary btn-sm" onClick={()=>startSession(c.id)}>Start QR Session</button>
            </div>
          ))}
        </div>
      )}

      {user?.role === 'STUDENT' && (
        <div className="grid-3">
          {courses.map(c => {
            const rep = reports[c.id]
            const pct = rep?.percentage || 0
            return (
              <div key={c.id} className="card">
                <h3 style={{ fontSize:15, fontWeight:600, marginBottom:4 }}>{c.name}</h3>
                <p style={{ fontSize:13, color:'#64748b', marginBottom:12 }}>{c.code}</p>
                {rep ? (
                  <div>
                    <div style={{ display:'flex', justifyContent:'space-between', fontSize:13, marginBottom:6 }}>
                      <span>{rep.attended}/{rep.totalSessions} classes</span>
                      <span style={{ fontWeight:600, color:pctColor(pct) }}>{pct}%</span>
                    </div>
                    <div style={{ height:6, background:'#e2e8f0', borderRadius:3 }}>
                      <div style={{ height:'100%', width:pct+'%', background:pctColor(pct), borderRadius:3, transition:'width .3s' }}/>
                    </div>
                    {pct < 75 && <p style={{ fontSize:11, color:'#ef4444', marginTop:6 }}>⚠ Below minimum 75%</p>}
                  </div>
                ) : <p style={{ fontSize:13, color:'#94a3b8' }}>No data</p>}
              </div>
            )
          })}
        </div>
      )}

      {qrModal && (
        <div className="modal-overlay">
          <div className="modal" style={{ textAlign:'center' }}>
            <h2>QR Code — Active Session</h2>
            <p style={{ fontSize:13, color:'#64748b', marginBottom:16 }}>Show this to students. Expires in 5 minutes.</p>
            <div style={{ background:'#f1f5f9', padding:24, borderRadius:12, marginBottom:16, fontFamily:'monospace', fontSize:14, wordBreak:'break-all', letterSpacing:1 }}>
              {qrModal.qrToken}
            </div>
            <p style={{ fontSize:12, color:'#94a3b8', marginBottom:16 }}>Students scan with the "Scan QR" button in the app</p>
            <button className="btn btn-primary" onClick={()=>setQrModal(null)}>Close Session</button>
          </div>
        </div>
      )}

      {scanModal && (
        <div className="modal-overlay" onClick={()=>setScanModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>Enter QR Code</h2>
            <p style={{ fontSize:13, color:'#64748b', marginBottom:16 }}>Enter the token shown by your faculty</p>
            <div className="form-group">
              <input className="form-control" value={qrInput} onChange={e=>setQrInput(e.target.value)} placeholder="Paste QR token here" autoFocus/>
            </div>
            <div style={{ display:'flex', gap:8, justifyContent:'flex-end' }}>
              <button className="btn btn-outline" onClick={()=>setScanModal(false)}>Cancel</button>
              <button className="btn btn-primary" onClick={markAttendance} disabled={!qrInput}>Mark Present</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
