package commons;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Board {

    @Id
    private String key;

    @Column
    @OneToMany(mappedBy = "board")
    private List<TaskList> taskLists;

//    constructors

    public Board() { // required for Spring, please don't use.
        this.key = "";
        this.taskLists = new ArrayList<>();
    }

    public Board(String key) {
        this.key = key;
        this.taskLists = new ArrayList<>();
    }

//    getters and setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    private void setTaskLists(List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

//    actual methods

    public TaskList addTaskList() {
        TaskList taskList = new TaskList(this);
        this.taskLists.add(taskList);
        return taskList;
    }

    protected void removeTaskList(TaskList taskList) {
        if (!this.taskLists.remove(taskList))
            throw new IllegalArgumentException();
    }
}
