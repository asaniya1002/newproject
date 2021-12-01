package com.hcl.services;

import com.hcl.domain.Backlog;
import com.hcl.domain.Project;
import com.hcl.domain.ProjectTask;
import com.hcl.exceptions.ProjectNotFoundException;
import com.hcl.repositories.BacklogRepository;
import com.hcl.repositories.ProjectRepository;
import com.hcl.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
        //Handle exception when project is not found

        //all project tasks will be added and belong to a specific project...
        // project should != null and backlog exists
        //all the validation whether or not you are the owner of the project happens at the projectService level already
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
        //backlogRepository.findByProjectIdentifier(projectIdentifier); //find the backlog in which to add project task


        //set the backlog to the project task (to set up relationship)
        projectTask.setBacklog(backlog);

        //projectSequence of Project will be [projectIdentifier + PTSequence (in/from Backlog)]
        Integer backlogSequence = backlog.getPTSequence();

        //update the Backlog PTSequence
        backlogSequence++;
        backlog.setPTSequence(backlogSequence); //very important! or ptsequence is just gonna stay at 1

        //Add sequence to Project Task
        projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //Initial Priority (by default) when priority is null
        //where priority 1 is high and priority 3 is low
        if (projectTask.getPriority() == null || projectTask.getPriority() == 0) //in future we need projectTask.getPriority() == 0 to handle form on UI
            projectTask.setPriority(3);

        //Initial status when status is null
        if (projectTask.getStatus() == "" || projectTask.getStatus() == null)
            projectTask.setStatus("TO_DO");

        return projectTaskRepository.save(projectTask);


    }

    public Iterable<ProjectTask> findBacklogById(String backlogId, String username) {
        projectService.findProjectByIdentifier(backlogId, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
    }

    public ProjectTask findPTByProjectSequence(String backlogId, String ptId, String username) {
        //make sure we are searching on an existing backlog (/project)
        projectService.findProjectByIdentifier(backlogId, username);

        //make sure that our task exists and is actually a part of the project user is working on
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);

        if (projectTask == null)
            throw new ProjectNotFoundException("Project Task '" + ptId + "' does not exist");

        //make sure that the backlog/project id in the path corresponds to the right project (so shouldn't display a task from another project)
        //so case where even though project task is valid AND project id is valid but you are searching in the wrong backlog (/project)
        if (!projectTask.getProjectIdentifier().equals(backlogId))
            throw new ProjectNotFoundException("Project Task '" + ptId + "' does not exist in project '" + backlogId + "'");

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId, String username) {
        //just gonna call the method from above bcuz we need same validation flags
        ProjectTask projectTask = findPTByProjectSequence(backlogId, ptId, username);

        //update project task
        projectTask = updatedTask;

        //save and update
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlogId, String ptId, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlogId, ptId, username);

        projectTaskRepository.delete(projectTask);
    }
}
