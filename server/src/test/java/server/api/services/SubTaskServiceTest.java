package server.api.services;

import commons.Board;
import commons.SubTask;
import commons.Task;
import commons.TaskList;
import commons.models.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import server.exceptions.SubTaskDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import static org.junit.jupiter.api.Assertions.assertEquals;


class SubTaskServiceTest {
    private BoardRepository boardRepository;

    private SubTaskService subTaskService;

    private TaskService taskService;

    private TaskRepository taskRepository;

    private SubTaskRepository subTaskRepository;

    private ListService listService;

    private ListRepository listRepository;

    private BoardService boardService;

    private TaskList taskList;

    private TaskList taskList2;

    private TaskList taskList3;

    private Task[]  tasks;
    private SubTask[] subTasks;

    private Board board1;

    @BeforeEach
    void setUp() throws CannotCreateBoard, ListDoesNotExist, TaskDoesNotExist {
        boardRepository = new BoardRepositoryTest();
        subTaskRepository = new SubTaskRepositoryTest();
        taskRepository = new TaskRepositoryTest(subTaskRepository);
        listRepository = new ListRepositoryTest(taskRepository);
        boardService = new BoardService(boardRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        taskService = new TaskService(taskRepository, listRepository);
        subTaskService = new SubTaskService(subTaskRepository);
        board1 =boardService.create(new CreateBoardModel("1", "1"));

        boardService.createList(board1, 1L, "1");
        boardService.createList(board1, 2L, "2");
        boardService.createList(board1, 3L, "3");

        System.out.println("HOAOAOAOOAO"+board1.getTaskLists().get(0).getId());
        System.out.println(board1.getTaskLists().get(1).getId());
        System.out.println(board1.getTaskLists().get(2).getId());

        listService.save(board1.getTaskLists().get(0));
        listService.save(board1.getTaskLists().get(1));
        listService.save(board1.getTaskLists().get(2));

        taskList = listService.getById(1L);
        taskList2 = listService.getById(2L);
        taskList3 = listService.getById(3L);

        tasks = new Task[3];

        listService.createTask(taskList, "task1");
        listService.createTask(taskList, "task2");
        listService.createTask(taskList3, "task3");

        tasks[0] = taskService.getById(1L);
        tasks[1] = taskService.getById(2L);
        tasks[2] = taskService.getById(3L);

        subTasks = new SubTask[3];


    }

    @Test
    void getById() throws SubTaskDoesNotExist {
        assertEquals(subTasks[0], subTaskService.getById(1L));
    }

    @Test
    void getAllSubTasksOfTask() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void renameSubTask() {
    }

    @Test
    void getAll() {
    }
}