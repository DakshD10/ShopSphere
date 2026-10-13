# Java Lab Projects

Each experiment is isolated in its own folder. Java desktop experiments use plain `javac`; web projects use Maven WAR packaging and Jakarta Servlet APIs for Tomcat 11. The `jdbc-mysql` and `ecommerce-store` folders need a local MySQL server.

## Folder map
1. `01-swing-student-registration` — Swing form with validation/reset.
2. `02-swing-mvc-calculator` — calculator separated into Model/View/Controller.
3. `03-tomcat-basic-webapp` — static HTML deployed as a Tomcat context.
4. `04-tcp-client-server` — TCP socket server/client on port 5000.
5. `05-servlet-basics` — GET/POST, session login/logout, request logging filter and app listener.
6. `06-html-to-jsp` — student form → Servlet controller → JSP result using EL.
7. `07-jdbc-mysql` — JDBC CRUD console application.
8. `08-ecommerce-store` — small JSP/Servlet/MySQL shop with product listing, session cart, checkout/order records.

## Prerequisites
- JDK 17 or newer (`java -version`, `javac -version`)
- Maven (`mvn -version`) for web projects
- Apache Tomcat 11 for web deployment
- MySQL 8.x for projects 7 and 8

## Run desktop / socket experiments
Open a terminal in the respective folder:
- Swing: `javac -d out src/*.java` then `java -cp out StudentRegistration` or `java -cp out Main`
- TCP: `javac -d out src/*.java`; terminal 1: `java -cp out Server`; terminal 2: `java -cp out Client`

## Run a web project
From a web project folder: `mvn clean package`, then copy `target/<artifact>.war` into `$CATALINA_HOME/webapps/`. Start Tomcat and open `http://localhost:8080/<artifact>/`.
Use the README inside each project for the exact URL and setup.

## Important
- Tomcat 11 uses `jakarta.servlet.*`, not the older `javax.servlet.*`.
- Never commit real DB passwords. The sample projects use environment variables with local-development defaults; change them for your machine.
- A GitHub repository stores source code; it does not itself run Tomcat/MySQL. Localhost is your machine. Hosting a Java WAR application publicly requires a Java-compatible host and a separately configured database.
- This is a prepared starter repository; run `mvn clean package` and test locally before claiming the projects are verified.


## Academic Syllabus Alignment Note
Every practical in this repository has been aligned with the prescribed RTU Advanced Java lab syllabus and professor course materials. Preserve prescribed class names, folder names, methods, expected outputs and request flows. In particular, the Swing registration practical specifies Male/Female only; the Tomcat practical demonstrates `MyWebApp/index.html`; the TCP practical demonstrates a client/server stream exchange; the HTML-to-JSP experiment uses `student.jsp` → `/hello` (`HelloServlet`) → `result.jsp`; Experiment 7 demonstrates the step-by-step JDBC CRUD lifecycle matching the lab manual; and Experiment 8 implements the complete ShopSphere capstone system.
