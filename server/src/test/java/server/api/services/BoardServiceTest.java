package server.api.services;

import commons.Board;
import commons.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class BoardServiceTest {
    private BoardRepository boardRepository;
    private BoardService boardService;
    @BeforeEach
    private void setup() throws CannotCreateBoard {
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);

        boardService.create(new CreateBoardModel("key", "name", 1234));
        boardService.create(new CreateBoardModel("key2", "name2", 1994));
        boardService.create(new CreateBoardModel("key3", "name3", 2334));
    }

    @Test
    void getAllEmpty() {
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        assertEquals(0, boardService.getAll().size());
    }

    @Test
    void getAllDefault() {
        assertEquals(3, boardService.getAll().size());
    }

    @Test
    void findByKey() {
        assertEquals(new Board(new CreateBoardModel("key3", "name3", 2334)), boardService.findByKey("key3"));
    }

    @Test
    void findByKeyEmpty() {
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        assertEquals(null, boardService.findByKey("imaginary"));
    }

    @Test
    void findByKeyNotExisting() {
        assertEquals(null, boardService.findByKey("imaginary"));
    }

    @Test
    void createSameKey() {
        assertThrows(CannotCreateBoard.class, () -> boardService.create(new CreateBoardModel("key3", "name1", 1100)));
    }

    @Test
    void createCorrectly() throws CannotCreateBoard {
        boardService = new BoardService(boardRepository);
        boardService.create(new CreateBoardModel("key4", "name4", 7777));
        assertEquals(new Board(new CreateBoardModel("key4", "name4", 7777)),boardService.findByKey("key4"));
    }

    @Test
    void createWrongModel() {
        assertThrows(CannotCreateBoard.class, () -> boardService.create(new CreateBoardModel("key4", null,12344)));
        assertThrows(CannotCreateBoard.class, () -> boardService.create(new CreateBoardModel(null, "tablename",12344)));
    }

    @Test
    void deleteByKeyNotExisting() {
        assertThrows(BoardDoesNotExist.class, () -> boardService.deleteByKey("notExisting"));
    }

    @Test
    void deleteByKey() throws BoardDoesNotExist {
        boardService.deleteByKey("key3");
        assertEquals(null, boardService.findByKey("key3"));
    }

    @Test
    void deleteByKeyEmpty() {
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        assertThrows(BoardDoesNotExist.class, () -> boardService.deleteByKey("key3"));
    }

    @Test
    void deleteByKeyTwice() throws BoardDoesNotExist {
        boardService.deleteByKey("key3");
        assertThrows(BoardDoesNotExist.class, () -> boardService.deleteByKey("key3"));
    }

    @Test
    void save() throws BoardDoesNotExist {
        boardService.save(new Board(new CreateBoardModel("key5", "pony", 5555)));
        assertEquals(new Board(new CreateBoardModel("key5", "pony", 5555)), boardService.findByKey("key5"));
    }

    @Test
    void saveNull(){
        assertThrows(BoardDoesNotExist.class, () -> boardService.save(null));
    }

}