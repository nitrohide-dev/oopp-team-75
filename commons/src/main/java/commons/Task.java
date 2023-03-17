package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.util.Objects;


@Entity
public class Task {

    public static final int MAX_TITLE_LENGTH = 256;

//    attributes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique=true, nullable=false)
    private long id;

    @Column(nullable=false, length=MAX_TITLE_LENGTH)
    private String title;

    @Column(nullable=false)
    private String desc;

    @JsonBackReference
    @ManyToOne
    private TaskList taskList;

//    constructors

    public Task() {} // for object mappers, please don't use.

    protected Task(TaskList taskList) {
        this.taskList = taskList;
        this.title = "";
        this.desc = "";
    }

//    getters and setters

    public long getId() {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public void detach() {
        this.taskList.removeTask(this);
    }

}
