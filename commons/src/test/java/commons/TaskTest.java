package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskTest {

    private Board board;
    private TaskList taskList;
    private Task task;

    @BeforeEach
    void setUp() {
        this.board = new Board("abc", "cbd", false);
        this.taskList = board.createTaskList("name");
        this.task = taskList.createTask("task");
    }

    @Test
    void getId() {
        task.setId(999);
        assertEquals(999, task.getId());
    }

    @Test
    void setId() {
        task.setId(999);
        assertEquals(999, task.getId());
    }

    @Test
    void getTitle() {
        assertEquals("task", task.getTitle());
    }

    @Test
    void setTitle() {
        task.setTitle("DEF");
        assertEquals("DEF", task.getTitle());
    }

    @Test
    void getDesc() {
        assertEquals("", task.getDesc());
    }

    @Test
    void setDesc() {
        task.setDesc("DEF");
        assertEquals("DEF", task.getDesc());
    }

    @Test
    void getTaskList() {
        assertEquals(taskList, task.getTaskList());
    }

    @Test
    void setTaskList() {
        TaskList taskList2 = board.createTaskList("list2");
        task.setTaskList(taskList2);
        assertEquals(taskList2, task.getTaskList());
    }

    @Test
    void testEquals() {
        Task task1 = taskList.createTask("task 1");
        Task task2 = taskList.createTask("task 1");
        Task task3 = taskList.createTask("task 3");
        task1.setId(999);
        task2.setId(999);
        task3.setId(777);
        assertEquals(task1, task1);
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @Test
    void testHashCode() {
        Task task1 = taskList.createTask("task 1");
        Task task2 = taskList.createTask("task 1");
        Task task3 = taskList.createTask("task 3");
        task1.setId(999);
        task2.setId(999);
        task3.setId(777);
        assertEquals(task1.hashCode(), task1.hashCode());
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1.hashCode(), task3.hashCode());
    }

    @Test
    void detach() {
        task.detach();
        assertNull(task.getTaskList());
    }
}