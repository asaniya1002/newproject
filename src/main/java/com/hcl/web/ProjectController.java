package com.hcl.web;

import com.hcl.domain.Project;
import com.hcl.services.MapErrorValidationService;
import com.hcl.services.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Api(value = "Swagger2RestController", description = "REST APIs related to Project Entity!!")
@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapErrorValidationService mapErrorValidationService;

    //This annotation is used to describe the exposed REST API.
    // It describes an operation or typically a HTTP method against a specific path.
    @ApiOperation(value = "Create a New Project", response = ResponseEntity.class, tags = "postProject")
    @PostMapping("")
    //@Valid - to ensure we are passing a valid body of type Project
    //BindingResult - invokes validator on object; determines whether or not object has errors
    //prinicpal is the person who is loggefd in, the user thats in the claims and is the owner of the token
    //our filter is going to fetch the user by using the id that we put in the claims and then put
    //that into the securitycontext, and thats why we are able to get the prinicapl everytim someone comes
    //in with a vlid token
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = mapErrorValidationService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        //now we will be passing principal.getName() to set up the relationship between the Project and the User
        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Retrieve a Project by its Project Identifier", response = ResponseEntity.class, tags = "getProject")
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {
        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @ApiOperation(value = "Retrieve List of All Projects", response = Iterable.class, tags = "getProjects")
    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @ApiOperation(value = "Delete a Project with given Project Identifier", response = ResponseEntity.class, tags = "deleteProject")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<String>("Project with ID: '" + projectId + "' was deleted", HttpStatus.OK);

    }

}
