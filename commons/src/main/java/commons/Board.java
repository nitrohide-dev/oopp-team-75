package commons;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import commons.models.CreateBoardModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
@Entity

public class Board {

//    attributes

    @Id
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String key;

    @Column(nullable = false)
    @Getter
    @Setter
    private String title;

    @Column(nullable = true)
    @Getter
    @Setter
    private String password;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn
    @Getter
    @Setter
    private List<TaskList> taskLists;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Getter
    @Setter
    private Set<Tag> tags;

//    constructors

    public Board() { } // for object mappers, please don't use.

    public Board(String key) {
        this(key, "", "", new ArrayList<>(),new HashSet<>());
    }

    public Board(CreateBoardModel model) {
        this(model.getKey(), model.getTitle(), model.getPassword(), new ArrayList<>(),new HashSet<>());
    }

    public Board(String key, String title, List<TaskList> taskLists) {
        this(key, title, "", taskLists,new HashSet<>());
    }

    public Board(String key, String title, String password, List<TaskList> taskLists,Set<Tag> tags) {
        this.key = key;
        this.title = title;
        this.taskLists = taskLists;
        this.tags = tags;
        this.password = password;
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
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(key, board.key) && Objects.equals(title, board.title)
                && Objects.equals(password, board.password) && Objects.equals(taskLists, board.taskLists)
                && Objects.equals(tags, board.tags);
    }

    /**
     * Generates a hashcode using all attributes.
     * @return the generated hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, title, password, taskLists, tags);
    }

//    actual methods

    /**
     * Adds the given {@code TaskList}, adds it to the end of this board and
     * returns it.
     * And gives it a random id.
     * @return the created {@code TaskList}.
     */
    public TaskList createTaskList() {
        TaskList list = new TaskList(this);
        this.taskLists.add(list);
        return list;
    }

    /**
     * Creates a new task list with the given id
     * and adds it to the end of this board.
     * @param id id of the task list
     * @return the created task list
     * @throws IllegalArgumentException
     */
    public TaskList createTaskList(long id) throws IllegalArgumentException {
        TaskList list = new TaskList(this);
        list.setId(id);
        if (this.taskLists.contains(list)) {
            throw new IllegalArgumentException("TaskList already exists");
        }
        this.taskLists.add(list);
        return list;
    }

    /**
     * Creates a new task list with the given id and title
     * @param id id of the task list
     * @param title title of the task list
     * @return the created task list
     * @throws IllegalArgumentException
     */
    public TaskList createTaskList(long id, String title) throws IllegalArgumentException {
        TaskList list = new TaskList(this);
        list.setId(id);
        list.setTitle(title);
        if (this.taskLists.contains(list)) {
            throw new IllegalArgumentException("TaskList already exists");
        }
        this.taskLists.add(list);
        return list;
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

    /**
     * Creates a new tag with the given name
     * @param name name of the tag
     * @return the created tag
     */
    public Tag createTag(String name) {
        Tag tag = new Tag(name, this);
        this.tags.add(tag);
        return tag;
    }
}
