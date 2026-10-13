# Experiment 7: Java Database Connectivity (JDBC) with MySQL

> **Implementation Note & Academic Context:**  
> This project is a **proposed beginner-level implementation** fulfilling standard university lab syllabus requirements for JDBC CRUD operations using `PreparedStatement`. It also includes direct alignment with the professor's reference guide **`JDBC ( Java Database Connectivity ).pdf`**.

---

## 1. Aim
To establish a database connection between a Java console application and a MySQL relational database using JDBC (Java Database Connectivity), and perform full **CRUD** (Create, Read, Update, Delete) operations using parameterized `PreparedStatement` queries.

---

## 2. Theoretical Concepts Explained

### A. What is a JDBC Driver?
Java applications cannot directly understand database-specific network protocols (e.g., MySQL binary client/server protocol). The **JDBC Driver** (`com.mysql.cj.jdbc.Driver`) is a software component (JAR library) that translates standard JDBC API calls (`java.sql.*`) into the native protocol understood by the MySQL database server.
* Modern MySQL uses a **Type-4 pure Java driver** (`mysql-connector-j.jar`).
* It is registered into JVM memory using `Class.forName("com.mysql.cj.jdbc.Driver")` or automatically loaded via the Java `ServiceLoader` SPI mechanism.

```
+--------------------+
|    Java Program    | (Main.java / DB.java)
+--------------------+
          │ Calls java.sql.* APIs
          ▼
+--------------------+
|   DriverManager    |
+--------------------+
          │ Delegates to registered driver
          ▼
+--------------------+
|  MySQL Connector/J | (com.mysql.cj.jdbc.Driver)
+--------------------+
          │ TCP/IP Socket (Port 3306)
          ▼
+--------------------+
|    MySQL Server    | (Database engine)
+--------------------+
```

### B. The `Connection` Interface
A `Connection` (`java.sql.Connection`) represents an open physical communication session with a specific database. It is obtained through:
```java
Connection con = DriverManager.getConnection(url, user, password);
```
All statements, transactions, and metadata inquiries are created through this connection object.

### C. `Statement` vs. `PreparedStatement`
| Feature | `Statement` | `PreparedStatement` |
| :--- | :--- | :--- |
| **Compilation** | Compiled every time by MySQL server. | Precompiled once by the database engine. |
| **Performance** | Slower for repetitive queries. | Faster when executed repeatedly with different parameters. |
| **Parameters** | String concatenation (`"WHERE id=" + id`). | Parameter placeholders (`?`) bound via typed setters. |
| **SQL Injection** | Vulnerable to SQL injection attacks. | **Immune** to SQL injection because inputs are treated strictly as data, never executable code. |
| **Best Used For** | Static DDL/SELECT with no parameters. | All queries accepting user input (CRUD). |

### D. The `ResultSet` Interface
A `ResultSet` represents the tabular data returned by executing a `SELECT` query (`executeQuery()`).
* It maintains a **cursor** pointing before the first row initially.
* Calling `rs.next()` advances the cursor to the next row (returns `true` if a row exists, `false` when no rows remain).
* Column data is extracted using getters such as `rs.getInt("id")`, `rs.getString("name")`, `rs.getString("email")`, etc.

### E. CRUD Operations Lifecycle
1. **Create (INSERT):**
   ```sql
   INSERT INTO students (name, email, course) VALUES (?, ?, ?)
   ```
   Executed using `ps.executeUpdate()` — returns number of affected rows.
2. **Read (SELECT):**
   ```sql
   SELECT id, name, email, course FROM students ORDER BY id ASC
   ```
   Executed using `ps.executeQuery()` — returns a `ResultSet`.
3. **Update (UPDATE):**
   ```sql
   UPDATE students SET course = ? WHERE id = ?
   ```
   Executed using `ps.executeUpdate()` — returns number of rows updated.
4. **Delete (DELETE):**
   ```sql
   DELETE FROM students WHERE id = ?
   ```
   Executed using `ps.executeUpdate()` — returns number of rows deleted.

