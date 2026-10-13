package client;

import remote.InventoryService;
import remote.ProductDTO;

import java.rmi.Naming;
import java.util.List;

/**
 * RmiClient: Client performing remote method invocation on ShopSphere RMI Server.
 * Demonstrates Naming.lookup(), stub creation, parameter marshaling, and remote execution.
 */
public class RmiClient {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 1099;

        System.out.println("==================================================");
        System.out.println("  ShopSphere Java RMI Client Demonstration");
        System.out.println("  Connecting to RMI Registry at " + host + ":" + port);
        System.out.println("==================================================");

        try {
            long startTime = System.currentTimeMillis();
            String lookupUrl = "rmi://" + host + ":" + port + "/InventoryService";
            System.out.println("[*] Looking up remote service stub: " + lookupUrl);

            InventoryService service = (InventoryService) Naming.lookup(lookupUrl);
            System.out.println("[+] Remote Stub acquired: " + service.getClass().getName());

            // 1. Remote call: getAllProducts
            System.out.println("\n--- [Remote Call 1]: service.getAllProducts() ---");
            List<ProductDTO> products = service.getAllProducts();
            System.out.println("Returned " + products.size() + " remote products:");
            for (ProductDTO p : products) {
                System.out.println("  • " + p);
            }

            // 2. Remote call: getProduct(1)
            System.out.println("\n--- [Remote Call 2]: service.getProduct(1) ---");
            ProductDTO p1 = service.getProduct(1);
            System.out.println("Product 1 details: " + p1);

            // 3. Remote call: getStock(1)
            System.out.println("\n--- [Remote Call 3]: service.getStock(1) ---");
            int initialStock = service.getStock(1);
            System.out.println("Initial Stock of Product 1: " + initialStock);

            // 4. Remote call: updateStock(1, initialStock + 10)
            int newTargetStock = initialStock + 10;
            System.out.println("\n--- [Remote Call 4]: service.updateStock(1, " + newTargetStock + ") ---");
            boolean updated = service.updateStock(1, newTargetStock);
            System.out.println("Update Status: " + (updated ? "SUCCESS" : "FAILED"));

            // 5. Remote call: verify updated stock
            System.out.println("\n--- [Remote Call 5]: service.getStock(1) (Verification) ---");
            int updatedStock = service.getStock(1);
            System.out.println("Updated Stock of Product 1: " + updatedStock);

            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("\n==================================================");
            System.out.println("  All RMI Operations Executed Successfully!");
            System.out.println("  Total Round-Trip Latency: " + totalTime + " ms");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("[-] RMI Client invocation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
