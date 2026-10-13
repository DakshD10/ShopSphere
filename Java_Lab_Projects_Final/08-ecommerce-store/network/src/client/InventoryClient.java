package client;

import model.NetworkProduct;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * InventoryClient: Java TCP Socket Client communicating with InventoryServer.
 * Supports interactive command prompt and automated test sequence (--demo flag).
 * Demonstrates socket streams, request-response cycles, and Java Object deserialization.
 */
public class InventoryClient {
    private final String host;
    private final int port;

    public InventoryClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void runDemo() {
        System.out.println("==================================================");
        System.out.println("  ShopSphere TCP Socket Client: Automated Demo");
        System.out.println("  Connecting to " + host + ":" + port + "...");
        System.out.println("==================================================");

        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream())) {

            // Read welcome message
            String welcome = reader.readLine();
            System.out.println("[Server Response]: " + welcome);

            // Test 1: HELP
            sendAndPrint("HELP", writer, reader);

            // Test 2: LIST_ALL
            sendAndPrint("LIST_ALL", writer, reader);

            // Test 3: GET_PRODUCT 1
            sendAndPrint("GET_PRODUCT 1", writer, reader);

            // Test 4: GET_STOCK 1
            sendAndPrint("GET_STOCK 1", writer, reader);

            // Test 5: UPDATE_STOCK 1 95
            sendAndPrint("UPDATE_STOCK 1 95", writer, reader);

            // Test 6: Verify updated stock
            sendAndPrint("GET_STOCK 1", writer, reader);

            // Test 7: SEARCH_PRODUCT phone
            sendAndPrint("SEARCH_PRODUCT phone", writer, reader);

            // Test 8: Object Deserialization demo (SERIALIZE_PRODUCT 2)
            System.out.println("\n>>> [Client Request]: SERIALIZE_PRODUCT 2 (ObjectInputStream demonstration)");
            writer.println("SERIALIZE_PRODUCT 2");
            String status = reader.readLine();
            System.out.println("[Server Response]: " + status);
            if (status != null && status.contains("SENDING_OBJECT")) {
                try {
                    Object obj = objIn.readObject();
                    if (obj instanceof NetworkProduct) {
                        NetworkProduct np = (NetworkProduct) obj;
                        System.out.println("[+] Deserialized Product Object Successfully Received:");
                        System.out.println("    Class: " + np.getClass().getName());
                        System.out.println("    Product ID: " + np.getProductId());
                        System.out.println("    Name: " + np.getName());
                        System.out.println("    Category: " + np.getCategory());
                        System.out.println("    Price: $" + np.getPrice());
                        System.out.println("    Stock: " + np.getStockQuantity());
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("[-] ClassNotFoundException: " + e.getMessage());
                }
            }

            // Test 9: QUIT
            sendAndPrint("QUIT", writer, reader);

            System.out.println("==================================================");
            System.out.println("  Socket Client Demonstration Finished Cleanly!");
            System.out.println("==================================================");

        } catch (IOException e) {
            System.err.println("[-] Socket communication error: " + e.getMessage());
        }
    }

    private void sendAndPrint(String command, PrintWriter writer, BufferedReader reader) throws IOException {
        System.out.println("\n>>> [Client Request]: " + command);
        writer.println(command);
        String response;
        while ((response = reader.readLine()) != null) {
            System.out.println("[Server Response]: " + response);
            if (response.startsWith("200 ") && !response.contains("items:") && !response.contains("matches:") && !response.contains("COMMANDS:")) {
                break;
            }
            if (response.endsWith("END_OF_LIST") || response.startsWith("400 ") || response.startsWith("404 ") || response.startsWith("200 BYE") || response.contains("COMMANDS:")) {
                break;
            }
        }
    }

    public void runInteractive() {
        System.out.println("Connecting to ShopSphere Inventory Server at " + host + ":" + port + "...");
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("[Server]: " + reader.readLine());
            System.out.println("Interactive session ready. Type 'HELP' for commands or 'QUIT' to exit.\n");

            while (true) {
                System.out.print("shopsphere-cli> ");
                if (!scanner.hasNextLine()) break;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                writer.println(line);
                if (line.equalsIgnoreCase("QUIT") || line.equalsIgnoreCase("EXIT")) {
                    System.out.println("[Server]: " + reader.readLine());
                    break;
                }

                String resp = reader.readLine();
                System.out.println("[Server]: " + resp);
                if (resp != null && (resp.contains("items:") || resp.contains("matches:"))) {
                    while ((resp = reader.readLine()) != null) {
                        System.out.println("[Server]: " + resp);
                        if (resp.endsWith("END_OF_LIST")) break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[-] Connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 6060;
        boolean demoMode = false;

        for (String arg : args) {
            if ("--demo".equalsIgnoreCase(arg)) demoMode = true;
            else if (arg.matches("\\d+")) port = Integer.parseInt(arg);
            else if (!arg.startsWith("-")) host = arg;
        }

        InventoryClient client = new InventoryClient(host, port);
        if (demoMode) {
            client.runDemo();
        } else {
            // Default to running demo if non-interactive or user passes --demo
            client.runDemo();
        }
    }
}
