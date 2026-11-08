package com.inventory.service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * Secure Password Hasher using PBKDF2 with HMAC-SHA256 and Cryptographic Salt.
 */
public class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16; // 16 bytes = 128 bits

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a new random cryptographic salt, returned as a hex string.
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        return HexFormat.of().formatHex(salt);
    }

    /**
     * Computes the PBKDF2 HMAC-SHA256 hash of a password using the provided salt.
     *
     * @param password the password as a char array
     * @param saltHex the salt in hex representation
     * @return hex encoded hash string
     */
    public static String hashPassword(char[] password, String saltHex) {
        if (password == null || saltHex == null) {
            throw new IllegalArgumentException("Password and salt cannot be null.");
        }

        byte[] salt = HexFormat.of().parseHex(saltHex);
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Error during password hashing: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /**
     * Verifies whether an input password matches the stored salt and hash.
     * Uses constant-time comparison (MessageDigest.isEqual) to prevent timing attacks.
     *
     * @param inputPassword password entered by the user
     * @param saltHex stored user salt
     * @param expectedHash stored password hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(char[] inputPassword, String saltHex, String expectedHash) {
        if (inputPassword == null || saltHex == null || expectedHash == null) {
            return false;
        }

        String computedHash = hashPassword(inputPassword, saltHex);
        return java.security.MessageDigest.isEqual(
                computedHash.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                expectedHash.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
    }
}
