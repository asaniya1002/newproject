package com.hcl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Backlog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id; //id in DB

    private Integer PTSequence = 0; //sequence of project tasks within a particular backlog
    private String projectIdentifier; //Backlog is gonna share the same Identifier as the Project

    //One-to-One with Project
    //FetchType.Lazy basically doesn't load the relationships unless explicitly requested
    //while EAGER loads them all...we're just doing EAGER so its faster
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId", nullable = false) //will contain projectId as foreign key / reference to Project
    @JsonIgnore //breaks the infinite recursion problem when creating a project in DB
    private Project project;

    //One-to-Many with the Project Task
    //cascade.REFRESH: can delete a project task that belongs (is a child) to a Backlog
    //object and this will just refresh the Backlog object and tells it this project task
    //no longer exists
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "backlog", orphanRemoval = true)
    private List<ProjectTask> projectTasks = new ArrayList<>();

    public List<ProjectTask> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(List<ProjectTask> projectTasks) {
        this.projectTasks = projectTasks;
    }
}
