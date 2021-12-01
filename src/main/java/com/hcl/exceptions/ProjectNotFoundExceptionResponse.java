package com.hcl.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectNotFoundExceptionResponse {
    private String projectNotFound;
}
   /*
        {
            projectNotFound: "Project was not found"
        }
        */

