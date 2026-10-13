# Java Lab Experiments & Projects — Run Cheatsheet
> **One-Stop Copy-Paste Guide** for running every experiment and project in this repository.

---

## ⚡ Quick Reference: Run Any Experiment in 1 Copy-Paste

| Experiment | Name / Description | Quick Copy-Paste Command |
| :--- | :--- | :--- |
| **Exp 1** | Swing Student Registration Form | `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/01-swing-student-registration && ./run.sh` |
| **Exp 2** | Swing MVC Calculator | `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/02-swing-mvc-calculator && ./run.sh` |
| **Exp 3** | Apache Tomcat 11 WebApp | `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp && ./start-tomcat.sh` |
| **Exp 4** | TCP Client-Server Sockets | *Terminal 1:* `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/04-tcp-client-server && ./run-server.sh`<br>*Terminal 2:* `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/04-tcp-client-server && ./run-client.sh` |
| **Exp 5** | Servlet Basics (Tomcat 11) | `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/05-servlet-basics && ./run.sh` |
| **Exp 6** | HTML to JSP Conversion | `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/06-html-to-jsp && ./run.sh` |
| **Exp 7** | JDBC MySQL Database Practical | `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/07-jdbc-mysql && ./run.sh` |
| **Exp 8** | ShopSphere Full Web Store | `cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/08-ecommerce-store && ./run.sh` |

---

## 🛠️ Global Services (MySQL & Tomcat)

Before running database projects (**Exp 7** and **Exp 8**), ensure MySQL is running:

```bash
# Start MySQL via Homebrew
brew services start mysql

# Check MySQL status
brew services list
```

To manage the Apache Tomcat 11 web server manually:

```bash
# Start Tomcat
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp && ./start-tomcat.sh

# Stop Tomcat
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp && ./stop-tomcat.sh
```

---

## 📋 Detailed Step-by-Step for Each Experiment

### Experiment 1: Swing Student Registration Form
* **Topic:** Java Swing GUI, AWT Layouts, Event Delegation Model (`ActionListener`), Input Validation.
* **Output:** Opens native desktop window with registration form and reset/submit actions.

```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/01-swing-student-registration
./run.sh
```

*(Manual alternative without script:)*
```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/01-swing-student-registration
javac -d out src/StudentRegistration.java
java -cp out StudentRegistration
```

---

### Experiment 2: Swing MVC Calculator
* **Topic:** GUI Architecture, Model-View-Controller (MVC) Separation of Concerns.
* **Output:** Opens calculator GUI where Controller handles events and updates View via Model.

```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/02-swing-mvc-calculator
./run.sh
```

*(Manual alternative without script:)*
```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/02-swing-mvc-calculator
javac -d out src/*.java
java -cp out Main
```

---

### Experiment 3: Apache Tomcat 11 Basic Web Application
* **Topic:** Web Server Installation, Context Paths, Serving Web Artifacts (`MyWebApp`).
* **Output:** Deploys web application to Tomcat 11.

```bash
# 1. Start Tomcat
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp
./start-tomcat.sh
```

