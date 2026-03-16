import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useState } from 'react'

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: '⊞', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/departments', label: 'Departments', icon: '🏛', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/courses', label: 'Courses', icon: '📚', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/attendance', label: 'Attendance', icon: '✅', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/assignments', label: 'Assignments', icon: '📝', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/notices', label: 'Notice Board', icon: '📢', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/elections', label: 'Elections', icon: '🗳', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/calendar', label: 'Calendar', icon: '📅', roles: ['ADMIN','FACULTY','STUDENT'] },
  { to: '/admin', label: 'Admin Panel', icon: '⚙️', roles: ['ADMIN'] },
]

export default function Layout() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const [collapsed, setCollapsed] = useState(false)

  const handleLogout = () => { logout(); navigate('/login') }

  const filtered = navItems.filter(n => n.roles.includes(user?.role))

  return (
    <div style={{ display: 'flex', minHeight: '100vh', background: 'var(--gray-50)' }}>
      {/* Sidebar */}
      <aside style={{
        width: collapsed ? 60 : 240, background: '#1e1b4b', color: 'white',
        display: 'flex', flexDirection: 'column', flexShrink: 0,
        transition: 'width .2s', overflow: 'hidden'
      }}>
        {/* Logo */}
        <div style={{ padding: '20px 16px', borderBottom: '1px solid rgba(255,255,255,.1)', display:'flex', alignItems:'center', gap: 10 }}>
          <div style={{ width: 32, height: 32, background: '#4f46e5', borderRadius: 8, display:'flex', alignItems:'center', justifyContent:'center', fontWeight: 700, fontSize: 14, flexShrink: 0 }}>CC</div>
          {!collapsed && <div>
            <div style={{ fontSize: 13, fontWeight: 700, lineHeight: 1.2 }}>CollabCampus</div>
            <div style={{ fontSize: 10, color: 'rgba(255,255,255,.5)', marginTop: 2 }}>KNRU</div>
          </div>}
          <button onClick={() => setCollapsed(!collapsed)} style={{ marginLeft: 'auto', background: 'none', border: 'none', color: 'rgba(255,255,255,.5)', cursor: 'pointer', fontSize: 16, flexShrink: 0 }}>
            {collapsed ? '▶' : '◀'}
          </button>
        </div>

        {/* Nav */}
        <nav style={{ flex: 1, padding: '12px 0', overflowY: 'auto' }}>
          {filtered.map(item => (
            <NavLink key={item.to} to={item.to} style={({ isActive }) => ({
              display: 'flex', alignItems: 'center', gap: 10,
              padding: '10px 16px', textDecoration: 'none', fontSize: 13,
              color: isActive ? 'white' : 'rgba(255,255,255,.6)',
              background: isActive ? 'rgba(79,70,229,.4)' : 'none',
              borderLeft: isActive ? '3px solid #818cf8' : '3px solid transparent',
              transition: 'all .15s', whiteSpace: 'nowrap', overflow: 'hidden'
            })}>
              <span style={{ fontSize: 16, flexShrink: 0 }}>{item.icon}</span>
              {!collapsed && item.label}
            </NavLink>
          ))}
        </nav>

        {/* User */}
        <div style={{ padding: '16px', borderTop: '1px solid rgba(255,255,255,.1)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <div style={{ width: 32, height: 32, borderRadius: '50%', background: '#4f46e5', display: 'flex', alignItems:'center', justifyContent:'center', fontWeight: 600, fontSize: 13, flexShrink: 0 }}>
              {user?.name?.[0]?.toUpperCase()}
            </div>
            {!collapsed && <div style={{ overflow: 'hidden' }}>
              <div style={{ fontSize: 12, fontWeight: 600, color: 'white', overflow:'hidden', textOverflow:'ellipsis', whiteSpace:'nowrap' }}>{user?.name}</div>
              <div style={{ fontSize: 10, color: 'rgba(255,255,255,.4)' }}>{user?.role}</div>
            </div>}
          </div>
          {!collapsed && <button onClick={handleLogout} style={{ marginTop: 10, width: '100%', padding: '6px', background: 'rgba(239,68,68,.15)', border: '1px solid rgba(239,68,68,.3)', color: '#fca5a5', borderRadius: 6, cursor:'pointer', fontSize: 12 }}>
            Sign out
          </button>}
        </div>
      </aside>

      {/* Main */}
      <main style={{ flex: 1, overflow: 'auto' }}>
        <div style={{ padding: '28px 32px', maxWidth: 1200 }}>
          <Outlet />
        </div>
      </main>
    </div>
  )
}
