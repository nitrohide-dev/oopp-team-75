package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskTest {

    private Board board;
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        this.board = new Board("abc", "cbd", false);
        this.taskList = board.createTaskList("name");
    }

    @Test
    void getId() {
        Task task = taskList.createTask();
        task.setId(999);
        assertEquals(999, task.getId());
    }

    @Test
    void setId() {
        Task task = taskList.createTask();
        task.setId(999);
        assertEquals(999, task.getId());
    }

    @Test
    void getTitle() {
        Task task = taskList.createTask();
        assertEquals("", task.getTitle());
    }

    @Test
    void setTitle() {
        Task task = taskList.createTask();
        task.setTitle("DEF");
        assertEquals("DEF", task.getTitle());
    }

    @Test
    void getDesc() {
        Task task = taskList.createTask();
        assertEquals("", task.getDesc());
    }

    @Test
    void setDesc() {
        Task task = taskList.createTask();
        task.setDesc("DEF");
        assertEquals("DEF", task.getDesc());
    }

    @Test
    void getTaskList() {
        Task task = taskList.createTask();
        assertEquals(taskList, task.getTaskList());
    }

    @Test
    void setTaskList() {
        TaskList taskList2 = board.createTaskList("list2");
        Task task = taskList.createTask();
        task.setTaskList(taskList2);
        assertEquals(taskList2, task.getTaskList());
    }

    @Test
    void testEquals() {
        Task task1 = taskList.createTask();
        Task task2 = taskList.createTask();
        Task task3 = taskList.createTask();
        task1.setId(999);
        task2.setId(999);
        task3.setId(777);
        assertEquals(task1, task1);
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }

    @Test
    void testHashCode() {
        Task task1 = taskList.createTask();
        Task task2 = taskList.createTask();
        Task task3 = taskList.createTask();
        task1.setId(999);
        task2.setId(999);
        task3.setId(777);
        assertEquals(task1.hashCode(), task1.hashCode());
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1.hashCode(), task3.hashCode());
    }

    @Test
    void detach() {
        Task task = taskList.createTask();
        task.detach();
        assertNull(task.getTaskList());
    }
}