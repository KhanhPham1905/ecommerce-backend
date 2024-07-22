package com.ghtk.ecommercewebsite.exceptions;

public class UserAlreadyExistedException extends RuntimeException {

    public UserAlreadyExistedException(String email) {
        super("User with email " + email + " already existed.");
    }

}
