package commons;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Board {
    @Id
    private Long id;

    @ElementCollection
    private List<TaskList> taskLists;

//    constructors

    public Board() {
        this.taskLists = new ArrayList<>();
    }

    public Board(List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

//    setters and getters

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(List<TaskList> taskLists) {
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
