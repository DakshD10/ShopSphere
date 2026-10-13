# Experiment 5 — Java Servlet Basics (Jakarta Servlet / Tomcat 11)

This project implements the complete syllabus requirements from the professor's PDF **`Servlet (1).pdf`**. It is built using **Jakarta Servlet API 6.1.0** targeting **Apache Tomcat 11**.

---

## 1. Syllabus & Practical Tasks Audit

| Task # | Syllabus Practical Task | Implementation Class | Mapping / URL | Status |
|---|---|---|---|:---:|
| 1 | Create `HelloServlet` using `doGet()` | `com.servlet.HelloServlet` | `/hello` | **Implemented** |
| 2 | Create a Student Form using `POST` | `student-form.html` & `com.servlet.StudentServlet` | `/student-form.html`, `/student` | **Implemented** |
| 3 | Display name, email, and course using `request.getParameter()` | `com.servlet.StudentServlet` | `/student` (POST) | **Implemented** |
| 4 | Store username in `HttpSession` | `com.servlet.LoginServlet`, `com.servlet.StudentServlet` | `/login`, `/student` | **Implemented** |
| 5 | Create logout using `session.invalidate()` | `com.servlet.LogoutServlet` | `/logout` | **Implemented** |
| 6 | Create a `ServletContextListener` for application start/stop | `com.servlet.AppListener` | `@WebListener` | **Implemented** |
| 7 | Create a Filter that logs request URIs | `com.servlet.MyFilter` | `@WebFilter("/*")` | **Implemented** |
| 8 | Create an authentication Filter for a protected Servlet | `com.servlet.AuthFilter` guarding `com.servlet.HomeServlet` | `@WebFilter("/home")` | **Implemented** |
| Demo A | Servlet Life Cycle (`init`, `service`, `doGet`, `destroy`) | `com.servlet.LifeCycleServlet` | `/lifecycle` | **Implemented** |
| Demo B | `ServletConfig` vs `ServletContext` | `com.servlet.ConfigContextServlet` | `/config-context` | **Implemented** |
| Demo C | `Cookie` vs `HttpSession` | `com.servlet.CookieServlet` | `/cookies` | **Implemented** |
| Demo D | `HttpSessionListener` (sessionCreated / sessionDestroyed) | `com.servlet.SessionListener` | `@WebListener` | **Implemented** |

**Tasks remaining un-implemented:** **0** (All practical tasks from PDF Section 38 and architectural demonstrations from Sections 12–33 are fully implemented).

---

## 2. Project Architecture

```
05-servlet-basics/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── servlet/
        │           ├── AppListener.java          # ServletContextListener (start/stop)
        │           ├── AuthFilter.java           # Authentication Filter protecting /home
        │           ├── ConfigContextServlet.java # ServletConfig vs ServletContext demo
        │           ├── CookieServlet.java        # Cookie creation, reading & session comparison
        │           ├── HelloServlet.java         # doGet & doPost handling (?name=...)
        │           ├── HomeServlet.java          # Protected resource requiring active session
        │           ├── LifeCycleServlet.java     # Servlet lifecycle (init, service, doGet, destroy)
        │           ├── LoginServlet.java         # Validates credentials (admin/1234) & sets session
        │           ├── LogoutServlet.java        # Calls session.invalidate()
        │           ├── MyFilter.java             # WebFilter("/*") logging request URIs
        │           ├── SessionListener.java      # HttpSessionListener (create/destroy events)
        │           └── StudentServlet.java       # Handles POST: name, email, course
        └── webapp/
            ├── WEB-INF/
            │   └── web.xml                       # Deployment descriptor with context-param
            ├── index.html                        # Main interactive lab portal
            ├── login.html                        # Student login form (PDF Section 33.1)
            └── student-form.html                 # Student registration form (POST)
```

---

## 3. How to Build & Deploy

### A. Build with Maven
Inside `05-servlet-basics`:
```bash
mvn clean package
```
Generates `target/servlet-basics.war`.

### B. Deploy to Apache Tomcat 11
Copy the generated WAR to Tomcat's `webapps/` folder:
```bash
cp target/servlet-basics.war ../03-tomcat-basic-webapp/apache-tomcat-11.0.25/webapps/servlet-basics.war
cp target/servlet-basics.war ../03-tomcat-basic-webapp/apache-tomcat-11.0.25/webapps/servlet.war
```
*(Both `/servlet-basics/` and `/servlet/` are deployed so that both the workspace conventions and the professor PDF Section 35 conventions are supported).*

