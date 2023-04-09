package commons;

import commons.models.CreateBoardModel;
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

class BoardTest {

    private Board board;

    @BeforeEach
    void setup() {
        board = new Board("a", "a", new ArrayList<>());
    }

    @Test
    void constructor1() {
        Board board = new Board();
        assertNull(board.getKey()); // not initialized
        assertNull(board.getTitle()); // not initialized
        assertNull(board.getTaskLists()); // not initialized
    }

    @Test
    void constructor2() {
        Board board = new Board("Hello");
        assertEquals("Hello", board.getKey()); // initialized by constructor parameter
        assertNotNull(board.getTitle()); // initialized to default value
        assertNotNull(board.getTaskLists()); // initialized to default value
    }

    @Test
    void constructor3() {
        Board board = new Board(new CreateBoardModel("a", "a"));
        assertEquals("a", board.getKey()); // initialized by constructor parameter
        assertEquals("a", board.getTitle()); // initialized by constructor parameter
        assertNotNull(board.getTaskLists()); // initialized to default value
    }

    @Test
    void constructor4() {
        Board board = new Board("a", "a",  new ArrayList<>());
        assertEquals("a", board.getKey()); // initialized by constructor parameter
        assertEquals("a", board.getTitle()); // initialized by constructor parameter
        assertEquals(new ArrayList<>(), board.getTaskLists()); // initialized by constructor parameter
    }

    @Test
    void getKey() {
        assertEquals("a", board.getKey());
        assertNotEquals("b", board.getKey());
    }

    @Test
    void setKey() {
        board.setKey("b");
        assertNotEquals("a", board.getKey());
        assertEquals("b", board.getKey());
    }

    @Test
    void getTitle() {
        assertEquals("a", board.getTitle());
        assertNotEquals("b", board.getTitle());
    }

    @Test
    void setTitle() {
        board.setTitle("b");
        assertNotEquals("a", board.getTitle());
        assertEquals("b", board.getTitle());
    }


    @Test
    void getTaskLists() {
        List<TaskList> goodList = new ArrayList<>();
        List<TaskList> naughtyList = new ArrayList<>();
        naughtyList.add(null);
        assertEquals(goodList, board.getTaskLists());
        assertNotEquals(naughtyList, board.getTaskLists());
    }

    @Test
    void setTaskLists() {
        List<TaskList> goodList = new ArrayList<>();
        List<TaskList> naughtyList = new ArrayList<>();
        goodList.add(null);
        board.setTaskLists(goodList);
        assertEquals(goodList, board.getTaskLists());
        assertNotEquals(naughtyList, board.getTaskLists());
    }

    @Test
    void testEquals() {
        Board board1 = new Board("a", "a",  new ArrayList<>());
        Board board2 = new Board("a", "a",  new ArrayList<>());
        Board board3 = new Board("b", "a",  new ArrayList<>());
        Board board4 = new Board("a", "b",  new ArrayList<>());
        List<TaskList> naughtyList = new ArrayList<>();
        naughtyList.add(null);
        Board board6 = new Board("a", "a",  naughtyList);

        assertEquals(board1, board1);
        assertEquals(board1, board2);
        assertNotEquals(board1, board3);
        assertNotEquals(board1, board4);
        assertNotEquals(board1, board6);
        assertNotEquals(null, board1);
    }

    @Test
    void testHashCode() {
        Board board1 = new Board("a", "a",  new ArrayList<>());
        Board board2 = new Board("a", "a",  new ArrayList<>());
        Board board3 = new Board("b", "a",  new ArrayList<>());
        Board board4 = new Board("a", "b",  new ArrayList<>());
        List<TaskList> naughtyList = new ArrayList<>();
        naughtyList.add(null);
        Board board6 = new Board("a", "a",  naughtyList);

        assertEquals(board1.hashCode(), board1.hashCode());
        assertEquals(board1.hashCode(), board2.hashCode());
        assertNotEquals(board1.hashCode(), board3.hashCode());
        assertNotEquals(board1.hashCode(), board4.hashCode());
        assertNotEquals(board1.hashCode(), board6.hashCode());
    }

    @Test
    void createTaskList() {
        TaskList tl1 = board.createTaskList();
        assertEquals(1, board.getTaskLists().size());
        assertEquals(board, tl1.getBoard());
        TaskList tl2 = board.createTaskList();
        assertEquals(2, board.getTaskLists().size());
        assertEquals(board, tl2.getBoard());
    }

    @Test
    void removeTaskList() {
        TaskList tl1 = board.createTaskList();
        TaskList tl2 = board.createTaskList();
        board.removeTaskList(tl1);
        assertEquals(1, board.getTaskLists().size());
        assertEquals(null, tl1.getBoard());
        board.removeTaskList(tl2);
        assertEquals(0, board.getTaskLists().size());
        assertEquals(null, tl2.getBoard());
        assertThrows(IllegalArgumentException.class, () -> board.removeTaskList(null));
    }
    @Test
    void getPassword(){
        assertEquals(null, board.getPassword());
        assertNotEquals("123", board.getPassword());
    }
    @Test
    void setPassword(){
        board.setPassword("123");
        assertEquals("123", board.getPassword());
        assertNotEquals(null, board.getPassword());
    }

    @Test
    void getTags() {
        Set<Tag> goodList = new HashSet<>();
        Set<Tag> naughtyList = new HashSet<>();
        naughtyList.add(null);
        assertEquals(goodList, board.getTags());
        assertNotEquals(naughtyList, board.getTags());
    }

    @Test
    void setTags() {
        Set<Tag> goodList = new HashSet<>();
        Set<Tag> naughtyList = new HashSet<>();
        goodList.add(null);
        board.setTags(goodList);
        assertEquals(goodList, board.getTags());
        assertNotEquals(naughtyList, board.getTags());
    }

    @Test
    void createTag() {
        Tag tag1 = board.createTag("name");
        assertEquals(1, board.getTags().size());
        assertEquals(tag1, board.getTags().iterator().next());
        Tag tag2 = board.createTag("name2");
        assertEquals(2, board.getTags().size());
        assertEquals(tag2, board.getTags().iterator().next());
    }

    @Test
    void createTaskList2(){
        TaskList tl1 = board.createTaskList(1L);
        assertEquals(1, board.getTaskLists().size());
        assertEquals(board, tl1.getBoard());
        assertEquals(1, tl1.getId());
        TaskList tl2 = board.createTaskList(2L);
        assertEquals(2, board.getTaskLists().size());
        assertEquals(board, tl2.getBoard());
        assertEquals(2, tl2.getId());
        assertThrows(IllegalArgumentException.class, () -> board.createTaskList(1L));
    }

    @Test
    void createTaskList4(){
        TaskList tl1 = board.createTaskList(1L, "name");
        assertEquals(1, board.getTaskLists().size());
        assertEquals(board, tl1.getBoard());
        assertEquals(1, tl1.getId());
        assertEquals("name", tl1.getTitle());
        TaskList tl2 = board.createTaskList(2L, "name2");
        assertEquals(2, board.getTaskLists().size());
        assertEquals(board, tl2.getBoard());
        assertEquals(2, tl2.getId());
        assertEquals("name2", tl2.getTitle());
        assertThrows(IllegalArgumentException.class, () -> board.createTaskList(1L, "name"));
    }
}