package com.maveric.demo.exception;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(String s) {
        super(s);
    }
}