### C. Start Tomcat 11
```bash
../03-tomcat-basic-webapp/apache-tomcat-11.0.25/bin/catalina.sh run
```

---

## 4. Endpoints & How to Test

### 1. Lab Portal
- **URL:** `http://localhost:8080/servlet-basics/` (or `http://localhost:8080/servlet/`)
- Interactive dashboard linking every practical task, form, and demonstration.

### 2. `HelloServlet` (GET / POST)
- **URL (GET):** `http://localhost:8080/servlet-basics/hello`
  - Returns `<h1>Hello Students!</h1><h2>Welcome to Servlet Programming</h2>`.
- **URL (GET with parameter):** `http://localhost:8080/servlet-basics/hello?name=Jahanvi`
  - Test via curl: `curl -s "http://localhost:8080/servlet-basics/hello?name=Jahanvi"`
- **URL (POST):**
  - Test via curl: `curl -s -X POST -d "name=Jahanvi" http://localhost:8080/servlet-basics/hello`

### 3. Student Form (POST: name, email, course)
- **Form URL:** `http://localhost:8080/servlet-basics/student-form.html`
- **POST Action:** `http://localhost:8080/servlet-basics/student`
- **Test via curl:**
  ```bash
  curl -s -X POST \
    -d "name=Daksh Duhlani&email=daksh@example.com&course=B.Tech CSE" \
    http://localhost:8080/servlet-basics/student
  ```
  Extracts `request.getParameter("name")`, `request.getParameter("email")`, and `request.getParameter("course")`, prints an HTML table, and sets `session.setAttribute("username", name)`.

### 4. Authentication Filter & Login/Session Flow (PDF Section 33)
- **Step A: Test Unauthorized Access (Blocked by AuthFilter):**
  ```bash
  curl -s -I http://localhost:8080/servlet-basics/home
  # Returns HTTP 302 Redirect to /servlet-basics/login.html
  ```
- **Step B: Submit Login Credentials:**
  ```bash
  curl -s -c cookies.txt -X POST \
    -d "username=admin&password=1234" \
    http://localhost:8080/servlet-basics/login
  # Returns "Login Successful" and sets session.setAttribute("username", "admin")
  ```
- **Step C: Access Protected Resource with Session:**
  ```bash
  curl -s -b cookies.txt http://localhost:8080/servlet-basics/home
  # Returns "Welcome admin" (AuthFilter calls chain.doFilter())
  ```
- **Step D: Invalidate Session (Logout):**
  ```bash
  curl -s -b cookies.txt http://localhost:8080/servlet-basics/logout
  # Calls session.invalidate()
  ```
- **Step E: Verify Protected Resource is Blocked Again:**
  ```bash
  curl -s -I -b cookies.txt http://localhost:8080/servlet-basics/home
  # Returns HTTP 302 Redirect to login.html
  ```

### 5. Request Logging Filter (`MyFilter`)
- Mapped to `/*`.
- Automatically intercepts all requests and prints to standard output / Tomcat log:
  ```
  [MyFilter] Request intercepted -> URI: /servlet-basics/hello | Method: GET
  [MyFilter] Response completed -> URI: /servlet-basics/hello
  ```

### 6. Event Listeners
- **`AppListener`** (`ServletContextListener`): Logs `"Application Started"` when the context initializes and `"Application Stopped"` on context destruction.
- **`SessionListener`** (`HttpSessionListener`): Logs `"Session Created"` with session ID when a new session is initialized, and `"Session Destroyed"` when `session.invalidate()` is called.

### 7. Concept Demonstrations
- **Servlet Life Cycle:** `http://localhost:8080/servlet-basics/lifecycle`
  - Demonstrates `init()`, `service()`, `doGet()`, `destroy()`, and shows request hit counts.
- **ServletConfig vs ServletContext:** `http://localhost:8080/servlet-basics/config-context`
  - Compares servlet-scoped init-params vs application-scoped context-params.
- **Cookies vs HttpSession:** `http://localhost:8080/servlet-basics/cookies`
  - Demonstrates client-side `Cookie` addition (`action=set`) and reading (`request.getCookies()`) alongside server-side session comparison.
