import java.io.*;
import java.net.*;

/**
 * Experiment 4: Simple TCP Server
 * Reference: Professor's "Server and Client" Lab Material
 *
 * Listens on port 5000, accepts a single client connection,
 * reads one message, responds with "Hello Client", and closes the connection.
 */
public class Server {
    public static void main(String[] args) {
        try {
            // Create ServerSocket listening on port 5000
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started...");
            System.out.println("Waiting for client...");

            // Blocking call: waits until a client connects
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            // Set up input reader to receive message from client
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Set up output writer with auto-flush (true) to send message to client
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Read the message sent by the client
            String message = input.readLine();
            System.out.println("Client says: " + message);

            // Send response back to the client
            output.println("Hello Client");

            // Close sockets and release network resources
            socket.close();
            serverSocket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
