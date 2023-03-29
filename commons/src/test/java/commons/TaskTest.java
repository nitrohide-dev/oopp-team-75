package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskTest {

    private Task task;

    @BeforeEach
    void setup() {
        task = new Task(new TaskList(null, null, null), "a", "a");
    }

    @Test
    void constructor1() {
        Task task = new Task();
        assertNotNull(task.getId());
        assertNull(task.getDesc());
        assertNull(task.getTitle());
        assertNull(task.getTaskList());
    }

    @Test
    void constructor2() {
        Task task = new Task(new TaskList());
        assertNotNull(task.getId());
        assertNotNull(task.getDesc());
        assertNotNull(task.getTitle());
        assertEquals(new TaskList(), task.getTaskList());
    }

    @Test
    void constructor3() {
        Task task = new Task(new TaskList(), "a", "a");
        assertNotNull(task.getId());
        assertEquals("a", task.getDesc());
        assertEquals("a", task.getTitle());
        assertEquals(new TaskList(), task.getTaskList());
    }

    @Test
    void getId() {
        assertNotNull(task.getId());
    }

    @Test
    void setId() {
        task.setId(999);
        assertEquals(999, task.getId());

    }

    @Test
    void getTitle() {
        assertEquals("a", task.getTitle());
        assertNotEquals("b", task.getTitle());
    }

    @Test
    void setTitle() {
        task.setTitle("b");
        assertEquals("b", task.getTitle());
        assertNotEquals("a", task.getTitle());

    }

    @Test
    void getDesc() {
        assertEquals("a", task.getDesc());
        assertNotEquals("b", task.getDesc());
    }

    @Test
    void setDesc() {
        task.setDesc("b");
        assertEquals("b", task.getDesc());
        assertNotEquals("a", task.getDesc());
    }

    @Test
    void getTaskList() {
        assertEquals(new TaskList(null, null, null), task.getTaskList());
        assertNotEquals(new TaskList(null, "a", null), task.getTaskList());
    }

    @Test
    void setTaskList() {
        task.setTaskList(new TaskList(null, "a", null));
        assertNotEquals(new TaskList(null, null, null), task.getTaskList());
        assertEquals(new TaskList(null, "a", null), task.getTaskList());
    }

    @Test
    void testEquals() {
        Task task1 = new Task(null, "a", "a");
        Task task2 = new Task(null, "a", "a");
        Task task3 = new Task(null, "b", "a");
        Task task4 = new Task(null, "a", "b");
        Task task5 = new Task(new TaskList(null, null, null), "a", "a");
        assertEquals(task1, task1);
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
        assertNotEquals(task1, task4);
        assertEquals(task1, task5);
        assertNotEquals(null, task1);
    }

    @Test
    void testHashCode() {
        Task task1 = new Task(null, "a", "a");
        Task task2 = new Task(null, "a", "a");
        Task task3 = new Task(null, "b", "a");
        Task task4 = new Task(null, "a", "b");
        Task task5 = new Task(new TaskList(null, null, null), "a", "a");
        assertEquals(task1.hashCode(), task1.hashCode());
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1.hashCode(), task3.hashCode());
        assertNotEquals(task1.hashCode(), task4.hashCode());
        assertEquals(task1.hashCode(), task5.hashCode());
    }
}