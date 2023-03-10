package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String title;

    @OneToMany(mappedBy = "taskList")
    private List<Task> tasks;

    @ManyToOne
    private Board board;

//    constructors

    public TaskList() {} // required for Spring, please don't use.

    TaskList(Board board) {
        this.title = "";
        this.board = board;
        this.tasks = new ArrayList<>();
    }

//    getters and setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    private void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Board getBoard() {
        return board;
    }

    private void setBoard(Board board) {
        this.board = board;
    }

//    actual methods

    public Task addTask() {
        Task task = new Task(this);
        this.tasks.add(task);
        return task;
    }

    protected void removeTask(Task task) {
        if (!this.tasks.remove(task))
            throw new IllegalArgumentException();
    }

    public void delete() {
        this.board.removeTaskList(this);
    }

}
