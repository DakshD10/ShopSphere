# Experiment 4 — Java TCP Client-Server Socket Communication

## Aim
To design and implement a fundamental connection-oriented client-server communication application in Java using TCP sockets (`ServerSocket` and `Socket`) from the `java.net` package.

---

## Overview & Networking Concepts

Network programming involves writing software that enables communication between two or more independent processes running on the same computer or across a network.

### Key Terms (From Lab Reference)
- **Client**: A program that initiates communication and requests a service or data from another program. In this experiment, the client sends `"Hello Server"`.
- **Server**: A program that listens on a designated port, waits for incoming client connection requests, and serves them. In this experiment, the server replies with `"Hello Client"`.
- **IP Address**: Identifies a host or device on a network (e.g., loopback address `127.0.0.1` or `localhost`).
- **Port Number**: Identifies a specific application or service running on the host. In this experiment, the server binds to port **`5000`**.
- **Socket**: An endpoint for two-way communication between two programs. Java encapsulates socket communication using classes in `java.net`.

### Communication Flow
```text
CLIENT                                      SERVER
  |                                            |  [Listening on port 5000]
  | ------- TCP Handshake Connection --------> |  [accept() blocks until connected]
  | <------ Connection Accepted -------------- |
  |                                            |
  | ------------ "Hello Server" -------------> |  [Reads client message]
  |                                            |  [Prints "Client says: Hello Server"]
  | <----------- "Hello Client" -------------- |  [Sends reply]
  |                                            |
  | [Reads server reply]                       |
  | [Prints "Server says: Hello Client"]       |
  |                                            |
  | ------------ Close Socket ---------------> |  [Closes client socket & serverSocket]
  | [Connection closed.]                       |  [Connection closed.]
```

---

## TCP vs UDP Comparison

This practical uses **TCP (Transmission Control Protocol)**. The table below highlights key differences between TCP and UDP as covered in the lab syllabus:

| Feature | TCP (Transmission Control Protocol) | UDP (User Datagram Protocol) |
| :--- | :--- | :--- |
| **Connection Type** | Connection-oriented (establishes 3-way handshake) | Connectionless |
| **Reliability** | Highly reliable (acknowledgments & retransmissions) | Less reliable (no acknowledgment) |
| **Ordering** | Guarantees ordered delivery of packets | No guarantee of packet delivery order |
| **Error Handling** | Error-controlled and flow-controlled | Minimal error checking (checksum only) |
| **Java Classes** | `java.net.Socket`, `java.net.ServerSocket` | `java.net.DatagramSocket`, `java.net.DatagramPacket` |
| **Overhead** | Higher overhead due to connection management | Lower overhead, lightweight, faster |
| **Common Use Cases** | Web (HTTP/HTTPS), Email (SMTP), File Transfer (FTP) | Video streaming, DNS, VoIP, Online gaming |

---

## Project Structure

```text
04-tcp-client-server/
├── README.md               # Complete lab documentation and execution guide
└── src/
    ├── Server.java         # Server process: binds port 5000, accepts connection, replies
    └── Client.java         # Client process: connects to localhost:5000, exchanges messages
```

---

## Code Breakdown

### 1. `Server.java`
- **Imports**: `java.io.*` for I/O streams and `java.net.*` for networking primitives.
- **`ServerSocket serverSocket = new ServerSocket(5000);`**: Binds the server to TCP port `5000` and starts listening for incoming connection requests.
- **`Socket socket = serverSocket.accept();`**: A **blocking call** that halts execution until a client connects. Once connected, it returns a `Socket` object representing the established communication channel.
- **`BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));`**: Wraps the socket's byte input stream into a character-based buffered reader to read line-delimited text (`readLine()`).
- **`PrintWriter output = new PrintWriter(socket.getOutputStream(), true);`**: Wraps the socket's byte output stream into a formatted writer. Passing `true` enables auto-flushing on `println()`.
- **Message Exchange**: Reads one line (`input.readLine()`), prints it to the console, and replies with `"Hello Client"`.
- **Clean Teardown**: Closes the client `socket` and the `serverSocket`, freeing system resources.

### 2. `Client.java`
- **`Socket socket = new Socket("localhost", 5000);`**: Establishes a TCP connection to the server listening on the loopback address (`localhost`) at port `5000`.
- **Message Exchange**: Sends `"Hello Server"` using `output.println(...)`, waits for the response via `input.readLine()`, and prints `"Server says: Hello Client"`.
- **Clean Teardown**: Closes the client `socket`.

---

## Why Are Server and Client Separate Programs?

The server and client represent **two separate, independent processes**:
1. The **server** must be started first so that it is actively listening on the target port before any connection attempts.
2. The **client** initiates the connection request to the server.
3. If the client is executed before the server is running, the operating system will reject the connection with:
   ```text
   java.net.ConnectException: Connection refused
   ```
4. They compile into separate bytecode classes (`Server.class` and `Client.class`) and run in separate Java Virtual Machine (JVM) instances.

---

## Compilation & Execution Guide

### Step 1: Compile Both Programs

From the `04-tcp-client-server` directory:

```bash
# Using standard javac:
javac -d out src/Server.java src/Client.java

# Or using macOS Homebrew OpenJDK path:
/opt/homebrew/opt/openjdk/bin/javac -d out src/Server.java src/Client.java
```

This creates `Server.class` and `Client.class` in the `out/` directory.

---

### Step 2: Open Two Terminals and Run

> **IMPORTANT**: Always start **Terminal 1 (Server)** before running **Terminal 2 (Client)**!

#### Terminal 1 — Start the Server
```bash
# Navigate to project directory:
cd /Users/dakshduhlani/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/04-tcp-client-server

# Run the Server:
java -cp out Server

# (Or with OpenJDK path):
/opt/homebrew/opt/openjdk/bin/java -cp out Server
```

**Expected Terminal 1 Output (initially):**
```text
Server started...
Waiting for client...
```
*(The server pauses here at `serverSocket.accept()`, waiting for the client).*

---

#### Terminal 2 — Run the Client
```bash
# Navigate to project directory:
cd /Users/dakshduhlani/Desktop/Java_Lab_Exp/Java_Lab_Projects_Final/04-tcp-client-server

# Run the Client:
java -cp out Client

# (Or with OpenJDK path):
/opt/homebrew/opt/openjdk/bin/java -cp out Client
```

**Expected Terminal 2 Output:**
```text
Connected to server.
Server says: Hello Client
Connection closed.
```

---

#### Terminal 1 — Completed Server Output
Once the client connects and transmits its message, Terminal 1 will finish printing:
```text
Server started...
Waiting for client...
Client connected.
Client says: Hello Server
Connection closed.
```
Both processes terminate cleanly with code 0.

---

## Troubleshooting & macOS Note

### `java.net.BindException: Address already in use` (macOS AirPlay)
On macOS Monterey and later, Apple's built-in **AirPlay Receiver** listens on port **5000** by default. If running the server produces this error:
1. Open **System Settings** on macOS.
2. Search for or navigate to **General > AirDrop & AirPlay**.
3. Toggle **AirPlay Receiver** to **Off**.
4. Re-run `Server.java`.

### `java.net.ConnectException: Connection refused`
This occurs if `Client` is started before `Server`. Make sure `Server` is running in Terminal 1 and displaying `"Waiting for client..."` before running `Client` in Terminal 2.
