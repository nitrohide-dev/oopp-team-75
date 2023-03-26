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

    public Task(TaskList taskList) {
        this(taskList, "", "");
    }

    public Task(TaskList taskList, String title, String desc) {
        this.taskList = taskList;
        this.title = title;
        this.desc = desc;
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

    /**
     * Checks for equality in all attributes except the parent taskList, since
     * that would introduce infinite recursion.
     * @param o the object to compare with
     * @return true if this and o are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!Objects.equals(title, task.title)) return false;
        return Objects.equals(desc, task.desc);
    }

    /**
     * Generates a hashcode using all attributes except the parent taskList, since
     * that would introduce infinite recursion.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}
