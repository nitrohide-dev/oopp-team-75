package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TaskList {

    public static final int MAX_TITLE_LENGTH = 64;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique=true, nullable=false)
    private long id;

    @Column(unique=false, nullable=false, length=MAX_TITLE_LENGTH)
    private String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Task> tasks;

    @JsonBackReference
    @ManyToOne
    private Board board;

//    constructors

    public TaskList() {}

    public TaskList(Board board) {
        this.board = board;
        this.tasks = new ArrayList<>();
        this.title = "";
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

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

//    equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList taskList = (TaskList) o;
        return id == taskList.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
