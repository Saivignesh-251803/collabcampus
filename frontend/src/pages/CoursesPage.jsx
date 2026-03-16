import { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import api from '../services/api'
import toast from 'react-hot-toast'

export default function CoursesPage() {
  const { user } = useAuth()
  const [courses, setCourses] = useState([])
  const [depts, setDepts] = useState([])
  const [faculty, setFaculty] = useState([])
  const [modal, setModal] = useState(false)
  const [form, setForm] = useState({ name:'', code:'', description:'', credits:3, maxSeats:60, departmentId:'', facultyId:'' })
  const [enrolledIds, setEnrolledIds] = useState([])

  const load = async () => {
    const [cRes, dRes] = await Promise.all([api.get('/courses'), api.get('/departments')])
    setCourses(cRes.data); setDepts(dRes.data)
    if (user?.role !== 'FACULTY') {
      const myRes = await api.get('/courses/my')
      setEnrolledIds(myRes.data.map(e => e.course?.id || e.id))
    }
    if (user?.role === 'ADMIN') {
      const fRes = await api.get('/users/faculty')
      setFaculty(fRes.data)
    }
  }
  useEffect(() => { load() }, [])

  const handleEnroll = async courseId => {
    try { await api.post(`/courses/${courseId}/enroll`); toast.success('Enrolled!'); load() }
    catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  const handleCreate = async e => {
    e.preventDefault()
    try {
      await api.post('/courses', {...form, credits:+form.credits, maxSeats:+form.maxSeats})
      toast.success('Course created'); setModal(false); load()
    } catch (err) { toast.error(err.response?.data?.message || 'Error') }
  }

  return (
    <div>
      <div className="page-header" style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between' }}>
        <div><h1>Courses</h1><p>Browse and enroll in courses</p></div>
        {user?.role === 'ADMIN' && <button className="btn btn-primary" onClick={()=>setModal(true)}>+ New Course</button>}
      </div>

      <div className="grid-3">
        {courses.map(c => {
          const enrolled = enrolledIds.includes(c.id)
          return (
            <div key={c.id} className="card">
              <span className="badge badge-primary" style={{ marginBottom:8 }}>{c.code}</span>
              <h3 style={{ fontSize:15, fontWeight:600, marginBottom:4 }}>{c.name}</h3>
              <p style={{ fontSize:13, color:'#64748b', marginBottom:8 }}>{c.description}</p>
              <div style={{ fontSize:12, color:'#94a3b8', marginBottom:12 }}>
                {c.credits} credits · {c.department?.name} · {c.faculty?.name}
              </div>
              {user?.role === 'STUDENT' && (
                enrolled
                  ? <span className="badge badge-success">✓ Enrolled</span>
                  : <button className="btn btn-outline btn-sm" onClick={()=>handleEnroll(c.id)}>Enroll</button>
              )}
            </div>
          )
        })}
      </div>

      {modal && (
        <div className="modal-overlay" onClick={()=>setModal(false)}>
          <div className="modal" onClick={e=>e.stopPropagation()}>
            <h2>New Course</h2>
            <form onSubmit={handleCreate}>
              <div className="grid-2">
                <div className="form-group"><label>Name</label><input className="form-control" required value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/></div>
                <div className="form-group"><label>Code</label><input className="form-control" required value={form.code} onChange={e=>setForm({...form,code:e.target.value})}/></div>
              </div>
              <div className="form-group"><label>Description</label><input className="form-control" value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/></div>
              <div className="grid-2">
                <div className="form-group"><label>Credits</label><input className="form-control" type="number" value={form.credits} onChange={e=>setForm({...form,credits:e.target.value})}/></div>
                <div className="form-group"><label>Max Seats</label><input className="form-control" type="number" value={form.maxSeats} onChange={e=>setForm({...form,maxSeats:e.target.value})}/></div>
              </div>
              <div className="form-group"><label>Department</label>
                <select className="form-control" required value={form.departmentId} onChange={e=>setForm({...form,departmentId:e.target.value})}>
                  <option value="">Select dept</option>
                  {depts.map(d=><option key={d.id} value={d.id}>{d.name}</option>)}
                </select>
              </div>
              <div className="form-group"><label>Faculty</label>
                <select className="form-control" required value={form.facultyId} onChange={e=>setForm({...form,facultyId:e.target.value})}>
                  <option value="">Select faculty</option>
                  {faculty.map(f=><option key={f.id} value={f.id}>{f.name}</option>)}
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
