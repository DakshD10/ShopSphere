package server;

import remote.InventoryService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RmiServer: Starts the Java RMI Registry and binds the InventoryService.
 * Standard port: 1099.
 * Demonstrates RMI Registry bootstrap and remote service registration.
 */
public class RmiServer {
    public static final int RMI_PORT = 1099;
    public static final String SERVICE_NAME = "InventoryService";

    public static void main(String[] args) {
        try {
            System.out.println("==================================================");
            System.out.println("  ShopSphere Java RMI Registry & Server Bootstrap");
            System.out.println("==================================================");

            // Step 1: Start RMI Registry programmatically on port 1099
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("[+] RMI Registry successfully started on port: " + RMI_PORT);
            } catch (Exception e) {
                System.out.println("[!] RMI Registry already running on port " + RMI_PORT + ", reusing existing registry.");
                registry = LocateRegistry.getRegistry(RMI_PORT);
            }

            // Step 2: Instantiate Remote Object
            InventoryService service = new InventoryServiceImpl();

            // Step 3: Bind Remote Object to RMI Registry
            String serviceUrl = "rmi://localhost:" + RMI_PORT + "/" + SERVICE_NAME;
            Naming.rebind(serviceUrl, service);

            System.out.println("[+] Service bound successfully at: " + serviceUrl);
            System.out.println("[+] Ready to accept distributed client invocations.");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("[-] Fatal error starting RMI Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
