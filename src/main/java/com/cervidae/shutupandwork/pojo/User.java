package com.cervidae.shutupandwork.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author AaronDu
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    public static final long serialVersionUID = -2711154011686271603L;

    private int id;

    private String username;

    private int score;

    private String role;

    private String password;

    private long updated;

}
