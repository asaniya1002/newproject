package com.hcl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data //including toString and getters and setters
public class ProjectTask {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, unique = true)//gonna be auto-generated anyway so user cant update it
    private String projectSequence; //projectIdentifier + PTSequence that's going to appear on Task [TEST1-4]

    @NotBlank (message = "Please include a task summary")
    private String summary;
    private String acceptanceCriteria;
    private String status;
    private Integer priority;
    @JsonFormat(pattern="yyyy-mm-dd") //need to have this here...wasnt able to update the due date in project task
    private Date dueDate;

    //Many-to-One with Backlog
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "backlogId", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;

    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    @Column(updatable = false)
    private String projectIdentifier;

    @JsonFormat(pattern="yyyy-mm-dd")
    private Date createdAt; //not showing it on the screen, but just to have a history of it

    @JsonFormat(pattern="yyyy-mm-dd")
    private Date updatedAt; //not showing it on the screen, but just to have a history of it

    @PrePersist
    protected void onCreate() { this.createdAt = new Date(); }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }




}
