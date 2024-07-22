package com.ghtk.ecommercewebsite.exceptions;

public class SellerAlreadyExistedException extends RuntimeException {

    public SellerAlreadyExistedException(String email) {
        super("Seller with email " + email + " already existed.");
    }

}
