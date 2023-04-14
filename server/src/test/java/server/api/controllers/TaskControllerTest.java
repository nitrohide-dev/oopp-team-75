package server.api.controllers;

import commons.Board;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import commons.models.CreateBoardModel;
import commons.models.TaskMoveModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.api.services.ListService;
import server.api.services.TaskService;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.database.ListRepository;
import server.database.ListRepositoryTest;
import server.database.SubTaskRepository;
import server.database.SubTaskRepositoryTest;
import server.database.TagRepository;
import server.database.TagRepositoryTest;
import server.database.TaskRepository;
import server.database.TaskRepositoryTest;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

    private SubTaskRepository subTaskRepository;
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() throws IOException, ListDoesNotExist, BoardDoesNotExist {
        tagRepository = new TagRepositoryTest();
        subTaskRepository = new SubTaskRepositoryTest();
        taskRepository = new TaskRepositoryTest(subTaskRepository);
        listRepository = new ListRepositoryTest(taskRepository);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        boardService = new BoardService(boardRepository);
        taskService = new TaskService(taskRepository, listRepository, tagRepository);
        HashMap<Long, List<DeferredResult<List<SubTask>>>> pollConsumers = new HashMap<>();
        taskController = new TaskController(taskService, boardService, listService, pollConsumers);
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

        task1.setId(10L);
        task2.setId(20L);
        task3.setId(30L);


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
        subTaskRepository = new SubTaskRepositoryTest();
        taskRepository = new TaskRepositoryTest(subTaskRepository);
        listRepository = new ListRepositoryTest(taskRepository);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        boardService = new BoardService(boardRepository);
        taskService = new TaskService(taskRepository, listRepository, tagRepository);
        HashMap<Long, List<DeferredResult<List<SubTask>>>> pollConsumers = new HashMap<>();
        taskController = new TaskController(taskService, boardService, listService, pollConsumers);
        boardController = new BoardController(boardService);
        assertEquals(0, taskController.getAll().size());
    }

    @Test
    void getAllCorrectly() {
        assertEquals(3, taskController.getAll().size());
        assertEquals(10L, taskController.getAll().get(0).getId());
        assertEquals(20L, taskController.getAll().get(1).getId());
        assertEquals(30L, taskController.getAll().get(2).getId());
    }

    @Test
    void getById() {
        assertEquals(10L, taskController.getById(10L).getId());
    }

    @Test
    void getByIdDoesNotExist() {
        assertThrows(ResponseStatusException.class, () -> taskController.getById(100L));
    }

    @Test
    void deleteById() throws TaskDoesNotExist, ListDoesNotExist {
        taskController.deleteById(10L);
        assertEquals(1, list1.getTasks().size());
    }

    @Test
    void renameTask() throws TaskDoesNotExist, ListDoesNotExist {
        taskController.renameTask(10L, "wubba-lubba-dub-dub", board1.getKey());
        assertEquals("wubba-lubba-dub-dub", taskService.getById(10L).getTitle());
    }

    @Test
    void renameTaskException() {
        assertThrows(ResponseStatusException.class, () -> taskController.renameTask(100L
                , "wubba-lubba-dub-dub", board1.getKey()));
    }

    @Test
    void deleteByIdException() {
        assertThrows(ResponseStatusException.class, () -> taskController.deleteById(100L));
    }

    @Test
    void moveTask() throws TaskDoesNotExist, ListDoesNotExist {
        TaskMoveModel taskMoveModel = new TaskMoveModel(10L, 1L, 0);
        taskController.moveTask(taskMoveModel, board1.getKey());
        assertEquals(2, list1.getTasks().size());
        assertEquals(20, list1.getTasks().get(1).getId());
        TaskMoveModel taskMoveModel1 = new TaskMoveModel(20L, 1l, Integer.MAX_VALUE);
        taskController.moveTask(taskMoveModel1, board1.getKey());
        assertEquals(20, list1.getTasks().get(1).getId());
    }

    @Test
    void findById() {
        assertEquals(10L, taskController.findById(10L).getBody().getId());
        assertThrows(ResponseStatusException.class, () -> taskController.findById(100L));
    }

    @Test
    void changeTaskDescription() throws TaskDoesNotExist, ListDoesNotExist {
        taskController.changeTaskDesc("wubba-lubba-dub-dub", 10L, board1.getKey());
        assertEquals("wubba-lubba-dub-dub", taskService.getById(10L).getDesc());
        assertThrows(ResponseStatusException.class, () -> taskController.changeTaskDesc("wubba-lubba-dub-dub", 100L
                , board1.getKey()));
    }

    @Test
    void createSubTask() throws TaskDoesNotExist, ListDoesNotExist {
        taskController.createSubTask(10L, "10");
        assertEquals(1, taskService.getById(10L).getSubtasks().size());
        assertEquals("10", taskService.getById(10L).getSubtasks().get(0).getTitle());
    }
}