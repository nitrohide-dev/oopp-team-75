package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TaskList {

    public static final int MAX_TITLE_LENGTH = 64;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique=true, nullable=false)
    public long id;

    @Column(nullable=false, length=MAX_TITLE_LENGTH)
    public String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public List<Task> tasks;

    @JsonBackReference
    @ManyToOne
    public Board board;

//    constructors

    public TaskList() {}

    public TaskList(Board board) {
        this.board = board;
        this.tasks = new ArrayList<>();
        this.title = "";
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
