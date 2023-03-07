package commons;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Task {
    @Id
    private Long id;

    private String title;

    private TaskList taskList;

//    constructors

    public Task(TaskList taskList) {
        this.taskList = taskList;
        this.title = "";
    }

    public Task(TaskList taskList, String title) {
        this.taskList = taskList;
        this.title = title;
    }

//    setters and getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

//    actual methods

    public void delete() {
        this.taskList.removeTask(this);
    }

}
