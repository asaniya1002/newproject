package com.hcl.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

//response to when we have a valid user and we actually want to return back a json web token
//so this will be a response object that we can pass on to our front end so that then Redux
//can keep the token and do requests till the token expires
//part of JWT pre-work...
//This is class is required for creating a response containing the JWT to be returned to the user.
@Data //also want the toString() overridden
@AllArgsConstructor
public class JWTLoginSuccessResponse {
    private boolean success; //your login was successful
    private String token; //and here's your token

}