* **Access in Browser:**
  * [http://localhost:8080/MyWebApp/](http://localhost:8080/MyWebApp/)

* **To Stop Tomcat:**
```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp
./stop-tomcat.sh
```

---

### Experiment 4: TCP Client-Server Socket Communication
* **Topic:** `java.net.Socket`, `ServerSocket`, Full-Duplex Byte & Character Streams over TCP Port 5000.
* **Output:** Interactive terminal chat exchange between Server and Client.

**Step 1: Open Terminal 1 and start the Server:**
```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/04-tcp-client-server
./run-server.sh
```

**Step 2: Open Terminal 2 and connect the Client:**
```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/04-tcp-client-server
./run-client.sh
```
*Type messages in the Client terminal and press Enter to see them echoed/processed by the Server.*

---

### Experiment 5: Java Servlet Basics (Jakarta Servlet 6.1)
* **Topic:** Servlet Lifecycle, HTTP GET vs POST, Session Management (`HttpSession`), Filters (`LoggingFilter`), Listeners (`AppListener`, `SessionListener`).
* **Output:** Maven packages WAR and deploys to Tomcat 11 on port 8080.

```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/05-servlet-basics
./run.sh
```

* **Access in Browser:**
  * **Portal Homepage:** [http://localhost:8080/servlet-basics/](http://localhost:8080/servlet-basics/)
  * **Login / Session Demo:** [http://localhost:8080/servlet-basics/login.html](http://localhost:8080/servlet-basics/login.html)
  * **Student Registration:** [http://localhost:8080/servlet-basics/student.html](http://localhost:8080/servlet-basics/student.html)
  * **Test Script:** `./test-endpoints.sh` (validates all HTTP endpoints via curl)

---

### Experiment 6: HTML to JSP Conversion
* **Topic:** Converting static HTML forms to dynamic JSP with Expression Language (`${}`), MVC Controller (`HelloServlet`), Scriptlets vs Standard Action Tags.
* **Output:** Form submission passes data through Servlet controller and renders via JSP.

```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/06-html-to-jsp
./run.sh
```

* **Access in Browser:**
  * **Student Entry Form:** [http://localhost:8080/html-to-jsp/student.jsp](http://localhost:8080/html-to-jsp/student.jsp)
  * **Project Portal:** [http://localhost:8080/html-to-jsp/](http://localhost:8080/html-to-jsp/)
  * **Test Script:** `./test-endpoints.sh`

---

### Experiment 7: JDBC with MySQL Practical
* **Topic:** Type-4 MySQL Driver (`com.mysql.cj.jdbc.Driver`), `DriverManager`, `Connection`, `PreparedStatement`, `Statement`, `ResultSet`, SQL Injection Prevention.
* **Output:** Runs the professor's step-by-step JDBC insertion and query demonstration.

```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/07-jdbc-mysql
./run.sh
```

*(Interactive CRUD Menu Alternative:)*
```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/07-jdbc-mysql
java -cp "out:lib/*" Main
```

---

### Experiment 8: ShopSphere Enterprise E-Commerce Store (Capstone)
* **Stack:** Jakarta Servlet 6.1, JSP 3.1, JSTL 3.0, Bootstrap 5, MySQL 8, TCP Sockets, RMI, Desktop Swing.

#### 1. Web Application (Tomcat 11)
```bash
cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/08-ecommerce-store
./run.sh
```

* **Access in Browser:**
  * **Online Storefront:** [http://localhost:8080/shopsphere/](http://localhost:8080/shopsphere/)
  * **Sign In:** [http://localhost:8080/shopsphere/login](http://localhost:8080/shopsphere/login)

* **Demo Accounts:**
  * **Customer:** `student@shopsphere.com` / `customer123`
  * **Admin:** `admin@shopsphere.com` / `admin123`

* **Syllabus Demonstration Pages:**
  * **JSP Elements & Directives:** [http://localhost:8080/shopsphere/demo-elements.jsp](http://localhost:8080/shopsphere/demo-elements.jsp)
  * **I18N (Hindi / English):** [http://localhost:8080/shopsphere/demo-i18n.jsp](http://localhost:8080/shopsphere/demo-i18n.jsp)
  * **URL / URLConnection:** [http://localhost:8080/shopsphere/demo-url-protocol.jsp](http://localhost:8080/shopsphere/demo-url-protocol.jsp)

---

#### 2. Advanced Syllabus Modules in ShopSphere

* **A. TCP Socket Inventory Server & Client (Port 6060):**
  ```bash
  cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/08-ecommerce-store
  ./run-network.sh
  ```
  *(Tests inventory queries, live MySQL loading, and serialized object transmission.)*

* **B. Distributed Java RMI Remote Service (Port 1099):**
  ```bash
  cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/08-ecommerce-store
  ./run-rmi.sh
  ```
  *(Tests RMI registry bootstrap, remote stub lookup, and RPC stock updates.)*

* **C. Swing Desktop Admin GUI (Native Window):**
  ```bash
  cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/08-ecommerce-store
  ./run-swing-admin.sh
  ```
  *(Opens the Swing Desktop application connected live to the MySQL database.)*

* **D. Object Serialization & JNDI Architecture Demos:**
  ```bash
  cd ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/08-ecommerce-store
  java -cp target/classes com.shopsphere.util.SerializationDemo
  java -cp target/classes com.shopsphere.util.JndiDemo
  ```

---

## 🔑 Default Credentials Cheat-Sheet

| Resource | Username / Email | Password | Port / URL |
| :--- | :--- | :--- | :--- |
| **MySQL (Project User)** | `shopsphere_user` | `ShopSphere@2027!` | `localhost:3306` (Database: `shopsphere`) |
| **Tomcat Server** | — | — | `http://localhost:8080/` |
| **ShopSphere Customer** | `student@shopsphere.com` | `customer123` | [http://localhost:8080/shopsphere/login](http://localhost:8080/shopsphere/login) |
| **ShopSphere Admin** | `admin@shopsphere.com` | `admin123` | [http://localhost:8080/shopsphere/login](http://localhost:8080/shopsphere/login) |
| **TCP Socket Module** | — | — | Port `6060` |
| **Java RMI Module** | — | — | Port `1099` |

---

## 💡 Troubleshooting Tips

1. **Port 8080 in use?**
   ```bash
   lsof -i :8080
   # To stop Tomcat cleanly:
   ~/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/03-tomcat-basic-webapp/stop-tomcat.sh
   ```

2. **Java Version Check:**
   ```bash
   export JAVA_HOME="/opt/homebrew/opt/openjdk"
   export PATH="$JAVA_HOME/bin:$PATH"
   java -version
   ```

3. **Re-deploy WAR to Tomcat:**
   Every script (`./run.sh` in 05, 06, 08) automatically compiles with Maven, packages the WAR, copies it to Tomcat's `webapps/`, and restarts/verifies Tomcat.
