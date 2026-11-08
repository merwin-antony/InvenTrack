package com.inventory;

import com.inventory.service.PasswordHasher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PasswordHasher (PBKDF2 with HMAC-SHA256 and Salt).
 */
public class PasswordHasherTest {

    @Test
    @DisplayName("Salt generation creates unique non-empty 32-character hex strings")
    void testSaltGeneration() {
        String salt1 = PasswordHasher.generateSalt();
        String salt2 = PasswordHasher.generateSalt();

        assertNotNull(salt1);
        assertNotNull(salt2);
        assertEquals(32, salt1.length(), "16 bytes should format to 32 hex chars");
        assertNotEquals(salt1, salt2, "Subsequent salts must be randomly generated and unique");
    }

    @Test
    @DisplayName("Password hashing is deterministic with the same salt")
    void testDeterministicHashing() {
        String salt = PasswordHasher.generateSalt();
        char[] password = "SecretPassword123!".toCharArray();

        String hash1 = PasswordHasher.hashPassword(password, salt);
        String hash2 = PasswordHasher.hashPassword(password, salt);

        assertNotNull(hash1);
        assertEquals(hash1, hash2, "Hashing the same password with the same salt must yield identical hashes");
    }

    @Test
    @DisplayName("Different salts produce different hashes for the same password")
    void testSaltEffect() {
        String salt1 = PasswordHasher.generateSalt();
        String salt2 = PasswordHasher.generateSalt();
        char[] password = "admin".toCharArray();

        String hash1 = PasswordHasher.hashPassword(password, salt1);
        String hash2 = PasswordHasher.hashPassword(password, salt2);

        assertNotEquals(hash1, hash2, "Different salts must produce different hash outputs (rainbow table prevention)");
    }

    @Test
    @DisplayName("Password verification succeeds on correct password and rejects incorrect password")
    void testPasswordVerification() {
        String salt = PasswordHasher.generateSalt();
        char[] correctPassword = "CorrectHorseBatteryStaple".toCharArray();
        String hash = PasswordHasher.hashPassword(correctPassword, salt);

        assertTrue(PasswordHasher.verifyPassword("CorrectHorseBatteryStaple".toCharArray(), salt, hash),
                "Verification must return true for correct password");

        assertFalse(PasswordHasher.verifyPassword("WrongPassword".toCharArray(), salt, hash),
                "Verification must return false for incorrect password");

        assertFalse(PasswordHasher.verifyPassword("".toCharArray(), salt, hash),
                "Verification must return false for empty password");

        assertFalse(PasswordHasher.verifyPassword(null, salt, hash),
                "Verification must return false for null password");
    }
}
