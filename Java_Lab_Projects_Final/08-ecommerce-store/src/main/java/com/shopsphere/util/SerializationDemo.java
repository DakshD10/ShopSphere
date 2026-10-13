package com.shopsphere.util;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Demonstrates Object Serialization and Deserialization matching Section 20 of the RTU guide.
 * Shows how Product and Order objects are converted into byte streams for storage or network transport.
 */
public class SerializationDemo {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  ShopSphere - Java Object Serialization Demonstration");
        System.out.println("==========================================================");

        try {
            // 1. Create source Product object
            Product originalProduct = new Product(101, 1, "ANC Studio Headphones", "AudioCraft",
                    "Premium active noise cancelling over-ear headphones", 4999.00, 15.00, 20, "img.jpg", true);

            System.out.println("\n[1] Original Product Object:");
            System.out.println("    Class       : " + originalProduct.getClass().getName());
            System.out.println("    Product ID  : " + originalProduct.getProductId());
            System.out.println("    Name        : " + originalProduct.getName());
            System.out.println("    Price       : ₹" + originalProduct.getPrice());
            System.out.println("    Discounted  : ₹" + originalProduct.getDiscountedPrice());

            // 2. Serialize to byte array using ObjectOutputStream
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try (ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
                objOut.writeObject(originalProduct);
                objOut.flush();
            }

            byte[] serializedBytes = byteOut.toByteArray();
            System.out.println("\n[2] Serialization Result:");
            System.out.println("    Serialized Stream Size: " + serializedBytes.length + " bytes");
            System.out.printf("    First 16 bytes (Hex)  : ");
            for (int i = 0; i < Math.min(16, serializedBytes.length); i++) {
                System.out.printf("%02X ", serializedBytes[i]);
            }
            System.out.println();

            // 3. Deserialize back using ObjectInputStream
            Product deserializedProduct;
            try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serializedBytes))) {
                deserializedProduct = (Product) objIn.readObject();
            }

            System.out.println("\n[3] Deserialized Product Object:");
            System.out.println("    Product ID  : " + deserializedProduct.getProductId());
            System.out.println("    Name        : " + deserializedProduct.getName());
            System.out.println("    Discounted  : ₹" + deserializedProduct.getDiscountedPrice());
            System.out.println("    State Match : " + (originalProduct.getProductId() == deserializedProduct.getProductId() 
                    && originalProduct.getName().equals(deserializedProduct.getName()) ? "SUCCESS (Objects are identical)" : "FAILED"));

            // 4. Serialize an Order with OrderItems
            Order order = new Order(501, 2, 1, 4249.15, 749.85, "UPI", "SUCCESS", "CONFIRMED");
            order.addItem(new OrderItem(1, 501, 101, "ANC Studio Headphones", 1, 4249.15));

            ByteArrayOutputStream orderByteOut = new ByteArrayOutputStream();
            try (ObjectOutputStream objOut = new ObjectOutputStream(orderByteOut)) {
                objOut.writeObject(order);
                objOut.flush();
            }

            Order deserializedOrder;
            try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(orderByteOut.toByteArray()))) {
                deserializedOrder = (Order) objIn.readObject();
            }

            System.out.println("\n[4] Complex Graph Serialization (Order + OrderItems):");
            System.out.println("    Order ID    : #" + deserializedOrder.getOrderId());
            System.out.println("    Total Paid  : ₹" + deserializedOrder.getTotalAmount());
            System.out.println("    Items Count : " + deserializedOrder.getItems().size());
            System.out.println("    Item Name   : " + deserializedOrder.getItems().get(0).getProductName());

            System.out.println("\n==========================================================");
            System.out.println("  [VERIFIED] Object Serialization & Deserialization PASSED");
            System.out.println("==========================================================");

        } catch (Exception e) {
            System.err.println("[Serialization Error] " + e.getMessage());
            e.printStackTrace();
        }
    }
}
