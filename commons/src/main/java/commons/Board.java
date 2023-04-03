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

@SuppressWarnings("SpellCheckingInspection")
@Entity

public class Board {

//    attributes

    @Id
    @Column(nullable=false, unique=true)
    private String key;

    @Column(nullable=false)
    private String title;



    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn(name="taskLists")
    private List<TaskList> taskLists;

//    constructors

    public Board() {} // for object mappers, please don't use.

    public Board(String key) {
        this(key, "",  new ArrayList<>());
    }

    public Board(CreateBoardModel model) {
        this(model.getKey(), model.getTitle(), new ArrayList<>());
    }

    public Board(String key, String title, List<TaskList> taskLists) {
        this.key = key;
        this.title = title;
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
