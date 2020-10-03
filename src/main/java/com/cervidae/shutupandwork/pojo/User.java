package com.cervidae.shutupandwork.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private int id;

    private String username;

    private int score;

    private long updated; // Unix timestamp*1000 (1000=1s), from currentTimeMillis

    /**
     * Overriding lombok to convert MySQL datetime to Unix timestamp
     */
    public void setUpdated(Date updated) {
        this.updated = updated.getTime() / 1000;
    }
}
