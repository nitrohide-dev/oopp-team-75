package commons;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TaskList {

    @Id
    private Long id;

    @ElementCollection
    private List<Task> tasks;

    private Board board;

//    constructors

    public TaskList(Board board) {
        this.board = board;
        this.tasks = new ArrayList<>();
    }

    public TaskList(Board board, List<Task> tasks) {
        this.board = board;
        this.tasks = tasks;
    }

//    setters and getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

//    actual methods

    public void addTask() {
        this.tasks.add(new Task(this));
    }

    public void removeTask(Task task) {
        if (!this.tasks.remove(task))
            throw new IllegalArgumentException();
    }

    public void delete() {
        this.board.removeTaskList(this);
    }

}
