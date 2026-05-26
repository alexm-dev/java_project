package app.util;

/**
 * Password strength rules for ShareSpace.
 */
public final class PasswordValidator {

    private static final int MIN_LENGTH = 12;

    /** Human-readable description of the rules, for error messages in the UI. */
    public static final String RULES =
        "password must be at least " + MIN_LENGTH
        + " chars with at least one uppercase, one lowercase, one digit and one symbol";

    private PasswordValidator() {}

    /**
     * Checks whether the given password meets the strength rules.
     * Does not modify or zero the array.
     *
     * @param password the candidate password (may be null)
     * @return true if the password is acceptable
     */
    public static boolean isValid(char[] password) {
        if (password == null || password.length < MIN_LENGTH) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSymbol = false;
        boolean hasDigit = false;

        for (char c : password) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) hasSymbol = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUpper && hasLower && hasSymbol && hasDigit;
    }
}
