package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TaskListTest {

    @Test
    void getId() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        taskList.setId(999);
        assertEquals(999, taskList.getId());
    }

    @Test
    void setId() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        taskList.setId(999);
        assertEquals(999, taskList.getId());
    }

    @Test
    void getTitle() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        assertEquals("", taskList.getTitle());
    }

    @Test
    void setTitle() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        taskList.setTitle("DEF");
        assertEquals("DEF", taskList.getTitle());
    }

    @Test
    void getTasks() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        assertEquals(new ArrayList<>(), taskList.getTasks());
    }

    @Test
    void setTasks() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        List<Task> list = new ArrayList<>();
        taskList.setTasks(list);
        assertSame(taskList.getTasks(), list);
    }

    @Test
    void getBoard() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        assertEquals(board, taskList.getBoard());
    }

    @Test
    void setBoard() {
        Board board1 = new Board("ABC");
        Board board2 = new Board("DEF");
        TaskList taskList = board1.createTaskList();
        taskList.setBoard(board2);
        assertEquals(board2, taskList.getBoard());
    }

    @Test
    void testEquals() {
        Board board = new Board("ABC");
        TaskList a = board.createTaskList();
        TaskList b = board.createTaskList();
        TaskList c = board.createTaskList();
        a.setId(123);
        b.setId(123);
        c.setId(456);
        assertEquals(a, b);
        assertEquals(a, a);
        assertNotEquals(a, c);
    }

    @Test
    void testHashCode() {
        Board board = new Board("ABC");
        TaskList a = board.createTaskList();
        TaskList b = board.createTaskList();
        TaskList c = board.createTaskList();
        a.setId(123);
        b.setId(123);
        c.setId(456);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.hashCode(), a.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void createTask() {
        Board board = new Board("ABC");
        TaskList a = board.createTaskList();
        a.createTask();
        a.createTask();
        assertEquals(2, a.getTasks().size());
    }

    @Test
    void removeTask() {
        Board board = new Board("ABC");
        TaskList a = board.createTaskList();
        a.createTask(); // 1
        Task b = a.createTask(); // 2
        a.createTask(); // 3
        a.removeTask(b); // 2
        assertEquals(2, a.getTasks().size());
    }

    @Test
    void detach() {
        Board b = new Board("Steve");
        TaskList a = b.createTaskList();
        a.detach();
        assertNull(a.getBoard());
    }

    @Test
    void insertTask1() {

        TaskList a = new TaskList(null);
        TaskList b = new TaskList(null);

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

        TaskList a = new TaskList(null);
        TaskList b = new TaskList(null);

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