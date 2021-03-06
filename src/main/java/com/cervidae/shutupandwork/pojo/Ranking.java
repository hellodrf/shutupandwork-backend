package com.cervidae.shutupandwork.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AaronDu
 */
@Data
@NoArgsConstructor
public class Ranking implements Serializable {

    public static final long serialVersionUID = 6813392678220161008L;

    @Setter(AccessLevel.PRIVATE)
    private Map<Integer, User> rankMap;

    private int top;

    /**
     * Unix timestamp (1s = 1000)
     */
    private long timeGenerated;

    /**
     * Pack SQL result (array of users) into a ranking object
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
        return (System.currentTimeMillis() - this.getTimeGenerated()) >= expiry;
    }
}
