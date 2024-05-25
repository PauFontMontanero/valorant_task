package com.valorant.dbtestutils.exceptions;

public class DbAssertionException extends RuntimeException{

    public DbAssertionException(Exception e) {
        super(e);
    }

    public DbAssertionException(String message, Exception e) {
        super(message, e);
    }
}
