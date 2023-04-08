package server.api.controllers;


import commons.Board;
import commons.models.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.services.BoardService;
import server.api.services.TagService;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.database.TagRepository;
import server.database.TagRepositoryTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BoardControllerTest {

    private BoardController boardController;
    private BoardRepository boardRepository;
    private BoardService boardService;
    private TagRepository tagRepository;
    @BeforeEach
    public void setup() {
        tagRepository = new TagRepositoryTest();
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        this.boardController = new BoardController(boardService, new TagService(tagRepository, boardRepository));
        boardController.authenticate("testing");

        boardController.create(new CreateBoardModel("key", "name"));
        boardController.create(new CreateBoardModel("key2", "name2"));
        boardController.create(new CreateBoardModel("key3", "name3"));
    }

    @Test
    void testGetAll() {
        List<Board> boards = boardController.getAll();
        assertEquals(3, boards.size());
        assertEquals(boards.get(0), new Board(new CreateBoardModel("key", "name")));
        assertEquals(boards.get(1), new Board(new CreateBoardModel("key2", "name2")));
        assertEquals(boards.get(2), new Board(new CreateBoardModel("key3", "name3")));
    }

}