package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TaskList {

    public static final int MAX_TITLE_LENGTH = 64;

//    attributes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique=true, nullable=false)
    public long id;

    @Column(nullable=false, length=MAX_TITLE_LENGTH)
    public String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "taskList", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @OrderColumn
    public List<Task> tasks;

    @JsonBackReference
    @ManyToOne
    public Board board;

//    constructors

    public TaskList() {} // for object mappers, please don't use.

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

    /**
     * Creates a new empty task, adds it to the end of this taskList and
     * returns it.
     * @return the created task.
     */
    public Task createTask() {
        Task task = new Task(this);
        this.tasks.add(task);
        return task;
    }

    /**
     * Removes a task from this taskList and sets its parent to null.
     * @param task
     */
    public void removeTask(Task task) {
        if (task == null)
            throw new IllegalArgumentException("Task cannot be null");
        if (!this.tasks.remove(task))
            throw new IllegalArgumentException("Task not in TaskList");
        task.setTaskList(null);
    }

    /**
     * Removes this taskList from its board. Shorthand method for
     * <pre>  taskList.getBoard().removeTaskList(taskList)</pre>
     * If the taskList does not have a board (i.e. it is already detached), it
     * does nothing.
     */
    public void detach() {
        if (board == null) return;
        this.board.removeTaskList(this);
    }

    /**
     * Inserts the task at the specified index in this taskList.
     * @param index
     * @param task
     */
    public void insertTask(int index, Task task) {
        tasks.add(index, task);
        task.setTaskList(this);
    }

    /**
     * Inserts task1 before task2 in this taskList.
     * @param task1
     * @param task2
     */
    public void insertTask(Task task1, Task task2) {
        insertTask(tasks.indexOf(task2), task1);
    }

    /**
     * Detaches the task from its parent and inserts it into this taskList at
     * the specified index.
     * @param index
     * @param task
     */
    public void moveTask(int index, Task task) {
        if (task == null)
            throw new IllegalArgumentException("Task cannot be null");
        task.detach();
        insertTask(index, task);
    }

    /**
     * Detaches task1 from its parent and inserts it before task2 in this taskList.
     * @param task1
     * @param task2
     */
    public void moveTask(Task task1, Task task2) {
        if (task1 == null || task2 == null)
            throw new IllegalArgumentException("Tasks cannot be null");
        task1.detach();
        insertTask(task1, task2);
    }
}
