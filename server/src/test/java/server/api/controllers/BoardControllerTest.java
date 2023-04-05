package server.api.controllers;



import commons.Board;
import commons.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.exceptions.CannotCreateBoard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BoardControllerTest {

    private BoardController boardController;
    private BoardRepository boardRepository;
    private BoardService boardService;
    @BeforeEach
    private void setup() throws CannotCreateBoard, IOException {
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        this.boardController = new BoardController(boardService);
        boardController.authenticate("testing");

        boardController.create(new CreateBoardModel("key", "name"));
        boardController.create(new CreateBoardModel("key2", "name2"));
        boardController.create(new CreateBoardModel("key3", "name3"));
    }

    @Test
    void testGetAll() throws CannotCreateBoard {
        List<Board> boards = boardController.getAll();
        assertEquals(3, boards.size());
        assertTrue(boards.get(0).equals(new Board(new CreateBoardModel("key", "name"))));
        assertTrue(boards.get(1).equals(new Board(new CreateBoardModel("key2", "name2"))));
        assertTrue(boards.get(2).equals(new Board(new CreateBoardModel("key3", "name3"))));
    }

    @Test
    void testGetAll1() throws CannotCreateBoard, IOException {
        this.boardController = new BoardController(boardService);
        boardController.authenticate("testing");
        List<Board> boards = new ArrayList<>();
        boards = boardController.getAll();
        assertEquals(null, boards);
    }

    @Test
    public void testGetAllNotAuthenticated() throws IOException {
        var boardController2 = new BoardController(boardService);
        List<Board> result = boardController.getAll();

        // Verify
        assertNull(result);
    }

    @Test
    void findByKey() {
        Board board = boardController.findByKey("key").getBody();
        assertEquals(new Board(new CreateBoardModel("key", "name")), board);
        Board board1 = boardController.findByKey("key2").getBody();
        assertEquals(new Board(new CreateBoardModel("key2", "name2")), board1);
        Board board2 = boardController.findByKey("key3").getBody();
        assertEquals(new Board(new CreateBoardModel("key3", "name3")), board2);
    }

    @Test
    void findByKeyEmpty() {
        Board board = boardController.findByKey("key4").getBody();
        assertEquals(null, board);
    }

    @Test
    void create() throws CannotCreateBoard {
        Board board = boardController.create(new CreateBoardModel("key4", "name4")).getBody();
        assertEquals(new Board(new CreateBoardModel("key4", "name4")), board);
    }

    @Test
    void createEmpty() throws CannotCreateBoard {
        assertThrows(ResponseStatusException.class, () -> boardController.create(new CreateBoardModel()));
    }

    @Test
    void createDuplicate() throws CannotCreateBoard {
        assertThrows(ResponseStatusException.class, () -> boardController.create(new CreateBoardModel("key", "name")));
    }

    @Test
    void delete() throws CannotCreateBoard {
        boardController.create(new CreateBoardModel("key4", "name4"));
        boardController.deleteByKey("key4");
        Board board = boardController.findByKey("key4").getBody();
        assertEquals(null, board);
    }

    @Test
    void deleteMultiple() throws  CannotCreateBoard {
        boardController.deleteByKey("key");
        boardController.deleteByKey("key2");
        boardController.deleteByKey("key3");
        List<Board> boards = boardController.getAll();
        assertEquals(0, boards.size());
        Board board = boardController.findByKey("key").getBody();
        assertEquals(null, board);
        Board board1 = boardController.findByKey("key2").getBody();
        assertEquals(null, board1);
        Board board2 = boardController.findByKey("key3").getBody();
        assertEquals(null, board2);
    }

    @Test
    void createList() {
        boardController.createList("key");
        assertEquals(1, boardController.findByKey("key").getBody().getTaskLists().size());
    }

    @Test
    void createListEmpty() {
        assertThrows(IllegalArgumentException.class, () -> boardController.createList(""));
    }

    @Test
    void createListNotFound() {
        assertThrows(ResponseStatusException.class, () -> boardController.createList("wrongKey"));
    }
}