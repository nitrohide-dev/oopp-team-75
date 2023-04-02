package server.api.controllers;



import commons.Board;
import commons.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.services.BoardService;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.exceptions.CannotCreateBoard;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BoardControllerTest {

    private BoardController boardController;
    private BoardRepository boardRepository;
    private BoardService boardService;
    @BeforeEach
    private void setup() throws CannotCreateBoard {
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        boardController = new BoardController(boardService);

        boardController.create(new CreateBoardModel("key", "name", 1234));
        boardController.create(new CreateBoardModel("key2", "name2", 1994));
        boardController.create(new CreateBoardModel("key3", "name3", 2334));
    }

    @Test
    void testGetAll() throws CannotCreateBoard {
        List<Board> boards = boardController.getAll();
        assertEquals(3, boards.size());
        assertTrue(boards.get(0).equals(new Board(new CreateBoardModel("key", "name", 1234))));
        assertTrue(boards.get(1).equals(new Board(new CreateBoardModel("key2", "name2", 1994))));
        assertTrue(boards.get(2).equals(new Board(new CreateBoardModel("key3", "name3", 2334))));
    }

    @Test
    void testFindByKey() {
    }


    @Test
    void testCreate() {
    }


    @Test
    void deleteByKey() {
    }

    @Test
    void update() {
    }
}