package commons;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Task {
    @Id
    private Long id;

    private String title;

    public Task(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