### F. Resource Management & Clean-up
Database connections and cursors consume OS sockets and database threads. They must always be closed after use.
Modern Java achieves this cleanly using **`try-with-resources`**, which automatically closes all objects implementing `AutoCloseable` (`Connection`, `Statement`, `PreparedStatement`, `ResultSet`):
```java
try (Connection con = DB.connect();
     PreparedStatement ps = con.prepareStatement(sql);
     ResultSet rs = ps.executeQuery()) {
    // Process results...
} // All resources closed automatically even if an exception occurs
```

---

## 3. Project Structure

```
07-jdbc-mysql/
├── pom.xml                 # Maven build file (release 17, mysql-connector-j 9.4.0)
├── schema.sql              # MySQL DDL script with seed records
├── lib/
│   └── mysql-connector-j.jar  # Standalone Type-4 MySQL JDBC driver
├── src/
│   ├── DB.java             # Connection factory with environment variable overrides
│   ├── Main.java           # Interactive & automated CRUD console practical
│   └── JdbcDemo.java       # Direct 1-to-1 reproduction of the professor's PDF
└── README.md               # Lab documentation and setup manual
```

---

## 4. Local MySQL Setup Instructions

### Step 1: Start MySQL Server
* **macOS (Homebrew):**
  ```bash
  brew services start mysql
  ```
* **Linux (systemd):**
  ```bash
  sudo systemctl start mysql
  ```
* **Windows:**
  Start MySQL from the Windows Services console (`services.msc`) or MySQL Notifier.

### Step 2: Import the Database Schema
Execute `schema.sql` into MySQL:
```bash
# If your local MySQL root user has a password:
mysql -u root -p < schema.sql

# If your local root user has no password (common default):
mysql -u root < schema.sql
```

Verify that the database and table were created:
```sql
mysql -u root -p
USE java_lab;
SHOW TABLES;
DESC students;
SELECT * FROM students;
```

---

## 5. Configuration (Environment Variables)

To prevent committing sensitive credentials, the application reads connection details from environment variables with safe localhost defaults:

