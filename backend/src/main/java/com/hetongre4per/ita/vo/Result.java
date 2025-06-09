package com.hetongre4per.ita.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    
    public boolean success;
    
    public String message;
    
    public int code;
    
    public T data;
    
    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }
    
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage(message);
        return result;
    }
    
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setData(data);
        return result;
    }
    
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setData(data);
        result.setMessage(message);
        return result;
    }
    
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
    
}
