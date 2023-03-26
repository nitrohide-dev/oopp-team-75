package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    void setup() {
        taskList = new TaskList(new Board("a", "a", "a", null), "a", new ArrayList<>());
    }

    @Test
    void constructor1() {
        TaskList taskList = new TaskList();
        assertNotNull(taskList.getId());
        assertNull(taskList.getTasks());
        assertNull(taskList.getTitle());
        assertNull(taskList.getBoard());
    }

    @Test
    void constructor2() {
        TaskList taskList = new TaskList(new Board());
        assertNotNull(taskList.getId());
        assertNotNull(taskList.getTasks());
        assertNotNull(taskList.getTitle());
        assertEquals(new Board(), taskList.getBoard());
    }

    @Test
    void constructor3() {
        TaskList taskList = new TaskList(new Board(), "a", new ArrayList<>());
        assertNotNull(taskList.getId());
        assertEquals(new ArrayList<>(), taskList.getTasks());
        assertEquals("a", taskList.getTitle());
        assertEquals(new Board(), taskList.getBoard());
    }

    @Test
    void getId() {
        assertNotNull(taskList.getId());
    }

    @Test
    void setId() {
        taskList.setId(999);
        assertEquals(999, taskList.getId());
    }

    @Test
    void getTitle() {
        assertEquals("a", taskList.getTitle());
        assertNotEquals("b", taskList.getTitle());
    }

    @Test
    void setTitle() {
        taskList.setTitle("b");
        assertEquals("b", taskList.getTitle());
        assertNotEquals("a", taskList.getTitle());
    }

    @Test
    void getTasks() {
        List<Task> goodBoyList = new ArrayList<>();
        List<Task> badBoyList = new ArrayList<>();
        badBoyList.add(null);
        assertEquals(goodBoyList, taskList.getTasks());
        assertNotEquals(badBoyList, taskList.getTasks());
    }

    @Test
    void setTasks() {
        List<Task> goodBoyList = new ArrayList<>();
        List<Task> badBoyList = new ArrayList<>();
        goodBoyList.add(null);
        taskList.setTasks(goodBoyList);
        assertEquals(goodBoyList, taskList.getTasks());
        assertNotEquals(badBoyList, taskList.getTasks());
    }

    @Test
    void getBoard() {
        Board goodBoyBoard = new Board("a", "a", "a", null);
        Board badBoyBoard = new Board("b", "a", "a", null);
        assertEquals(goodBoyBoard, taskList.getBoard());
        assertNotEquals(badBoyBoard, taskList.getBoard());
    }

    @Test
    void setBoard() {
        Board badBoyBoard = new Board("a", "a", "a", null);
        Board goodBoyBoard = new Board("b", "a", "a", null);
        taskList.setBoard(goodBoyBoard);
        assertEquals(goodBoyBoard, taskList.getBoard());
        assertNotEquals(badBoyBoard, taskList.getBoard());
    }

    @Test
    void testEquals() {
        TaskList taskList1 = new TaskList(new Board("a", "a", "a", null), "a", new ArrayList<>());
        TaskList taskList2 = new TaskList(new Board("a", "a", "a", null), "a", new ArrayList<>());
        TaskList taskList3 = new TaskList(new Board("a", "a", "a", null), "b", new ArrayList<>());
        List<Task> tasks4 = new ArrayList<>();
        tasks4.add(null);
        TaskList taskList4 = new TaskList(new Board("a", "a", "a", null), "a", tasks4);
        TaskList taskList5 = new TaskList(new Board("b", "a", "a", null), "a", new ArrayList<>());
        assertEquals(taskList1, taskList1);
        assertEquals(taskList1, taskList2);
        assertNotEquals(taskList1, taskList3);
        assertNotEquals(taskList1, taskList4);
        assertEquals(taskList1, taskList5);
        assertNotEquals(null, taskList1);
    }

    @Test
    void testHashCode() {
        TaskList taskList1 = new TaskList(new Board("a", "a", "a", null), "a", new ArrayList<>());
        TaskList taskList2 = new TaskList(new Board("a", "a", "a", null), "a", new ArrayList<>());
        TaskList taskList3 = new TaskList(new Board("a", "a", "a", null), "b", new ArrayList<>());
        List<Task> tasks4 = new ArrayList<>();
        tasks4.add(null);
        TaskList taskList4 = new TaskList(new Board("a", "a", "a", null), "a", tasks4);
        TaskList taskList5 = new TaskList(new Board("b", "a", "a", null), "a", new ArrayList<>());
        assertEquals(taskList1.hashCode(), taskList1.hashCode());
        assertEquals(taskList1.hashCode(), taskList2.hashCode());
        assertNotEquals(taskList1.hashCode(), taskList3.hashCode());
        assertNotEquals(taskList1.hashCode(), taskList4.hashCode());
        assertEquals(taskList1.hashCode(), taskList5.hashCode());
    }

    @Test
    void createTask() {
        Task task1 = taskList.createTask();
        assertEquals(taskList, task1.getTaskList());
        assertEquals(1, taskList.getTasks().size());
        Task task2 = taskList.createTask();
        assertEquals(taskList, task1.getTaskList());
        assertEquals(2, taskList.getTasks().size());
    }

    @Test
    void removeTask() {
        Task task1 = taskList.createTask();
        Task task2 = taskList.createTask();
        taskList.removeTask(task1);
        assertNull(task1.getTaskList());
        assertEquals(1, taskList.getTasks().size());
        taskList.removeTask(task2);
        assertNull(task2.getTaskList());
        assertEquals(0, taskList.getTasks().size());
        assertThrows(NullPointerException.class, () -> taskList.removeTask(null));
        assertThrows(IllegalArgumentException.class, () -> taskList.removeTask(new Task()));
    }

    @Test
    void insertTask() {
        TaskList taskList1 = new TaskList(null, "a", new ArrayList<>());
        Task task11 = taskList1.createTask();
        TaskList taskList2 = new TaskList(null, "a", new ArrayList<>());
        Task task21 = taskList2.createTask();
        Task task22 = taskList2.createTask();
        taskList2.insertTask(1, task11);
        assertEquals(0, taskList1.getTasks().size());
        assertEquals(task21, taskList2.getTasks().get(0));
        assertEquals(task11, taskList2.getTasks().get(1));
        assertEquals(task22, taskList2.getTasks().get(2));
        assertEquals(taskList2, task11.getTaskList());
        assertThrows(NullPointerException.class, () -> taskList.insertTask(0, null));
    }

    @Test
    void insertTask2() {
        TaskList taskList1 = new TaskList(null, "a", new ArrayList<>());
        Task task11 = taskList1.createTask();
        TaskList taskList2 = new TaskList(null, "a", new ArrayList<>());
        Task task21 = taskList2.createTask();
        Task task22 = taskList2.createTask();
        taskList2.insertTask(task11, task22);
        assertEquals(0, taskList1.getTasks().size());
        assertEquals(task21, taskList2.getTasks().get(0));
        assertEquals(task11, taskList2.getTasks().get(1));
        assertEquals(task22, taskList2.getTasks().get(2));
        assertEquals(taskList2, task11.getTaskList());
        assertThrows(NullPointerException.class, () -> taskList.insertTask(new Task(), null));
        assertThrows(IllegalArgumentException.class, () -> taskList.insertTask(new Task(), new Task()));
    }
}