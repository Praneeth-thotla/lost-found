# Lost & Found Portal — Java Full Stack

Based on the uploaded project brief.

**Stack:** Java 21, Spring Boot 3, Spring Security + JWT, Spring Data JPA, TiDB (MySQL-compatible), React + Vite, Vercel frontend, Render backend.

## Features
- Register/login using JWT
- Create, browse, search, filter, edit and delete lost/found reports
- Status tracking: LOST, FOUND, CLAIMED, RESOLVED
- Users can edit/delete only their own reports
- Contact details for the report poster

## TiDB setup
Create a TiDB Cloud cluster and a schema/database named `lost_found`. Get the JDBC host, username, password and TLS settings from the TiDB connection panel. TiDB is MySQL-compatible, so this project uses MySQL Connector/J.

## Run locally
Backend requires Java 21 and Maven:
```bash
cd backend
export DB_URL='jdbc:mysql://YOUR_TIDB_HOST:4000/lost_found?sslMode=VERIFY_IDENTITY'
export DB_USERNAME='YOUR_TIDB_USERNAME'
export DB_PASSWORD='YOUR_TIDB_PASSWORD'
export JWT_SECRET='use-a-random-secret-at-least-32-characters-long'
export CORS_ALLOWED_ORIGINS='http://localhost:5173'
mvn spring-boot:run
```
Use the exact JDBC/TLS connection values supplied by TiDB. API: `http://localhost:8080/api`; health: `/api/health`.

Frontend requires Node.js 20+:
```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```
Set `VITE_API_BASE_URL=http://localhost:8080/api`.

## Deploy backend to Render
1. Push the project to GitHub.
2. Create a Render Web Service from the repository, set Root Directory to `backend`.
3. Build command: `mvn -DskipTests package`
4. Start command: `java -Dserver.port=$PORT -jar target/lost-found-portal-0.0.1-SNAPSHOT.jar`
5. Add `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, and `CORS_ALLOWED_ORIGINS` in Render environment settings. Set `CORS_ALLOWED_ORIGINS` to `https://lost-found-two-tau.vercel.app` (add any other permitted frontend origins as comma-separated values).
6. Deploy and check `/api/health`.

## Deploy frontend to Vercel
1. Import GitHub repository.
2. Set Root Directory to `frontend`, framework preset to Vite.
3. Add `VITE_API_BASE_URL=https://YOUR-RENDER-SERVICE.onrender.com/api` (do not use `localhost` for the deployed frontend).
4. Deploy.

## REST API
- `POST /api/auth/register` — `{ "name": "...", "email": "...", "password": "..." }`
- `POST /api/auth/login` — `{ "email": "...", "password": "..." }`
- `GET /api/items?q=&type=&category=&status=`
- `GET /api/items/{id}`
- `POST /api/items` (JWT required)
- `PUT /api/items/{id}` (owner only)
- `DELETE /api/items/{id}` (owner only)
- `GET /api/health`

Send protected requests with `Authorization: Bearer <token>`.

## Important
Set all credentials as deployment environment variables, never commit them. `ddl-auto=update` is convenient for a college project; use migrations for production. This starter uses browser localStorage for JWT simplicity; for production, consider HttpOnly secure cookies, rate limiting, email verification and password reset.
