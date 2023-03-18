package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TaskListTest {

    private Board board;
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        this.board = new Board("abc", "cbd", false);
        this.taskList = board.createTaskList("name");
    }

    @Test
    void getId() {
        taskList.setId(999);
        assertEquals(999, taskList.getId());
    }

    @Test
    void setId() {
        taskList.setId(999);
        assertEquals(999, taskList.getId());
    }

    @Test
    void getTitle() {
        assertEquals("name", taskList.getTitle());
    }

    @Test
    void setTitle() {
        taskList.setTitle("DEF");
        assertEquals("DEF", taskList.getTitle());
    }

    @Test
    void getTasks() {
        assertEquals(new ArrayList<>(), taskList.getTasks());
    }

    @Test
    void setTasks() {
        List<Task> list = new ArrayList<>();
        taskList.setTasks(list);
        assertSame(taskList.getTasks(), list);
    }

    @Test
    void getBoard() {
        assertEquals(board, taskList.getBoard());
    }

    @Test
    void setBoard() {
        Board board2 = new Board("DEF", "thc", false);
        taskList.setBoard(board2);
        assertEquals(board2, taskList.getBoard());
    }

    @Test
    void testEquals() {
        TaskList a = board.createTaskList("a");
        TaskList b = board.createTaskList("a");
        TaskList c = board.createTaskList("c");
        a.setId(123);
        b.setId(123);
        c.setId(456);
        assertEquals(a, b);
        assertEquals(a, a);
        assertNotEquals(a, c);
    }

    @Test
    void testHashCode() {
        TaskList a = board.createTaskList("a");
        TaskList b = board.createTaskList("a");
        TaskList c = board.createTaskList("c");
        a.setId(123);
        b.setId(123);
        c.setId(456);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.hashCode(), a.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void createTask() {
        taskList.createTask();
        taskList.createTask();
        assertEquals(2, taskList.getTasks().size());
    }

    @Test
    void removeTask() {
        taskList.createTask(); // 1
        Task b = taskList.createTask(); // 2
        taskList.createTask(); // 3
        taskList.removeTask(b); // 2
        assertEquals(2, taskList.getTasks().size());
    }

    @Test
    void detach() {
        taskList.detach();
        assertNull(taskList.getBoard());
    }

    @Test
    void insertTask1() {

        TaskList a = new TaskList(board, "name");
        TaskList b = new TaskList(board, "name2");

        Task at = a.createTask();
        b.createTask(); //0
        b.createTask(); //1
        Task bt = b.createTask(); //2

        b.insertTask(2, at);

        assertEquals(0, a.getTasks().size());
        assertEquals(b, at.getTaskList());
        assertEquals(at, b.getTasks().get(2));
        assertEquals(bt, b.getTasks().get(3));
    }

    @Test
    void insertTask2() {

        TaskList a = new TaskList(board, "name");
        TaskList b = new TaskList(board, "name2");

        Task at = a.createTask();
        b.createTask(); //0
        b.createTask(); //1
        Task bt = b.createTask(); //2

        b.insertTask(at, bt);

        assertEquals(0, a.getTasks().size());
        assertEquals(b, at.getTaskList());
        assertEquals(at, b.getTasks().get(2));
        assertEquals(bt, b.getTasks().get(3));

    }
}