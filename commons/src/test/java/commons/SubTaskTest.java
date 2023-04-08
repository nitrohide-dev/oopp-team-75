package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SubTaskTest {
    private SubTask subTask;

    @BeforeEach
    void setup() {
        subTask = new SubTask();
    }

    @Test
    void constructor1() {
        SubTask subTask = new SubTask();
        assertNull(subTask.getTitle());
        assertNull(subTask.getTask());
    }

    @Test
    void constructor2() {
        Task task = new Task();
        SubTask subTask = new SubTask(task, "1");
        assertEquals("1", subTask.getTitle());
        assertNotNull(subTask.getTask());
    }

    @Test
    void setId() {
        subTask.setId(999);
        assertEquals(999, subTask.getId());
    }

    @Test
    void getTitle() {
        assertNull(subTask.getTitle());
    }

    @Test
    void setTitle() {
        subTask.setTitle("a");
        assertEquals("a", subTask.getTitle());
        subTask.setTitle("b");
        assertEquals("b", subTask.getTitle());
    }

    @Test
    void getTask() {
        assertNull(subTask.getTask());
    }

    @Test
    void setTask() {
        Task task = new Task();
        subTask.setTask(task);
        assertNotNull(subTask.getTask());
        assertEquals(task, subTask.getTask());
    }

    @Test
    void testEquals() {
        Task task = new Task();
        SubTask subTask1 = new SubTask(task, "1");
        SubTask subTask2 = new SubTask(task, "1");
        SubTask subTask3 = new SubTask(task, "2");
        SubTask subTask4 = new SubTask(task, "1");
        subTask4.setId(999);
        subTask1.setId(999);
        assertEquals(subTask1, subTask4);
        assertNotEquals(subTask1, subTask3);
        assertNotEquals(subTask1, subTask2);
    }

    @Test
    void testHashCode() {
        Task task = new Task();
        SubTask subTask1 = new SubTask(task, "1");
        SubTask subTask2 = new SubTask(task, "1");
        SubTask subTask3 = new SubTask(task, "2");
        SubTask subTask4 = new SubTask(task, "1");
        subTask4.setId(999);
        subTask1.setId(999);
        assertEquals(subTask1.hashCode(), subTask4.hashCode());
        assertNotEquals(subTask1.hashCode(), subTask3.hashCode());
        assertNotEquals(subTask1.hashCode(), subTask2.hashCode());
    }
}