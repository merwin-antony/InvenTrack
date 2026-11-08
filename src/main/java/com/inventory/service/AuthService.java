package com.inventory.service;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Service managing user authentication, login validation, session state, and account creation.
 */
public class AuthService {

    private final UserDAO userDAO;
    private static User currentUser = null;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Authenticates a user with given username and password credentials.
     *
     * @param username the entered username
     * @param password the entered password as char array
     * @return the authenticated User object
     * @throws AuthenticationException if credentials are invalid or missing
     * @throws SQLException if a database error occurs
     */
    public User authenticate(String username, char[] password) throws AuthenticationException, SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("Please enter your username.");
        }
        if (password == null || password.length == 0) {
            throw new AuthenticationException("Please enter your password.");
        }

        try {
            Optional<User> userOpt = userDAO.findByUsername(username.trim());

            if (userOpt.isEmpty()) {
                throw new AuthenticationException("Invalid username or password.");
            }

            User user = userOpt.get();
            boolean match = PasswordHasher.verifyPassword(password, user.getSalt(), user.getPasswordHash());

            if (!match) {
                throw new AuthenticationException("Invalid username or password.");
            }

            currentUser = user;
            return user;
        } finally {
            // Memory hygiene: Zero out the password characters
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
    }

    /**
     * Registers a new user account.
     */
    public User registerUser(String username, char[] password, String fullName, String role)
            throws ValidationException, SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException("Username cannot be empty.");
        }
        if (username.trim().length() < 3 || username.trim().length() > 50) {
            throw new ValidationException("Username must be between 3 and 50 characters.");
        }
        if (password == null || password.length < 4) {
            throw new ValidationException("Password must be at least 4 characters long.");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Full name cannot be empty.");
        }

        try {
            Optional<User> existing = userDAO.findByUsername(username.trim());
            if (existing.isPresent()) {
                throw new ValidationException("Username '" + username.trim() + "' is already taken.");
            }

            String salt = PasswordHasher.generateSalt();
            String hash = PasswordHasher.hashPassword(password, salt);

            User newUser = new User(
                    username.trim(),
                    hash,
                    salt,
                    fullName.trim(),
                    role != null ? role.toUpperCase() : "ADMIN"
            );

            userDAO.createUser(newUser);
            return newUser;
        } finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }
}
