package com.cervidae.shutupandwork.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> implements Serializable {

    public static final long serialVersionUID = 4210673593941538294L;

    private long timestamp;

    private int success; // 0=fail 1=success

    private String message; // error message

    private T payload;

    private Response(T payload, int success, String message) {
        this.payload = payload;
        this.success = success;
        this.message = message;
        this.timestamp = System.currentTimeMillis()/1000;
    }

    /**
     * Produce a success response, without payload.
     * @param <T> no use, wildcard is fine
     * @return a simple success response
     */
    public static <T> Response<T> success() {
        return new Response<>(null, 1, null);
    }

    /**
     * Produce a success response, with a payload.
     * @param payload payload to carry
     * @param <T> payload type
     * @return a success response with payload
     */
    public static <T> Response<T> success(T payload) {
        return new Response<>(payload, 1, null);
    }

    /**
     * Produce a failure response, no payload or message
     * @param <T> no use, wildcard is fine
     * @return a simple failure response
     */
    public static <T> Response<T> fail() {
        return new Response<>(null, 0, null);
    }

    /**
     * Produce a failure response, with an error message
     * @param message an error message in string
     * @param <T> no use, wildcard is fine
     * @return a failure response with error message
     */
    public static <T> Response<T> fail(String message) {
        return new Response<>(null, 0, message);
    }

    /**
     * Produce a failure response, with an error message and a payload
     * @param message an error message in string
     * @param payload payload to carry
     * @param <T> payload type
     * @return a failure response with payload and error message
     */
    public static <T> Response<T> fail(String message, T payload) {
        return new Response<>(payload, 0, message);
    }
}
