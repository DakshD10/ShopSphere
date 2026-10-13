import java.io.*;
import java.net.*;

/**
 * Experiment 4: Simple TCP Client
 * Reference: Professor's "Server and Client" Lab Material
 *
 * Connects to localhost on port 5000, sends "Hello Server",
 * receives the server's reply, prints it, and closes the connection.
 */
public class Client {
    public static void main(String[] args) {
        try {
            // Connect to server running on localhost at port 5000
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to server.");

            // Set up input reader to receive response from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Set up output writer with auto-flush (true) to send message to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Send greeting message to server
            output.println("Hello Server");

            // Read server's reply
            String response = input.readLine();
            System.out.println("Server says: " + response);

            // Close socket and release network resources
            socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
