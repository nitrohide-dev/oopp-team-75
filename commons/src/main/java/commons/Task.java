package commons;

import javax.persistence.*;


@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String title;

    @ManyToOne
    private TaskList taskList;

//    constructors

    public Task() {} // required for Spring, please don't use.

    protected Task(TaskList taskList) {
        this.taskList = taskList;
        this.title = "";
    }

//    getters and setters

    private long getId() {
        return id;
    }

    private void setId(long id) {
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

    private void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

//    actual methods

    public void delete() {
        this.taskList.removeTask(this);
    }

}
