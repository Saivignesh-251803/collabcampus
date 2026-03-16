# CollabCampus — KNR UNIVERSITY (KNRU)

A complete Virtual University Platform built with Spring Boot + React + MySQL.

---

## Admin Login
- **Email:** saivigneshgunjapadugu@gmail.com
- **Password:** jeevan@251803
- **Role:** ADMIN (full access to all modules)

---

## Run Locally (Step by Step)

### Prerequisites (all must be installed)
- Java 21, Maven 3.9+, Node.js 20+, MySQL 8, Git, VS Code

### Step 1 — Create MySQL Database
Open PowerShell and run:
```
mysql -u root -pSystem -e "CREATE DATABASE IF NOT EXISTS collabcampus_db;"
```

### Step 2 — Start Backend
```
cd collabcampus/backend
mvn spring-boot:run
```
Wait for: `Started CollabCampusApplication`
Backend runs at: http://localhost:8080

### Step 3 — Start Frontend (new PowerShell window)
```
cd collabcampus/frontend
npm install
npm run dev
```
Frontend runs at: http://localhost:5173

### Step 4 — Open in Browser
Go to: http://localhost:5173
Login with admin credentials above.

---

## Deploy to Internet (Free — Permanent Public URL)

### 1. Push to GitHub
```
cd collabcampus
git init
git add .
git commit -m "initial commit"
git remote add origin https://github.com/YOUR_USERNAME/collabcampus.git
git push -u origin main
```

### 2. Deploy Frontend — Vercel
1. Go to vercel.com → New Project → Import your GitHub repo
2. Set Root Directory: `frontend`
3. Add Environment Variable: `VITE_API_URL` = your Render backend URL
4. Click Deploy → get URL like collabcampus.vercel.app

### 3. Deploy Backend — Render
1. Go to render.com → New Web Service → Connect GitHub repo
2. Root Directory: `backend`
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/collabcampus-backend-1.0.0.jar`
5. Add Environment Variables:
   - `SPRING_DATASOURCE_URL` = your PlanetScale connection string
   - `SPRING_DATASOURCE_USERNAME` = your PlanetScale username
   - `SPRING_DATASOURCE_PASSWORD` = your PlanetScale password
   - `JWT_SECRET` = KNRUCollabCampusSecretKey2024SuperLongSecretForJWTSigning1234567890

### 4. Cloud Database — PlanetScale
1. Go to planetscale.com → Create Database → collabcampus-db
2. Get connection string → paste into Render environment variables

---

## Access Levels

| Role    | Can Do |
|---------|--------|
| ADMIN   | Everything — create users, departments, elections, calendar, view all stats |
| FACULTY | Manage own courses, start QR attendance, post/grade assignments, post notices |
| STUDENT | Enroll in courses, mark attendance, submit assignments, vote in elections |
| Public  | View notice board, calendar, election results (no login needed) |

---

## Features
1. Departments & Courses
2. QR-Based Attendance
3. Assignments with Grade Appeals
4. Notice Board with Priority Levels
5. Student Council Elections with Live Results
6. Academic Calendar
7. Real-time Chat (WebSocket)
8. Admin Dashboard with Stats

---

## Tech Stack
- Backend: Spring Boot 3.2, Java 17, Maven, Spring Security, JWT
- Frontend: React 18, Vite, React Router, Axios
- Database: MySQL 8 (22 tables, auto-created on first run)
- Real-time: WebSocket STOMP
