package com.team2.mosoo_backend.user.exception;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException(String message) {super(message);}
}
