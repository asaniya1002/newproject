package com.hcl.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

//what the client is gonna send to the server is a json object containing the username and password
@Data
public class LoginRequest {
    @NotBlank(message = "Username cannot be blank")
    private String username; //has to be same as in User.java

    @NotBlank(message = "Password cannot be blank")
    private String password; //has to be same as in User.java

}
