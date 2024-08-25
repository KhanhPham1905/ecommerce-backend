package com.ghtk.ecommercewebsite.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangePassword(
        @JsonProperty("password") String password,
        @JsonProperty("repeat_password") String repeatPassword) {
}
