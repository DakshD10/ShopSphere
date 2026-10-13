# Experiment 3 — Introduction to Apache Tomcat & Basic Web Application

## Aim
To install and configure Apache Tomcat, and create, deploy, and run a simple web application using an HTML page.

---

## Directory Structure

```text
03-tomcat-basic-webapp/
├── apache-tomcat-11.0.25/  <-- Self-contained Apache Tomcat distribution
│   ├── bin/                <-- startup.sh, shutdown.sh, etc.
│   ├── conf/               <-- server.xml and configuration files
│   ├── webapps/
│   │   └── MyWebApp/       <-- Deployed application folder
│   │       └── index.html  <-- Welcome page served by Tomcat
│   └── logs/               <-- catalina.out and server logs
├── MyWebApp/               <-- Original project source folder
│   └── index.html
├── index.html              <-- Top-level copy of the welcome page
├── start-tomcat.sh         <-- 1-click startup script (handles JAVA_HOME automatically)
├── stop-tomcat.sh          <-- 1-click shutdown script
└── README.md               <-- Complete lab documentation & viva guide
```

---

## HTML Source (`index.html`)

```html
<!DOCTYPE html>
<html>
<head>
    <title>My Web Application</title>
</head>
<body>
    <h1>Welcome to My Web Application</h1>
    <p>This application is running on Apache Tomcat.</p>
</body>
</html>
```

---

## Quick Start (Direct Execution)

Apache Tomcat 11.0.25 is pre-installed in this directory with `MyWebApp` deployed.

### 1. Start Tomcat
Run the included helper script:
```bash
./start-tomcat.sh
```
*(Or directly: `./apache-tomcat-11.0.25/bin/startup.sh`)*

### 2. View in Browser
Open your browser and visit:
```text
http://localhost:8080/MyWebApp/
```

### 3. Stop Tomcat
When finished, stop the server with:
```bash
./stop-tomcat.sh
```
*(Or directly: `./apache-tomcat-11.0.25/bin/shutdown.sh`)*

---

## Key Networking & Web Concepts

1. **`localhost`**:
   - Refers to the loopback network address (`127.0.0.1`) representing the local computer itself on which the web server is currently running.
