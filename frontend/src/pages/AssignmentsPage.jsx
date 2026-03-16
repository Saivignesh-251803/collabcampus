import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function AssignmentsPage() {
  const { user } = useAuth()
  const [assignments, setAssignments] = useState([])
  const [courses, setCourses] = useState([])
  const [modal, setModal] = useState(false)
  const [submitModal, setSubmitModal] = useState(null)
  const [form, setForm] = useState({ title:'', description:'', maxMarks:100, deadline:'', courseId:'' })
  const [submitContent, setSubmitContent] = useState('')

  const load = () => {
    api.get('/assignments/my').then(r => setAssignments(r.data)).catch(()=>{})
    api.get('/courses').then(r => setCourses(r.data)).catch(()=>{})
  }
  useEffect(()=>{ load() },[])

  const handleCreate = async e => {
    e.preventDefault()
    try {
      await api.post('/assignments', form)
      toast.success('Assignment created'); setModal(false); load()
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const handleSubmit = async () => {
    try {
      await api.post(`/assignments/${submitModal.id}/submit`, { content: submitContent })
      toast.success('Submitted!'); setSubmitModal(null); setSubmitContent(''); load()
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const isOverdue = deadline => deadline && new Date(deadline) < new Date()

  return (
    <div>
      <div className="page-header" style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
        <div><h1>Assignments</h1><p>{user?.role === 'FACULTY' ? 'Manage your assignments' : 'Your assignments'}</p></div>
        {user?.role === 'FACULTY' && <button className="btn btn-primary" onClick={()=>setModal(true)}>+ New Assignment</button>}
      </div>

      <div style={{ display:'flex', flexDirection:'column', gap:12 }}>
        {assignments.length === 0 && <div className="card"><p style={{ color:'#94a3b8' }}>No assignments yet.</p></div>}
        {assignments.map(a => (
          <div key={a.id||a.assignment?.id} className="card">
            <div style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between', gap:16 }}>
              <div style={{ flex:1 }}>
                <div style={{ display:'flex', alignItems:'center', gap:8, marginBottom:6 }}>
                  <h3 style={{ fontSize:15, fontWeight:600 }}>{a.title || a.assignment?.title}</h3>
                  {a.deadline && isOverdue(a.deadline) && <span className="badge badge-danger">Overdue</span>}
                  {a.status && <span className={`badge ${a.status==='GRADED'?'badge-success':a.status==='SUBMITTED'?'badge-primary':'badge-warning'}`}>{a.status}</span>}
                </div>
                <p style={{ fontSize:13, color:'#64748b', marginBottom:6 }}>{a.description || a.assignment?.description}</p>
                <div style={{ fontSize:12, color:'#94a3b8' }}>
                  Max: {a.maxMarks || a.assignment?.maxMarks} marks
                  {a.deadline && ` · Due: ${new Date(a.deadline).toLocaleDateString()}`}
                  {a.marksObtained != null && ` · Got: ${a.marksObtained} marks`}
                </div>
                {a.feedback && <p style={{ fontSize:12, color:'#4f46e5', marginTop:6 }}>Feedback: {a.feedback}</p>}
              </div>
              {user?.role === 'STUDENT' && !a.status && (
                <button className="btn btn-primary btn-sm" onClick={()=>setSubmitModal(a)}>Submit</button>
              )}
            </div>
          </div>
        ))}
      </div>

      {modal && (
        <div className="modal-overlay" onClick={()=>setModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>New Assignment</h2>
            <form onSubmit={handleCreate}>
              <div className="form-group"><label>Title</label><input className="form-control" required value={form.title} onChange={e=>setForm({...form,title:e.target.value})}/></div>
              <div className="form-group"><label>Description</label><textarea className="form-control" rows={3} value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/></div>
              <div className="grid-2">
                <div className="form-group"><label>Max Marks</label><input className="form-control" type="number" value={form.maxMarks} onChange={e=>setForm({...form,maxMarks:e.target.value})}/></div>
                <div className="form-group"><label>Deadline</label><input className="form-control" type="datetime-local" value={form.deadline} onChange={e=>setForm({...form,deadline:e.target.value})}/></div>
              </div>
              <div className="form-group"><label>Course</label>
                <select className="form-control" required value={form.courseId} onChange={e=>setForm({...form,courseId:e.target.value})}>
                  <option value="">Select course</option>
                  {courses.map(c=><option key={c.id} value={c.id}>{c.name}</option>)}
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

      {submitModal && (
        <div className="modal-overlay" onClick={()=>setSubmitModal(null)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>Submit: {submitModal.title}</h2>
            <div className="form-group">
              <label>Your Answer</label>
              <textarea className="form-control" rows={6} value={submitContent} onChange={e=>setSubmitContent(e.target.value)} placeholder="Write your answer here..."/>
            </div>
            <div style={{ display:'flex', gap:8, justifyContent:'flex-end' }}>
              <button className="btn btn-outline" onClick={()=>setSubmitModal(null)}>Cancel</button>
              <button className="btn btn-primary" onClick={handleSubmit} disabled={!submitContent}>Submit</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
