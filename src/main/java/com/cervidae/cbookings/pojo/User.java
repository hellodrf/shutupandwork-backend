package com.cervidae.cbookings.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private int id;

    private String username;

    private String password;

    private String name;

    private String address;

    private String phone;

    private String email;

    private String extra;

    private int roleId;

    private Date created;

    private Date updated;

}
