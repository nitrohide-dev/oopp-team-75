package commons;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Board {

    public static final int MAX_KEY_LENGTH = 16;
    public static final int MAX_TITLE_LENGTH = 64;

    @Id
    @Column(unique=true, nullable=false, length=MAX_KEY_LENGTH)
    public String key;

    @Column(unique=false, nullable=false, length=MAX_TITLE_LENGTH)
    public String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<TaskList> taskLists;

//    constructors

    public Board() {}

    public Board(String key) {
        this.key = key;
        this.title = "";
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

    public TaskList addTaskList() {
        TaskList taskList = new TaskList(this);
        this.taskLists.add(taskList);
        return taskList;
    }

    protected void removeTaskList(TaskList taskList) {
        if (!this.taskLists.remove(taskList))
            throw new IllegalArgumentException();
    }
}
