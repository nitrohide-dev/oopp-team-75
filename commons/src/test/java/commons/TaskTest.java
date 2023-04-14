package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Task task = new Task(new TaskList(),"");
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

    @Test
    void getTags() {
        assertEquals( new HashSet<>(), task.getTags());
    }

    @Test
    void setTags() {
        Set<Tag>list = new HashSet<>();
        task.setTags(list);
        assertEquals(0, task.getTags().size());
        Tag tag = new Tag();
        list.add(tag);
        task.setTags(list);
        assertEquals(1, task.getTags().size());
    }

    @Test
    void getSubTasks() {
        assertEquals( new ArrayList<>(), task.getSubtasks());
        assertNotEquals( new HashSet<>(), task.getSubtasks());
    }

    @Test
    void setSubTasks() {
        List<SubTask> list = new ArrayList<>();
        task.setSubtasks(list);
        assertEquals(0, task.getSubtasks().size());
        SubTask subTask = new SubTask();
        list.add(subTask);
        task.setSubtasks(list);
        assertEquals(1, task.getSubtasks().size());
    }

    @Test
    void createSubTask() {
        task.setSubtasks(new ArrayList<>());
        task.createSubTask("");
        assertEquals(1, task.getSubtasks().size());
    }

    @Test
    void setTag() {
        assertEquals( new HashSet<>(), task.getTags());
        Tag tag = new Tag();
        tag.setTasks(new HashSet<>());
        task.setTags(new HashSet<>());
        assertEquals(0, task.getTags().size());
        Board board = new Board("1", "a", "", new ArrayList<>(), new HashSet<>());
        TaskList taskList = new TaskList();
        taskList.setBoard(board);
        taskList.setBoard(board);
        Set tags = new HashSet<>();
        tags.add(tag);
        board.setTags(tags);
        task.setTaskList(taskList);
        tag.setBoard(board);
        task.addTag(tag);
        assertEquals(1, task.getTags().size());
        task.addTag(tag); //should not add
        assertEquals(1, task.getTags().size());
        //help my brain melts
    }

    @Test
    void setTagException() {
        assertThrows(IllegalArgumentException.class, () -> task.addTag(null));
    }

    @Test
    void constructor5() {
        Task task = new Task(new TaskList(), "a", "a", new HashSet<>(), new ArrayList<>());
        assertNotNull(task.getId());
        assertEquals("a", task.getDesc());
        assertEquals("a", task.getTitle());
        assertEquals(new TaskList(), task.getTaskList());
        assertEquals(0, task.getTags().size());
        assertEquals(0, task.getSubtasks().size());
    }

    @Test
    void constructor4() {
        Task task = new Task(new TaskList(), "a", "a", new HashSet<>());
        assertNotNull(task.getId());
        assertEquals("a", task.getDesc());
        assertEquals("a", task.getTitle());
        assertEquals(new TaskList(), task.getTaskList());
        assertEquals(0, task.getTags().size());
    }

    @Test
    void removeSubTask() {
        task.setSubtasks(new ArrayList<>());
        SubTask subTask = new SubTask();
        task.getSubtasks().add(subTask);
        assertEquals(1, task.getSubtasks().size());
        task.removeSubTask(subTask);
        assertEquals(0, task.getSubtasks().size());
        assertThrows(NullPointerException.class, () -> task.removeSubTask(null));
        assertThrows(IllegalArgumentException.class, () -> task.removeSubTask(new SubTask()));
    }

    @Test
    void addTagException() {
        assertThrows(IllegalArgumentException.class, () -> task.addTag(null));
        assertEquals( new HashSet<>(), task.getTags());
        Tag tag = new Tag();
        tag.setTasks(new HashSet<>());
        task.setTags(new HashSet<>());
        assertEquals(0, task.getTags().size());
        Board board = new Board("1", "a", "", new ArrayList<>(), new HashSet<>());
        TaskList taskList = new TaskList();
        taskList.setBoard(board);
        taskList.setBoard(board);
        Set tags = new HashSet<>();
        tags.add(tag);
        board.setTags(tags);
        task.setTaskList(taskList);
        Board board1 = new Board("2", "b", "", new ArrayList<>(), new HashSet<>());
        tag.setBoard(board1);
        assertThrows(IllegalArgumentException.class, () -> task.addTag(tag));
        assertEquals(0, task.getTags().size());
    }

    @Test
    void removeTag() {
        assertEquals( new HashSet<>(), task.getTags());
        Tag tag = new Tag();
        Set tasks = new HashSet<>();
        tasks.add(task);
        tag.setTasks(tasks);
        task.setTags(new HashSet<>());
        assertEquals(0, task.getTags().size());
        Board board = new Board("1", "a", "", new ArrayList<>(), new HashSet<>());
        TaskList taskList = new TaskList();
        taskList.setBoard(board);
        taskList.setBoard(board);
        Set tags = new HashSet<>();
        tags.add(tag);
        board.setTags(tags);
        task.setTaskList(taskList);
        tag.setBoard(board);
        task.addTag(tag);
        assertEquals(1, task.getTags().size());
        task.removeTag(tag);
        assertEquals(0, task.getTags().size());
        assertThrows(IllegalArgumentException.class, () -> task.removeTag(null));
        assertThrows(IllegalArgumentException.class, () -> task.removeTag(new Tag()));
        assertThrows(IllegalArgumentException.class, () -> tag.removeTask(null));
        assertThrows(IllegalArgumentException.class, () -> tag.removeTask(new Task()));
    }
}