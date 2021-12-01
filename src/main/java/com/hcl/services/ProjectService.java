package com.hcl.services;

import com.hcl.domain.Backlog;
import com.hcl.domain.Project;
import com.hcl.domain.User;
import com.hcl.exceptions.ProjectIdException;
import com.hcl.exceptions.ProjectNotFoundException;
import com.hcl.repositories.BacklogRepository;
import com.hcl.repositories.ProjectRepository;
import com.hcl.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    //for both inserting (/creating) and updating a project
    public Project saveOrUpdateProject(Project project, String username) {
        if(project.getId() != null) //wanna update the project as opposed to creating one
        {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject !=null &&(!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
            }
        }

        //Logic
        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            //the way we want this to work is that whenever we create a Project, a Backlog is created
            //so check the id in the DB...if null, means project is a new project yet to be inserted
            if (project.getId() == null) { //when creating a new project
                Backlog backlog = new Backlog();
                //setting the relationship
                project.setBacklog(backlog);
                backlog.setProject(project);
                //same projectIdentifier for a particular project's respective backlog
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            } else //when updating the project, so that backlog isn't null
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));


            return projectRepository.save(project);

        } catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if (project == null)
            throw new ProjectIdException("Project ID '" + projectId + "' does not exist");

        if(!project.getProjectLeader().equals(username))
            throw new ProjectNotFoundException("Project not found in your account!");


        return project;

    }

    public Iterable<Project> findAllProjects(String username) {
        //Iterable returns a JSON object that has all the objects within the list
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {

        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }

}
