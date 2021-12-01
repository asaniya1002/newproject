package com.hcl.security;

import com.google.gson.Gson;
import com.hcl.exceptions.InvalidLoginResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//interface that provides impl for method called commence
//this is called whenever an exception is thrown bcuz a user is trying
//to access a resource that requires authentication
//basically customizing our exception for the frontend

//This class will extend Spring's AuthenticationEntryPoint class and override its method commence.
// It rejects every unauthenticated request and send error code 401
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    ///this is is how we want to display..how we re going to manage what the user sees
    //whenever they try to access a resource without being authenticated
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        InvalidLoginResponse loginResponse = new InvalidLoginResponse();
        String jsonLoginResponse = new Gson().toJson(loginResponse);

        //again this response is what the serves says when user tries to access resources that require authentication
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().print(jsonLoginResponse);


    }
}
