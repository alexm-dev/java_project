package app.util;

import java.util.Arrays;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Handles password hashing and verification using BCrypt.
 * The work factor is set to 12.
 *
 * Methods accept char[] instead of String so the plaintext can be zeroed
 * out from memory immediately after hashing, reducing the window where
 * a memory dump could expose it.
 */
public class PasswordHasher {

    private static final int WORK_FACTOR = 12;

    private PasswordHasher() {}

    /**
     * Hashes a plain-text password and zeroes out the char[] afterwards.
     *
     * @param plain the plain-text password as a char array (zeroed on return)
     * @return the bcrypt hash to store in the database
     */
    public static String hash(char[] plain) {
        try {
            return BCrypt.hashpw(new String(plain), BCrypt.gensalt(WORK_FACTOR));
        } finally {
            Arrays.fill(plain, '\0');
        }
    }

    /**
     * Verifies a plain-text password against a stored bcrypt hash,
     * then zeroes out the char[].
     *
     * @param plain the plain-text password to check (zeroed on return)
     * @param storedHash the hash retrieved from the database
     * @return true if the password matches, false otherwise
     */
    public static boolean verify(char[] plain, String storedHash) {
        try {
            return BCrypt.checkpw(new String(plain), storedHash);
        } finally {
            Arrays.fill(plain, '\0');
        }
    }
}
