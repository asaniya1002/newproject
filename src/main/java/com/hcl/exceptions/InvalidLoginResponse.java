package com.hcl.exceptions;

import lombok.Data;

//json object that we wanna return when user tries to access resources without authentication
@Data
public class InvalidLoginResponse {
    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid Username";
        this.password = "Invalid Password";
    }
}
