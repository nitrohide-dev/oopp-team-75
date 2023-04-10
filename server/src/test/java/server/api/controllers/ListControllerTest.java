package server.api.controllers;

import commons.models.CreateBoardModel;
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
import server.database.SubTaskRepository;
import server.database.SubTaskRepositoryTest;
import server.database.TaskRepository;
import server.database.TaskRepositoryTest;
import server.exceptions.CannotCreateBoard;
import server.exceptions.ListDoesNotExist;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ListControllerTest {
    private BoardController boardController;
    private BoardRepository boardRepository;
    private BoardService boardService;
    private ListController listController;
    private ListRepository listRepository;
    private ListService listService;
    private TaskRepository taskRepository;
    private TaskController taskController;
    private TaskService taskService;

    private SubTaskRepository subTaskRepository;
    @BeforeEach
    private void setup() throws CannotCreateBoard, IOException {
        subTaskRepository = new SubTaskRepositoryTest();
        taskRepository = new TaskRepositoryTest(subTaskRepository);
        listRepository = new ListRepositoryTest(taskRepository);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        boardService = new BoardService(boardRepository);
        this.boardController = new BoardController(boardService);
        listController = new ListController(listService, boardService);
        taskController = new TaskController(taskService, boardService, listService);
        boardController.authenticate("testing");

        boardController.create(new CreateBoardModel("key", "name"));
        boardController.create(new CreateBoardModel("key2", "name2"));
        boardController.create(new CreateBoardModel("key3", "name3"));
    }
    @Test
    void getAll() {
        boardService.createList(boardRepository.getById("key"), 1000L);
        assertEquals(1, listController.getAll().size());
    }

    @Test
    void getAllEmpty() {
        assertEquals(0, listController.getAll().size());
    }

    @Test
    void getAll2() {
        boardService.createList(boardRepository.getById("key"), 1000L);
        boardService.createList(boardRepository.getById("key2"), 1100L);
        assertEquals(2, listController.getAll().size());
    }

    @Test
    void rename() {
        boardService.createList(boardRepository.getById("key"), 1000L);
        assertEquals("", listRepository.findById(1000L).get().getTitle());
        listController.renameList( 1000L, "new name");
        assertEquals("new name", listRepository.findById(1000L).get().getTitle());
    }

    @Test
    void renameEmpty() {
        listRepository = new ListRepositoryTest(taskRepository);
        assertThrows(ResponseStatusException.class, () -> listController.renameList(1L, "new name"));
        boardService.createList(boardRepository.getById("key"), 1000L);
    }

    @Test
    void renameNullName() {
        boardService.createList(boardRepository.getById("key"), 1000L);
        listController.renameList(1000L, null);
        assertEquals(null, listRepository.findById(1000L).get().getTitle());
    }

    @Test
    void createTask() throws ListDoesNotExist {
        boardService.createList(boardRepository.getById("key"), 1000L);
        listController.createTask(1000L, "task");
        assertEquals(1, listRepository.findById(1000L).get().getTasks().size());
    }

    @Test
    void createTask2() throws ListDoesNotExist {
        boardService.createList(boardRepository.getById("key"), 1000L);
        boardService.createList(boardRepository.getById("key"), 1001L);
        listController.createTask(1000L, "task");
        listController.createTask(1000L, "task1");
        listController.createTask(1001L, "task");
        assertEquals(3, listRepository.findById(1000L).get().getTasks().size()
                +listRepository.findById(1001L).get().getTasks().size());
    }
    @Test
    void deleteById() {
        boardService.createList(boardRepository.getById("key"), 1000L);
        assertEquals(1, listRepository.findAll().size());
        boardService.createList(boardRepository.getById("key2"), 1001L);
        listController.deleteById(1000L);
        assertEquals(1, listRepository.findAll().size());
    }

    @Test
    void deleteByIdEmpty() {
        assertEquals(0, listRepository.findAll().size());
        assertThrows(ResponseStatusException.class, () -> listController.deleteById(1000L));
    }

    @Test
    void deleteById2() {
        boardService.createList(boardRepository.getById("key"), 1000L);
        boardService.createList(boardRepository.getById("key2"), 1001L);
        assertEquals(2, listRepository.findAll().size());
        listController.deleteById(1000L);
        assertEquals(1, listRepository.findAll().size());
    }

}