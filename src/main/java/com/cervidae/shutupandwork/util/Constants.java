package com.cervidae.shutupandwork.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AaronDu
 */
public class Constants {

    /**
     * Lifetime of ranking cache, Unix timestamp (1s = 1000)
     */
    public static final int RANKING_CACHE_EXPIRY = 5000;

    /**
     * Admin password (!deprecated)
     */
    public static final String ADMIN_PASSWORD = "12345";

    /**
     * SessionID requirement, you must change both of them at the same time
     */
    public static final String SESSION_ID_REGEX = "[0-9]{6}";

    public static final String SESSION_ID_DESCRIPTION = "a 6 digit number";

    /**
     * Maximum threshold for a session to succeed, Unix timestamp (1s = 1000)
     * 5 seconds
     */
    public static final int SESSION_SUCCESS_THRESHOLD = 5000;

    /**
     * How long should the session be kept in cache, Unix timestamp (1s = 1000)
     * 1 day (86400 seconds)
     * PS. this actually means sessions will be removed midnight the day after
     */
    public static final int SESSION_EXPIRY = 86400000;

    /**
     * Map of error code to error message
     */
    public static final Map<Integer, String> ERROR_CODE_MAP = new HashMap<>() {
        {
            // System
            put(1001, "Incorrect admin password");
            put(1002, "Internal server error, please contact system admin");
            put(1003, "Database error, please contact system admin");
            put(1404, "Resource not found");
            put(1005, "Redis cache error, please contact system admin");
            put(1006, "Argument requirements not satisfied");

            // Quote
            put(2001, "Quote must not be empty");
            put(2002, "Cannot find specified quote(s) in database");

            // User
            put(3001, "Cannot find user in database");
            put(3002, "User already exists");
            put(3003, "Unauthorized action");
            put(3004, "Invalid credentials");

            // Session
            put(4001, "Session is not in waiting state");
            put(4002, "Session is not in active state");
            put(4003, "Session is not in success or fail state");
            put(4004, "Cannot find specified session");
            put(4005, "SessionID must be " + SESSION_ID_DESCRIPTION);
            put(4006, "Session has not reach its target, maximum threshold is: "
                    + Constants.SESSION_SUCCESS_THRESHOLD /1000 + " seconds");
        }
    };

}
