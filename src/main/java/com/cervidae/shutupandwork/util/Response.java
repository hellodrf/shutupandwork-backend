package com.cervidae.shutupandwork.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    public static final long serialVersionUID = 4210673593941538294L;

    private long timestamp;

    private int success; // 0=fail 1=success

    private String message; // error message

    private T content;

    public Response(T content, int success, String message) {
        this.content = content;
        this.success = success;
        this.message = message;
        this.timestamp = System.currentTimeMillis()/1000;
    }

    public static <T> Response<T> success() {
        return new Response<>(null, 1, null);
    }

    public static <T> Response<T> success(T content) {
        return new Response<>(content, 1, null);
    }

    public static <T> Response<T> fail() {
        return new Response<>(null, 0, null);
    }

    public static <T> Response<T> fail(String message) {
        return new Response<>(null, 0, message);
    }

    public static <T> Response<T> fail(String message, T content) {
        return new Response<>(content, 0, message);
    }
}
