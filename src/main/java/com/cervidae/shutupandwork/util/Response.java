package com.cervidae.shutupandwork.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

/**
 * @author AaronDu
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> implements Serializable {

    public static final long serialVersionUID = 4210673593941538294L;

    private long timeStamp;

    /**
     * 0=fail 1=success
     */
    private int success;

    private Integer code = null;

    /**
     * error message
     */
    private String message = null;

    private T payload;

    private Response(T payload, int success, String code) {
        this.payload = payload;
        this.success = success;
        if (code != null) {
            try {
                this.code = Integer.parseInt(code);
                this.message = Constants.ERROR_CODE_MAP.get(this.code);
            } catch (NumberFormatException e) {
                this.message = code;
                this.code = 1002;
            }
        }
        this.timeStamp = System.currentTimeMillis();
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
     * @param code error code in string
     * @param <T> no use, wildcard is fine
     * @return a failure response with error message
     */
    public static <T> Response<T> fail(String code) {
        return new Response<>(null, 0, code);
    }

    /**
     * Produce a failure response, with an error message and a payload
     * @param code errorCode in String
     * @param payload payload to carry
     * @param <T> payload type
     * @return a failure response with payload and error message
     */
    public static <T> Response<T> fail(String code, T payload) {
        return new Response<>(payload, 0, code);
    }
}
