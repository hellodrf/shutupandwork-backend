package com.cervidae.shutupandwork.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Ranking {

    private Map<Integer, User> rankMap;

    private int top;

    private long timeGenerated; // Unix timestamp*1000 (1000=1s), from currentTimeMillis

    /**
     * Pack SQL result (array of users) into ranking object
     * @param users all users on the ranking
     */
    public Ranking(User[] users) {
        this.timeGenerated = System.currentTimeMillis();
        this.top = users.length;
        this.rankMap = new HashMap<>();
        for (int i = 0; i < top; i++) {
            rankMap.put(i+1, users[i]);
        }
    }

    public boolean isExpired(long expiry) {
        return (System.currentTimeMillis()-this.getTimeGenerated()) >= expiry;
    }
}
