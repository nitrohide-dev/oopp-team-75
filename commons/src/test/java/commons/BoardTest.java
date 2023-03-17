package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void validateKeyInvalid() {
        assertFalse(Board.validateKey(System.lineSeparator()));
        assertFalse(Board.validateKey(null));
    }

    @Test
    void validateKeyValid() {
        assertTrue(Board.validateKey("abc"));
        assertTrue(Board.validateKey("123"));
    }

    @Test
    void generateKeyWithLen() {
        String str = Board.generateKey(new Random(7), 8);
        assertEquals(8, str.length());
        assertTrue(Board.validateKey(str));
    }

    @Test
    void generateKeyWithoutLen() {
        String str = Board.generateKey(new Random(7));
        assertEquals(Board.MAX_KEY_LENGTH, str.length());
        assertTrue(Board.validateKey(str));
    }

    @Test
    void getKey() {
        Board board = new Board("abc");
        assertEquals(board.getKey(), "abc");
    }

    @Test
    void setKey() {
        Board board = new Board("abc");
        board.setKey("def");
        assertEquals(board.getKey(), "def");
    }

    @Test
    void getTitle() {
        Board board = new Board("abc");
        assertEquals(board.getTitle(), "");
    }

    @Test
    void setTitle() {
        Board board = new Board("def");
        board.setTitle("dee ee eff");
        assertEquals(board.getTitle(), "dee ee eff");
    }

    @Test
    void getTaskLists() {
        Board board = new Board("ChocolateCake");
        assertEquals(board.getTaskLists(), new ArrayList<>());
    }

    @Test
    void setTaskLists() {
        Board board = new Board("ChocolateCake");
        List<TaskList> list = new ArrayList<>();
        board.setTaskLists(list);
        list.add(new TaskList(board));
        list.add(new TaskList(board));
        assertEquals(board.getTaskLists(), list);
    }

    @Test
    void testEquals() {
        Board a = new Board("abc");
        Board b = new Board("def");
        Board c = new Board("def");
        assertEquals(a, a);
        assertNotEquals(a, b);
        assertEquals(b, c);
    }

    @Test
    void testHashCode() {
        Board a = new Board("abc");
        Board b = new Board("def");
        Board c = new Board("def");
        assertEquals(a.hashCode(), a.hashCode());
        assertNotEquals(a.hashCode(), b.hashCode());
        assertEquals(b.hashCode(), c.hashCode());
    }

    @Test
    void createTaskList() {
        Board a = new Board("abc");
        a.createTaskList();
        a.createTaskList();
        assertEquals(a.getTaskLists().size(), 2);
    }

    @Test
    void removeTaskList() {
        Board a = new Board("abc");
        a.createTaskList();
        TaskList b = a.createTaskList();
        TaskList c = a.createTaskList();
        a.removeTaskList(b);
        assertNull(b.getBoard());
        assertEquals(2, a.getTaskLists().size());
        assertEquals(c, a.getTaskLists().get(1));
    }

}