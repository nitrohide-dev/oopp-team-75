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
import java.util.Random;

@Entity
public class Board {

    public static final int MAX_KEY_LENGTH = 16;
    public static final char[] KEY_CHARS = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!-._ ").toCharArray();
    public static final int MAX_TITLE_LENGTH = 64;

//    static methods

    /**
     * Checks if a key is less than a certain length, and only contains characters from a predefined set.
     * @param key the key to validate
     * @return boolean value describing the validity of the key
     */
    public static boolean validateKey(String key) {
        if (key == null) return false;
        if (key.length() > MAX_KEY_LENGTH) return false;
        outer: for (char c1 : key.toCharArray()) {
            for (char c2 : KEY_CHARS) {
                if (c1 == c2) continue outer;
            } return false;
        } return true;
    }

    /**
     * Generates a random key of maximum length.
     * @param random a Random object used to generate random characters
     * @return the generated key.
     */
    public static String generateKey(Random random) {
        return generateKey(random, MAX_KEY_LENGTH);
    }

    /**
     * Generates a random key of a specified length.
     * @param random a Random object used to generate random characters
     * @param len the length of the key to generate
     * @return the generated key.
     */
    public static String generateKey(Random random, int len) {
        if (random == null)
            throw new IllegalArgumentException("random cannot be null");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++)
            stringBuilder.append(KEY_CHARS[random.nextInt(KEY_CHARS.length)]);
        return stringBuilder.toString();
    }

//    attributes

    @Id
    @Column(unique=true, nullable=false, length=MAX_KEY_LENGTH)
    private String key;

    @Column(nullable=false, length=MAX_TITLE_LENGTH)
    private String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @OrderColumn
    private List<TaskList> taskLists;

    /**
     * if true the board is only accessible to the users with a key
     * If false it is accessible for everyone
     */
    private boolean locked;

//    constructors

    public Board() {} // for object mappers, please don't use.

    public Board(String title, String key, boolean locked) {
        this.key = key;
        this.title = title;
        this.taskLists = new ArrayList<>();
        this.locked = locked;
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
     * @param listName - the name of the taskList to add to the board
     * @return the created {@code TaskList}.
     */
    public TaskList createTaskList(String listName) {
        TaskList taskList = new TaskList(this, listName);
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
        if (!this.taskLists.remove(taskList))
            throw new IllegalArgumentException("TaskList not in Board");
        taskList.setBoard(null);
    }

    public boolean isLocked(){
        return locked;
    }
}
