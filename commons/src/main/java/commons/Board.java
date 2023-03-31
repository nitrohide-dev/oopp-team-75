package commons;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("SpellCheckingInspection")
@Entity

public class Board {

//    attributes

    @Id
    @Column(nullable=false, unique=true)
    private java.lang.String key;

    @Column(nullable=false)
    private String title;

    @Column(nullable=false)
    private long password;  //if password is 0, board is unlocked


    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn(name="taskLists")
    private List<TaskList> taskLists;

//    constructors

    public Board() {} // for object mappers, please don't use.

    public Board(String key) {
        this(key, "", 0L, new ArrayList<>());
    }

    public Board(CreateBoardModel model) {
        this(model.getKey(), model.getTitle(), model.getPassword(), new ArrayList<>());
    }

    public Board(String title, String key, List<TaskList> taskLists) {
        this(key,title,0,taskLists);
    }

    public Board(String key, String title, long password, List<TaskList> taskLists) {
        this.key = key;
        this.title = title;
        this.password = password;
        this.taskLists = taskLists;
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

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public List<TaskList> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(List<TaskList> taskLists) {
        this.taskLists = taskLists;
    }

//    equals and hashcode

    /**
     * Checks for equality in all attributes.
     * @param o the object to compare with
     * @return true if this and o are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        if (!Objects.equals(key, board.key)) return false;
        if (!Objects.equals(title, board.title)) return false;
        if (!Objects.equals(password, board.password)) return false;
        return Objects.equals(taskLists, board.taskLists);
    }

    /**
     * Generates a hashcode using all attributes.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int)( password);
        result = 31 * result + (taskLists != null ? taskLists.hashCode() : 0);
        return result;
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
     * @param taskList to be deleted tasklist
     */
    public void removeTaskList(TaskList taskList) {
        if (taskList == null)
            throw new IllegalArgumentException("TaskList cannot be null");
        this.taskLists.remove(taskList);
        taskList.setBoard(null);
    }
}
