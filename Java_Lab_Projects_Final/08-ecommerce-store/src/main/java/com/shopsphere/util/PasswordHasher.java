package com.shopsphere.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for hashing and verifying passwords using SHA-256 with a project salt.
 * Ensures plain passwords are never stored in the database.
 */
public final class PasswordHasher {

    private static final String SALT = "shopsphere_salt_2026";

    private PasswordHasher() {}

    /**
     * Produces a hexadecimal SHA-256 digest of (SALT + plainPassword).
     *
     * @param plainPassword Plaintext password
     * @return 64-character hex string
     */
    public static String hash(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] input = (SALT + plainPassword).getBytes(StandardCharsets.UTF_8);
            byte[] digest = md.digest(input);
            StringBuilder sb = new StringBuilder(64);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available in JVM", e);
        }
    }

    /**
     * Verifies a plain password against a stored hash.
     *
     * @param plainPassword Candidate plaintext password
     * @param storedHash Stored hex hash from the database
     * @return true if candidate matches stored hash
     */
    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        return hash(plainPassword).equalsIgnoreCase(storedHash);
    }
}
