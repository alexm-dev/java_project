package app.util;

import java.util.regex.Pattern;

/**
 * Basic email format check using a pragmatic RFC 5322 subset.
 *
 * Rejects null, blank, missing @, missing TLD, leading/trailing dots and the
 * other usual obvious mistakes.
 *
 * Uses simple regex matching.
 */
public final class EmailValidator {

    private static final Pattern PATTERN = Pattern.compile(
        "^[A-Za-z0-9._+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$"
    );

    private EmailValidator() {}

    /**
     * Returns true if the given string looks like a valid email address.
     *
     * @param email the candidate string (may be null)
     * @return true if it matches the accepted email shape
     */
    public static boolean isValid(String email) {
        if (email == null) { 
            return false;
        }

        String trimmed = email.trim();
        if (trimmed.isEmpty()) { 
            return false;
        }

        if (trimmed.startsWith(".") || trimmed.contains("..")) { 
            return false;
        }

        return PATTERN.matcher(trimmed).matches();
    }
}
