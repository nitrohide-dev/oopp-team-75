package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.Set;
@Entity
public class Tag {
    public static final int MAX_TITLE_LENGTH = 256;
    // attributes

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique=true, nullable=false)
    private long id;

    @Getter
    @Setter
    @Column(nullable=false, length=MAX_TITLE_LENGTH)
    private String title;
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private Set<Task> tasks;

    @JsonBackReference
    @ManyToOne
    @Getter
    @Setter
    private Board board;

    public Tag(String title) {
        this.title = title;
    }

    public Tag() {} // for JPA

    // equals and hashcode

    /**
     * Checks for equality in all attributes except the parents set "tasks" , since
     * that would introduce infinite recursion.
     * @param o the object to compare with
     * @return true if this and o are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return id == tag.id && Objects.equals(title, tag.title);
    }
    /**
     * Generates a hashcode using all attributes except the parents set "Tasks", since
     * that would introduce infinite recursion.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    public boolean isChildOfTask(long task_id) {
        for (Task task : tasks) {
            if (task.getId() == task_id) {
                return true;
            }
            else continue;
        }
        return false;
    }

    public boolean isChildOfBoard(String board_key) {
        return board.getKey() == board_key;
    }

    public static Tag createTag(String title) {
        Tag tag = new Tag(title);
        return tag;
    }

    public static Tag createTag(String title, long id) {
        Tag tag = new Tag(title);
        tag.setId(id);
        return tag;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        if (!tasks.contains(task)){
            throw new IllegalArgumentException("Task does not contain this tag");
        }
        tasks.remove(task);
    }
}
