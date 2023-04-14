package server.api.controllers;

import commons.Board;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import commons.models.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.api.services.ListService;
import server.api.services.SubTaskService;
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
import server.exceptions.CannotCreateBoard;
import server.exceptions.ListDoesNotExist;
import server.exceptions.SubTaskDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SubTaskControllerTest {
    private BoardRepository boardRepository;

    private SubTaskService subTaskService;

    private TaskService taskService;

    private TaskRepository taskRepository;

    private SubTaskRepository subTaskRepository;

    private ListService listService;

    private ListRepository listRepository;

    private BoardService boardService;

    private TaskList taskList;

    private Task[]  tasks;
    private SubTask[] subTasks;

    private Board board1;
    private TagRepository tagRepository;

    private SubTaskController subTaskController;
    @BeforeEach
    void setUp() throws ListDoesNotExist, TaskDoesNotExist, BoardDoesNotExist, CannotCreateBoard {
        boardRepository = new BoardRepositoryTest();
        subTaskRepository = new SubTaskRepositoryTest();
        taskRepository = new TaskRepositoryTest(subTaskRepository);
        listRepository = new ListRepositoryTest(taskRepository);
        boardService = new BoardService(boardRepository);
        tagRepository = new TagRepositoryTest();
        listService = new ListService(listRepository, taskRepository, boardRepository);
        taskService = new TaskService(taskRepository, listRepository, tagRepository);
        subTaskService = new SubTaskService(subTaskRepository, taskRepository, boardRepository);
        HashMap<Long, List<DeferredResult<List<SubTask>>>> pollConsumers = new HashMap<>();
        subTaskController = new SubTaskController(subTaskService, boardService, taskService, pollConsumers);


        board1 = boardService.create(new CreateBoardModel("1", "1"));

        boardService.createList(board1, 1L, "1");

        listService.save(board1.getTaskLists().get(0));

        taskList = listService.getById(1L);

        tasks = new Task[2];

        listService.createTask(taskList, "task1");
        listService.createTask(taskList, "task2");

        tasks[0] = listService.getById(1L).getTasks().get(0);
        tasks[1] = listService.getById(1L).getTasks().get(1);

        tasks[0].setId(1L);
        tasks[1].setId(2L);

        subTasks = new SubTask[3];

        taskService.createSubTask(tasks[0], "subtask1");
        taskService.createSubTask(tasks[0], "subtask2");
        taskService.createSubTask(tasks[1], "subtask3");

        subTasks[0] = taskService.getById(1L).getSubtasks().get(0);
        subTasks[1] = taskService.getById(1L).getSubtasks().get(1);
        subTasks[2] = taskService.getById(2L).getSubtasks().get(0);

        subTasks[0].setId(10L);
        subTasks[1].setId(20L);
        subTasks[2].setId(30L);

        boardService.save(board1);
        subTaskRepository.saveAll(Arrays.asList(subTasks));
    }

    @Test
    void getById() throws SubTaskDoesNotExist {
        assertTrue(subTaskController.getById("10").equals(subTasks[0]));
    }

    @Test
    void getByNonExistingId() {
        assertThrows(ResponseStatusException.class, () -> subTaskController.getById("100"));
    }

    @Test
    void getByTask() {
        // checks if it gets the right subtasks
        var subTasks = subTaskController.getByTask("1");
        assertEquals(2, subTasks.getBody().size());
        assertTrue(subTasks.getBody().contains(this.subTasks[0]));
        assertTrue(subTasks.getBody().contains(this.subTasks[1]));
    }

    @Test
    void getByNonExistingTask() {
        assertThrows(ResponseStatusException.class, () -> subTaskController.getByTask("100"));
    }

//    @Test
//    void deleteById() throws SubTaskDoesNotExist, TaskDoesNotExist {
//        subTaskController.deleteById(10L,"1");
//        assertThrows(ResponseStatusException.class, () -> subTaskController.getById("10"));
//        assertEquals(1, subTaskController.getByTask("1").getBody().size());
//    }

    @Test
    void deleteByNonExistingId() {
        assertThrows(ResponseStatusException.class, () -> subTaskController.deleteById(100L,"1"));
    }

    @Test
    void renameSubTask() throws SubTaskDoesNotExist {
        subTaskController.renameSubTask(10L, "newName", "1");
        assertEquals("newName", subTaskController.getById("10").getTitle());
    }

    @Test
    void renameNonExistingSubTask() {
        assertThrows(ResponseStatusException.class, () -> subTaskController.renameSubTask(100L, "newName", "1"));
    }
}