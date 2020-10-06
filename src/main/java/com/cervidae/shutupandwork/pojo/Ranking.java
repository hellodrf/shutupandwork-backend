package com.cervidae.shutupandwork.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
public class Ranking {

    @Setter(AccessLevel.PRIVATE)
    private Map<Integer, User> rankMap;

    private int top;

    private long timeGenerated; // Unix timestamp (1s = 1)

    /**
     * Pack SQL result (array of users) into a ranking object
     * @param users all users on the ranking
     */
    public Ranking(User[] users) {
        this.timeGenerated = System.currentTimeMillis()/1000;
        this.top = users.length;
        this.rankMap = new HashMap<>();
        for (int i = 0; i < top; i++) {
            rankMap.put(i+1, users[i]);
        }
    }

    public boolean isExpired(long expiry) {
        return (System.currentTimeMillis()/1000 - this.getTimeGenerated()) >= expiry;
    }
}
