package com.hcl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //database id

    @NotBlank(message = "Please provide a project name")
    private String projectName;

    @NotBlank(message = "Please provide a project identifier")
    @Size(min = 4, max = 5, message = "Please use 4 to 5 characters") //emulating Jira
    @Column(updatable = false, unique = true) //at database layer/level...happens after mapErrorValidation
    private String projectIdentifier; //custom identifier known to user

    @NotBlank(message = "Please provide a project description")
    private String description;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date endDate;

    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(updatable = false)
    //this makes sure even if a user is wanting to Update Project Info, they wont be able to update the createdAt attribute
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date updatedAt;

    //fetch: when we load a project object, the backlog information is readily available
    //CascadeType.ALL: project is the OWNING side of the relationship...
    //meaning if you delete a project, everything that's a child to the project (Backlog and ProjectTask)
    //then everything should go away
    //project is the parent in this bidirectional one-to-one relationship
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project")
    @JsonIgnore //putting this here bcuz if not, then whenever we want to get a project by id
    //update it, the server is gonna keep updating each change in the backlog as well
    //and show it in the Project object...very inefficient...we do not need the backlog
    //data/info every time we would like to update the project...
    //any change happened on Backlog must cascade to Project
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    private String projectLeader;

    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    @PrePersist
    protected void onCreate() //every time we create a new object, assigns a new date
    {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

}
