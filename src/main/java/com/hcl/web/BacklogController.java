package com.hcl.web;

import com.hcl.domain.ProjectTask;
import com.hcl.services.MapErrorValidationService;
import com.hcl.services.ProjectTaskService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import javax.validation.Valid;

@Api(value = "Swagger2RestController", description = "REST APIs related to Backlog Entity!!")
@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {
    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    MapErrorValidationService mapErrorValidationService; //for checking constraints like NotBlank, etc.

    @PostMapping("/{backlogId}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask,
                                            BindingResult result, @PathVariable String backlogId, Principal principal) {
        //mapErrorValidation for errors like passing no summary...this will get the error: Please include a task summary
        ResponseEntity<?> errorMap = mapErrorValidationService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask, principal.getName());

        return new ResponseEntity<>(projectTask1, HttpStatus.CREATED);
    }

    @GetMapping("/{backlogId}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlogId, Principal principal) {
        return projectTaskService.findBacklogById(backlogId, principal.getName());
    }

    @GetMapping("/{backlogId}/{ptId}") //localhost:8080/api/backlog/TEST1/TEST1-2
    public ResponseEntity<?> getProjectTask(@PathVariable String backlogId, @PathVariable String ptId, Principal principal) {
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlogId, ptId, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                               BindingResult result, @PathVariable String backlogId,
                                               @PathVariable String ptId, Principal principal) {

        ResponseEntity<?> errorMap = mapErrorValidationService.MapValidationService(result);
        if (errorMap != null) return errorMap;

        ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask, backlogId, ptId, principal.getName());

        return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{backlogId}/{ptId}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlogId, @PathVariable String ptId, Principal principal) {
        projectTaskService.deletePTByProjectSequence(backlogId, ptId, principal.getName());
        return new ResponseEntity<String>("Project Task " + ptId + " was deleted successfully", HttpStatus.OK);
    }

}
