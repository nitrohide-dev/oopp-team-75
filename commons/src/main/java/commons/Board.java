package commons;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Board {

//    static attributes

//    public static final int MAX_KEY_LENGTH = 16;
//    public static final char[] KEY_CHARS = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!-._ ").toCharArray();
//    public static final HashSet<Character> KEY_CHARS_SET = new HashSet(Arrays.asList(KEY_CHARS));
//    public static final int MAX_TITLE_LENGTH = 64;
//    public static final int MAX_PASSWORD_LENGTH = 64;

//    attributes

    @Id
    @Column(nullable=false, unique=true)
    private String key;

    @Column(nullable=false)
    private String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn
    private List<TaskList> taskLists;

    @Column(nullable=false)
    private String password;

//    constructors

    public Board() {} // for object mappers, please don't use.


    public Board(String key, String title, String password) {
        this.key = key;
        this.title = title;
        this.password = password;
        this.taskLists = new ArrayList<>();
    }

//    getters and setters

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

//    equals and hashcode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(key, board.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

//    actual methods

    /**
     * Adds the given {@code TaskList}, adds it to the end of this board and
     * returns it.
     * @return the created {@code TaskList}.
     */
    public TaskList createTaskList() {
        TaskList taskList = new TaskList(this);
        this.taskLists.add(taskList);
        return taskList;
    }

    /**
     * Removes {@code taskList} from this board and sets its parent to null.
     * @param taskList
     */
    public void removeTaskList(TaskList taskList) {
        if (taskList == null)
            throw new IllegalArgumentException("TaskList cannot be null");
        this.taskLists.remove(taskList);
        taskList.setBoard(null);
    }
}
