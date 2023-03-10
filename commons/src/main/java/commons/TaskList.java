package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    protected TaskList(Board board) {
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

//    equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList taskList = (TaskList) o;
        return id == taskList.id && title.equals(taskList.title) && tasks.equals(taskList.tasks) && board.equals(taskList.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, tasks, board);
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
