package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskListTest {

    @Test
    void getId() {
        Board b = new Board("Germany");
        TaskList tl = b.createTaskList();
        tl.setId(999);
        assertEquals(999, tl.getId());
    }

    @Test
    void setId() {
        Board b = new Board("Germany");
        TaskList tl = b.createTaskList();
        tl.setId(999);
        assertEquals(999, tl.getId());
    }

    @Test
    void getTitle() {
        Board b = new Board("Germany");
        TaskList tl = b.createTaskList();
        assertEquals("", tl.getTitle());
    }

    @Test
    void setTitle() {
        Board b = new Board("Germany");
        TaskList tl = b.createTaskList();
        tl.setTitle("Francois");
        assertEquals("Francois", tl.getTitle());
    }

    @Test
    void getTasks() {
        Board b = new Board("Germany");
        TaskList tl = b.createTaskList();
        assertEquals(new ArrayList<>(), tl.getTasks());
    }

    @Test
    void setTasks() {
        Board b = new Board("Germany");
        TaskList tl = b.createTaskList();
        List list = new ArrayList<>();
        tl.setTasks(list);
        list.add(new Task(tl));
        list.add(new Task(tl));
        assertEquals(list, tl.getTasks());
    }

    @Test
    void getBoard() {
        Board b = new Board("Germany");
        TaskList tl = new TaskList(b);
        assertEquals(b, tl.getBoard());
    }

    @Test
    void setBoard() {
        Board b = new Board("Germany");
        TaskList tl = new TaskList();
        tl.setBoard(b);
        assertEquals(b, tl.getBoard());
    }

    @Test
    void testEquals() {
        TaskList a = new TaskList();
        TaskList b = new TaskList();
        TaskList c = new TaskList();
        a.setId(123);
        b.setId(123);
        c.setId(456);
        assertEquals(a, b);
        assertEquals(a, a);
        assertNotEquals(a, c);
    }

    @Test
    void testHashCode() {
        TaskList a = new TaskList();
        TaskList b = new TaskList();
        TaskList c = new TaskList();
        a.setId(123);
        b.setId(123);
        c.setId(456);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.hashCode(), a.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void createTask() {
    }

    @Test
    void removeTask() {
    }

    @Test
    void detach() {
    }

    @Test
    void insertTask() {
    }

    @Test
    void testInsertTask() {
    }

    @Test
    void moveTask() {
    }

    @Test
    void testMoveTask() {
    }
}