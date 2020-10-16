package com.cervidae.shutupandwork.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseUser implements Serializable {

    public static final long serialVersionUID = -6329703559756525294L;

    private int id;

    private String username;

    private int score;

    public ResponseUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.score = user.getScore();
    }
}
