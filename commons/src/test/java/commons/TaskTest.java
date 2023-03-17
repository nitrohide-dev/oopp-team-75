package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskTest {

    @Test
    void getId() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        task.setId(999);
        assertEquals(999, task.getId());
    }

    @Test
    void setId() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        task.setId(999);
        assertEquals(999, task.getId());
    }

    @Test
    void getTitle() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        assertEquals("", task.getTitle());
    }

    @Test
    void setTitle() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        task.setTitle("DEF");
        assertEquals("DEF", task.getTitle());
    }

    @Test
    void getDesc() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        assertEquals("", task.getDesc());
    }

    @Test
    void setDesc() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        task.setDesc("DEF");
        assertEquals("DEF", task.getDesc());
    }

    @Test
    void getTaskList() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        assertEquals(taskList, task.getTaskList());
    }

    @Test
    void setTaskList() {
        Board board = new Board("ABC");
        TaskList taskList1 = board.createTaskList();
        TaskList taskList2 = board.createTaskList();
        Task task = taskList1.createTask();
        task.setTaskList(taskList2);
        assertEquals(taskList2, task.getTaskList());
    }

    @Test
    void testEquals() {
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
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
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
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
        Board board = new Board("ABC");
        TaskList taskList = board.createTaskList();
        Task task = taskList.createTask();
        task.detach();
        assertNull(task.getTaskList());
    }
}