2. **Port `8080`**:
   - A numerical identifier assigned to a specific network service. In this setup, Tomcat listens for incoming HTTP requests on port `8080` by default. *(Note: The configured port may differ on a user's machine if modified in `conf/server.xml` or if port 8080 is already occupied).*
3. **Context Path (`/MyWebApp/`)**:
   - The path component of the URL that identifies a specific web application hosted on the server. The directory name inside `webapps/` directly corresponds to the context path in the URL.
4. **URL Structure**:
   ```text
   http://localhost:8080/MyWebApp/
   │      │         │    │
   │      │         │    └── Application context path (mapped to webapps/MyWebApp)
   │      │         └─────── Port number Tomcat is listening on
   │      └───────────────── Server address (local machine)
   └──────────────────────── Protocol (HyperText Transfer Protocol)
   ```
5. **Request-Response Flow**:
   - The user opens `http://localhost:8080/MyWebApp/` in a web browser.
   - The browser sends an HTTP GET request to port `8080` on `localhost`.
   - Tomcat receives the request, identifies the context path `/MyWebApp/`, and locates the folder `webapps/MyWebApp/`.
   - Tomcat detects `index.html` as the default welcome page and returns an HTTP response containing the HTML content.
   - The web browser renders the HTML page.

---

## Deployment & Execution Steps (Generic Setup)

If configuring an external Apache Tomcat installation from scratch:

### 1. Prerequisites
- Java Development Kit (JDK) installed with `JAVA_HOME` environment variable configured.
- Apache Tomcat (version 11.0.25) downloaded and extracted.

### 2. Deploying the Web Application
Copy the `MyWebApp` directory into Tomcat's `webapps` folder:

```bash
# macOS / Linux:
cp -r MyWebApp/ $CATALINA_HOME/webapps/

# Windows:
xcopy /E /I MyWebApp "%CATALINA_HOME%\webapps\MyWebApp"
```

The resulting path inside Tomcat should be:
```text
apache-tomcat-11.0.25/webapps/MyWebApp/index.html
```

### 3. Starting Apache Tomcat
Navigate to Tomcat's `bin` directory:

- **macOS / Linux**:
  ```bash
  cd path/to/apache-tomcat/bin
  ./startup.sh
  ```
- **Windows (PowerShell / Command Prompt)**:
  ```powershell
  cd path\to\apache-tomcat\bin
  .\startup.bat
  ```

### 4. Viewing the Application in a Browser
Open any web browser and visit:
```text
http://localhost:8080/MyWebApp/
```

### 5. Stopping Apache Tomcat
- **macOS / Linux**:
  ```bash
  ./shutdown.sh
  ```
- **Windows**:
  ```powershell
  .\shutdown.bat
  ```

---

## Expected Output in Browser

- **Browser Tab Title**: `My Web Application`
- **Page Heading**: `Welcome to My Web Application`
- **Body Paragraph**: `This application is running on Apache Tomcat.`

---

## PDF Requirement Mapping Checklist

- [x] **Context folder named `MyWebApp`**: Created at [03-tomcat-basic-webapp/MyWebApp](file:///Users/dakshduhlani/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp/MyWebApp).
- [x] **`index.html` inside `MyWebApp`**: Created at [03-tomcat-basic-webapp/MyWebApp/index.html](file:///Users/dakshduhlani/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp/MyWebApp/index.html).
- [x] **Page title**: `<title>My Web Application</title>`.
- [x] **Page heading**: `<h1>Welcome to My Web Application</h1>`.
- [x] **Paragraph**: `<p>This application is running on Apache Tomcat.</p>`.
- [x] **Directly runnable Tomcat setup**: Extracted `apache-tomcat-11.0.25` with `MyWebApp` deployed into `webapps/`.
- [x] **Documentation of Tomcat startup and URL**: Documented with commands for macOS/Linux and Windows.
- [x] **Explanation of `localhost`, port `8080`, and context path**: Clearly documented in networking section.
- [x] **No unnecessary frameworks**: Pure HTML; no Java classes, servlets, JSP, or complex CSS.

---

## Viva Questions & Answers

1. **What is Apache Tomcat?**
   Apache Tomcat is an open-source web server and Servlet container developed by the Apache Software Foundation used to execute Java web applications (Servlets and JSPs) and serve static web resources (HTML, CSS, JS).

2. **Which folder contains deployed web applications in Tomcat?**
   The `webapps/` folder located in the Tomcat root directory.

3. **What is `index.html`?**
   It is a standard HTML file configured as the default/welcome page of a web application. When a user requests the root of a context path without specifying a file name, Tomcat automatically looks for and serves `index.html`.

4. **What is `localhost`?**
   `localhost` is a domain name that points to the local machine's loopback IP address (`127.0.0.1`), allowing client applications (such as a web browser) to communicate with server applications running on the same computer.

5. **What port does Tomcat use by default?**
   Port `8080` is the default HTTP connector port configured in Tomcat's `conf/server.xml`.

6. **What does `MyWebApp` represent in the URL `http://localhost:8080/MyWebApp/`?**
   It represents the application's **context path**, which maps directly to the `MyWebApp` folder deployed inside Tomcat's `webapps/` directory.

7. **Can Tomcat run multiple web applications simultaneously?**
   Yes. Multiple folders (e.g., `MyWebApp`, `StudentApp`, `CollegeApp`) can reside inside `webapps/`, each assigned its own independent context path and resources.

8. **What is a Web Container?**
   A web container (also called a Servlet container) is the component of a web server that manages the lifecycle of Servlets, maps URLs to Servlets, and handles HTTP requests and responses.

9. **What is the difference between 2-Tier and 3-Tier architecture?**
   - **2-Tier**: Client directly connects to the database (Client $\rightarrow$ Database).
   - **3-Tier**: Separates the application into Presentation Tier (Browser/GUI), Business/Application Tier (Tomcat server & business logic), and Data Tier (Database).

10. **What causes a 404 Not Found error in Tomcat?**
    A 404 error occurs when Tomcat cannot find the requested resource—commonly because the folder name does not match the URL context path, or `index.html` is missing from the directory.
