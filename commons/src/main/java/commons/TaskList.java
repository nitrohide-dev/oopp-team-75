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
    @OneToMany(mappedBy = "taskList", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @OrderColumn
    private List<Task> tasks;

    @JsonBackReference
    @ManyToOne
    private Board board;

//    constructors

    public TaskList() {} // for object mappers, please don't use.

    public TaskList(Board board, String title) {
        this.board = board;
        this.tasks = new ArrayList<>();
        this.title = title;
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
     * Removes {@code task} from this taskList and sets its parent to null.
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
     * Removes this taskList from its board. Shorthand method for: {@code
     * taskList.getBoard().removeTaskList(taskList)}. If the taskList does not
     * have a board (i.e. it is already detached), the method does nothing.
     */
    public void detach() {
        if (board == null) return;
        this.board.removeTaskList(this);
    }

    /**
     * Detaches {@code task} and inserts it at {@code index} in this taskList.
     * @param index
     * @param task
     */
    public void insertTask(int index, Task task) {
        if (task == null)
            throw new IllegalArgumentException("Task cannot be null");
        task.detach();
        tasks.add(index, task);
        task.setTaskList(this);
    }

    /**
     * Detaches {@code task1} and inserts it before {@code task2} in this taskList.
     * @param task1
     * @param task2
     */
    public void insertTask(Task task1, Task task2) {
        int index = tasks.indexOf(task2);
        if (index == -1)
            throw new IllegalArgumentException("Task2 does not exist in the taskList.");
        insertTask(index, task1);
    }
}
