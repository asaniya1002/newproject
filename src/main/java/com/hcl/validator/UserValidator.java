package com.hcl.validator;

import com.hcl.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
//Spring features a Validator interface that you can use to validate objects.
// The Validator interface works using an Errors object so that while validating,
// validators can report validation failures to the Errors object.

//this is mainyl created to ensure password and confirmPassword are equal/same
public class UserValidator implements Validator {

    //Can this Validator validate instances of the supplied Class?
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        User user = (User) object;

        if(user.getPassword().length() <6){
            errors.rejectValue("password","Length", "Password must be at least 6 characters");
        }

        if(!user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("confirmPassword","Match", "Passwords must match");

        }

        //confirmPassword



    }

}
