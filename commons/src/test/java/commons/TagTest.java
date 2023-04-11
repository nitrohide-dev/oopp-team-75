package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TagTest {

    private Tag tag;

    @BeforeEach
    void setup() {
        tag = new Tag();
    }

    @Test
    void constructor1() {
        Tag tag = new Tag();
        assertNotNull(tag.getId());
        assertNull(tag.getTitle());
        assertNull(tag.getTasks());
        assertNull(tag.getBoard());
    }

    @Test
    void constructor2() {
        Tag tag = new Tag("a");
        assertNotNull(tag.getId());
        assertEquals("a", tag.getTitle());
        assertEquals(new HashSet<>(), tag.getTasks());
        assertNotNull(tag.getBoard());
    }

    @Test
    void getId() {
        assertNotNull(tag.getId());
    }

    @Test
    void setId() {
        tag.setId(999);
        assertEquals(999, tag.getId());
    }

    @Test
    void getTitle() {
        assertNull(tag.getTitle());
    }

    @Test
    void setTitle() {
        tag.setTitle("a");
        assertEquals("a", tag.getTitle());
    }

    @Test
    void getTasks() {
        assertNull(tag.getTasks());
    }

    @Test
    void setTasks() {
        Set<Task> tasks = new HashSet<>();
        Task task = new Task();
        tag.setTasks(tasks);
        assertNotNull(tag.getTasks());
        tasks.add(task);
        tag.setTasks(tasks);
        assertEquals(tasks, tag.getTasks());
    }

    @Test
    void getBoard() {
        assertNull(tag.getBoard());
    }

    @Test
    void setBoard() {
        Board board = new Board();
        tag.setBoard(board);
        assertEquals(board, tag.getBoard());
    }
    @Test
    void getColor() {
        // TO-DO create this test
       // assertEquals(6,tag.getColor().length());
    }

    @Test
    void setColor() {
        String color = "FFFFFF";
        tag.setColor(color);
        assertEquals("FFFFFF", tag.getColor());
    }

    @Test
    void equals() {
        Tag tag1 = new Tag("a");
        Tag tag2 = new Tag("a");
        Tag tag3 = new Tag("b");
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

    @Test
    void hashCodeTest() {
        Tag tag1 = new Tag("a");
        Tag tag2 = new Tag("a");
        Tag tag3 = new Tag("b");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    void isChildOfTask(){
        Task task = new Task();
        tag.setTasks(new HashSet<>());
        assertFalse(tag.isChildOfTask(task.getId()));
        Set<Task> tasks = new HashSet<>();
        tasks.add(task);
        tag.setTasks(tasks);
        assertTrue(tag.isChildOfTask(task.getId()));
    }

    @Test
    void isChildOfBoard(){
        Board board = new Board("1");
        tag.setBoard(board);
        assertTrue(tag.isChildOfBoard("1"));
        tag.setBoard(new Board("2"));
        assertFalse(tag.isChildOfBoard("1"));
    }

    @Test
    void createTag(){
        Tag tag = Tag.createTag("a", 3L);
        assertNotNull(tag);
        assertEquals("a", tag.getTitle());
        assertEquals(new HashSet<>(), tag.getTasks());
        assertNotNull(tag.getBoard());
    }

    @Test
    void createTag2(){
        Tag tag = Tag.createTag("a", 100);
        assertNotNull(tag);
        assertEquals("a", tag.getTitle());
        assertEquals(new HashSet<>(), tag.getTasks());
        assertNotNull(tag.getBoard());
        assertEquals(100, tag.getId());
    }
}