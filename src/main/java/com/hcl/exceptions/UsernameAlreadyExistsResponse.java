package com.hcl.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//the JSON object that will be sent as response when user tries to create an account
//with a username that already exists
public class UsernameAlreadyExistsResponse {
    private String username;

}
