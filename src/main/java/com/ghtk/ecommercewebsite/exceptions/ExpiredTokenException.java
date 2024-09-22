package com.ghtk.ecommercewebsite.exceptions;

public class ExpiredTokenException extends RuntimeException{
    public ExpiredTokenException(String message){super(message);}
}
