package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;


@Entity
public class Task {

    public static final int MAX_TITLE_LENGTH = 256;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique=true, nullable=false)
    public long id;

    @Column(unique=false, nullable=false, length=MAX_TITLE_LENGTH)
    public String title;

    @JsonBackReference
    @ManyToOne
    public TaskList taskList;

//    constructors

    public Task() {} // required for Spring, please don't use.

    public Task(TaskList taskList) {
        this.taskList = taskList;
        this.title = "";
    }

//    getters and setters

    private long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskList getTaskList() {
        return taskList;
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

//    equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

//    actual methods

    public void delete() {
        this.taskList.removeTask(this);
    }

}
