package commons;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


@Entity
public class Page {
    @Id
    private Long id;

    private List<Task> tasks;

    public Page(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }
}
