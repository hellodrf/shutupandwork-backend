package com.cervidae.shutupandwork.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author AaronDu
 */
@Data
public class Quote implements Serializable {

    public static final long serialVersionUID = -8938121240345775959L;

    private int id;

    private String quote;

    private int type;
}
