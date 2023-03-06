package commons;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;


@Entity
public class Board {
    @Id
    private Long id;

    @ElementCollection
    private java.util.List<TaskList> taskLists;

//    constructors

    public Board() {
        this.taskLists = new ArrayList<>();
    }

    public Board(java.util.List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

//    setters and getters

    public java.util.List<TaskList> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(java.util.List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    actual methods

    public void addTaskList() {
        this.taskLists.add(new TaskList(this));
    }

    public void removeTaskList(TaskList taskList) {
        if (!this.taskLists.remove(taskList))
            throw new IllegalArgumentException();
    }

}
