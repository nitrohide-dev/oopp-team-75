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
    private long id;

    @Column(nullable=false, length=MAX_TITLE_LENGTH)
    private String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn
    private List<Task> tasks;

    @JsonBackReference
    @ManyToOne
    private Board board;

//    constructors

    public TaskList() {} // for object mappers, please don't use.

    public TaskList(Board board) {
        this(board, "", new ArrayList());
    }

    public TaskList(Board board, String title, List tasks) {
        this.board = board;
        this.title = title;
        this.tasks = tasks;
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

    /**
     * Checks for equality in all attributes except the parent board, since
     * that would introduce infinite recursion.
     * @param o the object to compare with
     * @return true if this and o are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskList)) return false;

        TaskList taskList = (TaskList) o;

        if (id != taskList.id) return false;
        if (!Objects.equals(title, taskList.title)) return false;
        return Objects.equals(tasks, taskList.tasks);
    }

    /**
     * Generates a hashcode using all attributes except the parent board, since
     * that would introduce infinite recursion.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (tasks != null ? tasks.hashCode() : 0);
        return result;
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
     * Removes {@code task} from this taskList and sets its parent to null.
     * @param task
     */
    public void removeTask(Task task) {
        if (task == null)
            throw new NullPointerException("Task cannot be null");
        if (!this.tasks.remove(task))
            throw new IllegalArgumentException("Task not in TaskList");
        task.setTaskList(null);
    }

    /**
     * Inserts {@code task} at {@code index} in this taskList.
     * @param index
     * @param task
     */
    public void insertTask(int index, Task task) {
        if (task == null)
            throw new NullPointerException("Task cannot be null");
        if (task.getTaskList() != null)
            task.getTaskList().removeTask(task);
        tasks.add(index, task);
        task.setTaskList(this);
    }

    /**
     * Inserts {@code task1} before {@code task2} in this taskList.
     * @param task1
     * @param task2
     */
    public void insertTask(Task task1, Task task2) {
        if (task2 == null)
            throw new NullPointerException("Task2 cannot be null");
        int index = tasks.indexOf(task2);
        if (index == -1)
            throw new IllegalArgumentException("Task2 does not exist in the taskList.");
        insertTask(index, task1);
    }
}
