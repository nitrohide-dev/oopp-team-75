package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
public class Task {

    public static final int MAX_TITLE_LENGTH = 256;

//    attributes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique=true, nullable=false)
    @Getter
    @Setter
    private long id;

    @Column(nullable=false, length=MAX_TITLE_LENGTH)
    @Getter
    @Setter
    private String title;
    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private Set<Tag> tags;
    @JsonManagedReference
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Getter
    @Setter
    @OrderColumn
    private List<SubTask> subtasks;

    @Column(nullable=false)
    @Getter
    @Setter
    private String desc;

    @JsonBackReference
    @ManyToOne
    @Getter
    @Setter
    private TaskList taskList;

//    constructors

    public Task() {} // for object mappers, please don't use.

    public Task(TaskList taskList,String name) {
        this(taskList, name, "");
    }

    public Task(TaskList taskList, String title, String desc) {
        this.taskList = taskList;
        this.title = title;
        this.desc = desc;
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
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(tags, task.tags) && Objects.equals(subtasks, task.subtasks) && Objects.equals(desc, task.desc);
    }
    /**
     * Generates a hashcode using all attributes except the parent taskList, since
     * that would introduce infinite recursion.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, tags, subtasks, desc);
    }


}
