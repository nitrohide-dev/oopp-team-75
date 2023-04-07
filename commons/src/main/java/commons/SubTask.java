package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;
@Entity
public class SubTask {
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

    @JsonBackReference
    @ManyToOne
    @Getter
    @Setter
    private Task task;

    // constructor

    public SubTask(String title) {
        this.title = title;
    }

    public SubTask() {} // for JPA

    // equals and hashcode

    /**
     * Checks for equality in all attributes except the parent "task" , since
     * that would introduce infinite recursion.
     * @param o the object to compare with
     * @return true if this and o are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return id == subTask.id && Objects.equals(title, subTask.title);
    }

    /**
     * Generates a hashcode using all attributes except the parent "task", since
     * that would introduce infinite recursion.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