| Variable | Description | Default Fallback Value |
| :--- | :--- | :--- |
| `DB_URL` | JDBC Connection URL string | `jdbc:mysql://localhost:3306/java_lab?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `DB_USER` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | `""` (empty string) |

### Setting Environment Variables in your Terminal:
* **macOS / Linux (Bash / Zsh):**
  ```bash
  export DB_URL="jdbc:mysql://localhost:3306/java_lab"
  export DB_USER="root"
  export DB_PASSWORD="your_actual_mysql_password"
  ```
* **Windows (Command Prompt):**
  ```cmd
  set DB_URL=jdbc:mysql://localhost:3306/java_lab
  set DB_USER=root
  set DB_PASSWORD=your_actual_mysql_password
  ```
* **Windows (PowerShell):**
  ```powershell
  $env:DB_URL="jdbc:mysql://localhost:3306/java_lab"
  $env:DB_USER="root"
  $env:DB_PASSWORD="your_actual_mysql_password"
  ```

---

## 6. How to Compile and Run

### Method 1: Using `javac` and `java` (Command Line / VS Code)

#### On macOS / Linux:
```bash
# 1. Compile Java source files
javac -cp "lib/mysql-connector-j.jar" -d out src/*.java

# 2. Run the Main CRUD application
java -cp "out:lib/mysql-connector-j.jar" Main

# 3. (Optional) Run the professor's PDF demo
java -cp "out:lib/mysql-connector-j.jar" JdbcDemo
```

#### On Windows:
```cmd
:: 1. Compile Java source files
javac -cp "lib/mysql-connector-j.jar" -d out src/*.java

:: 2. Run the Main CRUD application (note semicolon delimiter)
java -cp "out;lib/mysql-connector-j.jar" Main

:: 3. (Optional) Run the professor's PDF demo
java -cp "out;lib/mysql-connector-j.jar" JdbcDemo
```

---

### Method 2: Using Maven

```bash
# 1. Compile project
mvn clean compile

# 2. Run the Main application
mvn exec:java

# 3. (Optional) Run the professor's PDF demo
mvn exec:java -Dexec.mainClass="JdbcDemo"
```

---

## 7. Sample Interactive Console Output

When running `Main`, you will see:
```text
==================================================
  Experiment 7: JDBC with MySQL CRUD Demonstration
==================================================
--------------------------------------------------
  JDBC Connection Configuration
--------------------------------------------------
  URL      : jdbc:mysql://localhost:3306/java_lab?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
  User     : root
  Password : (empty/none)
--------------------------------------------------
[SUCCESS] Connected to MySQL database successfully!

--------------------------------------------------
  STUDENT MANAGEMENT MENU (JDBC CRUD)
--------------------------------------------------
  1. Add Student         (INSERT / CREATE)
  2. View All Students   (SELECT / READ)
  3. Update Course       (UPDATE)
  4. Delete Student      (DELETE)
  5. Run Automated Demo  (All CRUD in sequence)
  0. Exit
--------------------------------------------------
Enter choice (0-5): 2

--- Current Students in Database ---
ID     | Name                 | Email                     | Course              
-----------------------------------------------------------------------------
1      | Rahul Sharma         | rahul@example.com         | Computer Science    
2      | Amit Verma           | amit@example.com          | Information Tech    
3      | Neha Patel           | neha@example.com          | Electronics & Comm  
-----------------------------------------------------------------------------
Total records: 3
```

Option `5` executes the entire CRUD sequence automatically:
1. **INSERT** temporary student `Demo User`.
2. **SELECT** and display updated table.
3. **UPDATE** the course to `Artificial Intelligence`.
4. **DELETE** the temporary record.

---

## 8. Common Errors & Troubleshooting Guide
*(As referenced in the professor's lab manual)*

| Error | Cause | Solution |
| :--- | :--- | :--- |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | MySQL Connector/J JAR is not in the classpath. | Ensure `lib/mysql-connector-j.jar` is included with `-cp` when compiling/running. |
| `Communications link failure` | MySQL Server daemon is not running on port 3306. | Start MySQL service (`brew services start mysql` or check Service Manager). |
| `Access denied for user 'root'@'localhost'` | Incorrect MySQL username or password provided. | Export correct `DB_USER` and `DB_PASSWORD` environment variables. |
| `Unknown database 'java_lab'` (or `'college'`) | Database has not been created in MySQL. | Run `mysql -u root -p < schema.sql` to initialize databases. |
| `Table 'students' doesn't exist` | Table creation script was skipped. | Run `schema.sql` or let `Main.java` auto-initialize the table on startup. |
| `Duplicate entry for key 'PRIMARY'` / `'email'` | A record with the specified ID or unique email already exists. | Use a different ID / email or remove conflicting rows before inserting. |

---

## 9. Verification & Test Status

| Component | Target / Practical Requirement | Status | Verification Command | Result |
| :--- | :--- | :--- | :--- | :--- |
| **`DB.java`** | Connects via `DriverManager`, reads environment variables | **PASS** | `javac -cp "lib/mysql-connector-j.jar" -d out src/*.java` | Compiled cleanly |
| **`Main.java`** | Full CRUD with `PreparedStatement`, interactive & automated demo | **PASS** | `javac -cp "lib/mysql-connector-j.jar" -d out src/*.java` | Compiled cleanly |
| **`JdbcDemo.java`**| Matches PDF `college`/`student` step-by-step walkthrough | **PASS** | `javac -cp "lib/mysql-connector-j.jar" -d out src/*.java` | Compiled cleanly |
| **`pom.xml`** | Maven build with `mysql-connector-j` 9.4.0 | **PASS** | `mvn compile` | `BUILD SUCCESS` (0.58s) |
| **Runtime Diagnostics**| Graceful handling and diagnostic messaging when offline | **PASS** | `java -cp "out:lib/mysql-connector-j.jar" Main` | Connection diagnostic PASS |
| **Credentials Safety** | No hardcoded production passwords committed | **PASS** | Code audit | Verified clean |
