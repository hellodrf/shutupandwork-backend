package com.cervidae.shutupandwork.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    /**
     * Lifetime of ranking cache
     */
    public static final int rankingCacheExpiry = 5000; // Unix timestamp (1s = 1000)

    /**
     * Admin password
     */
    public static final String adminPassword = "12345";

    /**
     * SessionID requirement, you must change both of them at the same time
     */
    public static final String sessionIDRegex = "[0-9]{6}"; // 6 digit number

    public static final String sessionIDDescription = "a 6 digit number";

    /**
     * Maximum threshold for a session to succeed
     */
    public static final int sessionSuccessThreshold = 5000; // Unix timestamp (1s = 1000)

    /**
     * Map of error code to error message
     */
    public static final Map<Integer, String> errorCodeMap = new HashMap<>() {
        {
            // System
            put(1001, "Incorrect admin password");
            put(1002, "Internal error, please contact system admin");
            put(1003, "Database error, please contact system admin");
            put(1404, "Resource not found");

            // Quote
            put(2001, "Quote must not be empty");

            // User
            put(3001, "Cannot find user in database");
            put(3002, "User already exists");

            // Session
            put(4001, "Session is not in waiting state");
            put(4002, "Session is not in active state");
            put(4003, "Session is not in success or fail state");
            put(4004, "Cannot find specified session");
            put(4005, "SessionID must be " + sessionIDDescription);
            put(4006, "Session has not reach its target, maximum threshold is: "
                    + Constants.sessionSuccessThreshold/1000 + " seconds");

        }
    };

}
