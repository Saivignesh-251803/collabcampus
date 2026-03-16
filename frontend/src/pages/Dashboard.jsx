import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function Dashboard() {
  const { user } = useAuth()
  const [stats, setStats] = useState(null)
  const [notices, setNotices] = useState([])

  useEffect(() => {
    if (user?.role === 'ADMIN') {
      api.get('/admin/stats').then(r => setStats(r.data)).catch(() => {})
    }
    api.get('/notices/public/all').then(r => setNotices(r.data.slice(0,5))).catch(() => {})
  }, [])

  const priorityColor = p => p==='URGENT'?'badge-danger':p==='IMPORTANT'?'badge-warning':'badge-gray'

  return (
    <div>
      <div className="page-header">
        <h1>Welcome back, {user?.name} 👋</h1>
        <p>{new Date().toLocaleDateString('en-IN',{weekday:'long',year:'numeric',month:'long',day:'numeric'})} · KNR UNIVERSITY (KNRU)</p>
      </div>

      {user?.role === 'ADMIN' && stats && (
        <div className="grid-4" style={{ marginBottom:24 }}>
          {[
            { label:'Students', value:stats.totalStudents, color:'#4f46e5' },
            { label:'Faculty', value:stats.totalFaculty, color:'#059669' },
            { label:'Courses', value:stats.totalCourses, color:'#d97706' },
            { label:'Departments', value:stats.totalDepartments, color:'#dc2626' },
          ].map(s => (
            <div key={s.label} className="card" style={{ textAlign:'center', borderTop:`3px solid ${s.color}` }}>
              <div style={{ fontSize:32, fontWeight:700, color:s.color }}>{s.value}</div>
              <div style={{ fontSize:13, color:'#64748b', marginTop:4 }}>{s.label}</div>
            </div>
          ))}
        </div>
      )}

      {user?.role === 'ADMIN' && stats && (stats.pendingAppeals > 0 || stats.pendingNotices > 0) && (
        <div className="card" style={{ marginBottom:24, borderLeft:'4px solid #f59e0b', background:'#fffbeb' }}>
          <div style={{ fontSize:14, fontWeight:600, color:'#92400e', marginBottom:8 }}>Pending Actions</div>
          <div style={{ display:'flex', gap:16, flexWrap:'wrap' }}>
            {stats.pendingAppeals > 0 && <span className="badge badge-warning">⚠ {stats.pendingAppeals} Grade Appeals</span>}
            {stats.pendingNotices > 0 && <span className="badge badge-warning">⚠ {stats.pendingNotices} Notices Awaiting Approval</span>}
          </div>
        </div>
      )}

      <div className="card">
        <div style={{ display:'flex', alignItems:'center', justifyContent:'space-between', marginBottom:16 }}>
          <h2 style={{ fontSize:16, fontWeight:600 }}>Latest Notices</h2>
          <a href="/notices" style={{ fontSize:13, color:'#4f46e5', textDecoration:'none' }}>View all →</a>
        </div>
        {notices.length === 0 ? (
          <p style={{ color:'#94a3b8', fontSize:14 }}>No notices yet.</p>
        ) : notices.map(n => (
          <div key={n.id} style={{ padding:'12px 0', borderBottom:'1px solid #f1f5f9' }}>
            <div style={{ display:'flex', alignItems:'center', gap:8, marginBottom:4 }}>
              <span className={`badge ${priorityColor(n.priority)}`}>{n.priority}</span>
              <span style={{ fontSize:14, fontWeight:500 }}>{n.title}</span>
            </div>
            <p style={{ fontSize:13, color:'#64748b', marginLeft:2 }}>{n.content?.substring(0,120)}{n.content?.length>120?'...':''}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
