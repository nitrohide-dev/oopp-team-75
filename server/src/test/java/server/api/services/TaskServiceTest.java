package server.api.services;

import commons.Board;
import commons.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.*;
import server.exceptions.CannotCreateBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TaskServiceTest {
    private ListRepository listRepository;
    private ListService listService;
    private BoardService boardService;
    private BoardRepository boardRepository;
    private TaskRepository taskRepo;
    private TaskService taskService;

    private Board board1;
    private Board board2;
    private Board board3;

    @BeforeEach
    public void setup() throws CannotCreateBoard {
        taskRepo = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepo);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);

        boardService = new BoardService(boardRepository);
        listService = new ListService(listRepository, taskRepo, boardRepository);
        taskService = new TaskService(taskRepo, listRepository);

        boardService.create(new CreateBoardModel("1", "1"));
        boardService.create(new CreateBoardModel("2", "2"));
        boardService.create(new CreateBoardModel("3", "3"));

        board1 = boardService.findByKey("1");
        board2 = boardService.findByKey("2");
        board3 = boardService.findByKey("3");
    }

    @Test
    void getAllNew() {
        boardService.createList(board1, 1L, "1");
        listService.createTask(listRepository.findById(1L).get(), "1");
        assertEquals(1, taskService.getAll().size());
    }

    @Test
    void getAll() {
        assertEquals(0, taskService.getAll().size());
    }

    @Test
    void getById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void save() {
    }
}