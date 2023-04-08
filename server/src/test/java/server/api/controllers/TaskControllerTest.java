package server.api.controllers;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.api.services.ListService;
import server.api.services.TaskService;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.database.ListRepository;
import server.database.ListRepositoryTest;
import server.database.TaskRepository;
import server.database.TaskRepositoryTest;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TaskControllerTest {

    private TaskController taskController;

    private TaskService taskService;

    private BoardService boardService;

    private ListService listService;

    private BoardRepository boardRepository;

    private ListRepository listRepository;

    private TaskRepository taskRepository;

    private BoardController boardController;

    private Board board1;

    private Board board2;

    private Board board3;

    private TaskList list1;
    private TaskList list2;
    private TaskList list3;

    @BeforeEach
    void setUp() throws IOException, ListDoesNotExist, BoardDoesNotExist {
        taskRepository = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepository);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        boardService = new BoardService(boardRepository);
        taskService = new TaskService(taskRepository, listRepository);
        taskController = new TaskController(taskService, boardService, listService);
        boardController = new BoardController(boardService);

        boardController.create(new CreateBoardModel("key", "name"));
        boardController.create(new CreateBoardModel("key2", "name2"));
        boardController.create(new CreateBoardModel("key3", "name3"));

        board1 = boardRepository.getById("key");
        board2 = boardRepository.getById("key2");
        board3 = boardRepository.getById("key3");

        boardService.createList(board1, 1L, "1");
        boardService.createList(board1, 2L, "2");
        boardService.createList(board3, 3L, "3");

        list1 = listService.getById(1L);
        list2 = listService.getById(2L);
        list3 = listService.getById(3L);

        listService.createTask(listRepository.getById(1L), "task 1");
        listService.createTask(listRepository.getById(1L), "task 2");
        listService.createTask(listRepository.getById(3L), "task 3");

        Task task1 = listService.getById(1L).getTasks().get(0);
        Task task2 = listService.getById(1L).getTasks().get(1);
        Task task3 = listService.getById(3L).getTasks().get(0);

        task1.setid(10L);
        task2.setid(20L);
        task3.setid(30L);


        boardService.save(board1);
        boardService.save(board2);
        boardService.save(board3);


    }

    @Test
    void getAll() {
        assertEquals(3, taskController.getAll().size());
    }

    @Test
    void getAllEmpty() throws IOException {
        taskRepository = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepository);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        boardService = new BoardService(boardRepository);
        taskService = new TaskService(taskRepository, listRepository);
        taskController = new TaskController(taskService, boardService, listService);
        boardController = new BoardController(boardService);
        assertEquals(0, taskController.getAll().size());
    }

    @Test
    void getAllCorrectly(){
        assertEquals(3, taskController.getAll().size());
        assertEquals(10L, taskController.getAll().get(0).getid());
        assertEquals(20L, taskController.getAll().get(1).getid());
        assertEquals(30L, taskController.getAll().get(2).getid());
    }

    @Test
    void getById() {
        assertEquals(10L, taskController.getById(10L).getid());
    }

    @Test
    void getByIdDoesNotExist() {
        assertThrows(ResponseStatusException.class, () -> taskController.getById(100L));
    }

    @Test
    void deleteById() throws TaskDoesNotExist, ListDoesNotExist {
        taskController.deleteById(10L ,taskService.getById(10L).getTaskList().getBoard().getKey());
        assertEquals(1, list1.getTasks().size());
    }

    @Test
    void renameTask() throws TaskDoesNotExist, ListDoesNotExist {
        taskController.renameTask(10L, "wubba-lubba-dub-dub", board1.getKey());
        assertEquals("wubba-lubba-dub-dub", taskService.getById(10L).getTitle());
    }

    @Test
    void renameTaskException() {
        assertThrows(ResponseStatusException.class, () -> taskController.renameTask(100L, "wubba-lubba-dub-dub", board1.getKey()));
    }

    @Test
    void deleteByIdException() {
        assertThrows(ResponseStatusException.class, () -> taskController.deleteById(100L, board1.getKey()));
    }

    @Test
    void moveTask() throws TaskDoesNotExist, ListDoesNotExist {
        TaskMoveModel taskMoveModel = new TaskMoveModel(10L, 1L, 0);
        taskController.moveTask(taskMoveModel, board1.getKey());
        assertEquals(2, list1.getTasks().size());
        assertEquals(20, list1.getTasks().get(1).getid());
        TaskMoveModel taskMoveModel1= new TaskMoveModel(20L, 1l, Integer.MAX_VALUE);
        taskController.moveTask(taskMoveModel1, board1.getKey());
        assertEquals(20, list1.getTasks().get(1).getid());
    }
}