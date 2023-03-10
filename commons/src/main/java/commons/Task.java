package commons;

import javax.persistence.*;
import java.util.Objects;


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

//    equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && title.equals(task.title) && taskList.equals(task.taskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, taskList);
    }

//    actual methods

    public void delete() {
        this.taskList.removeTask(this);
    }

}
