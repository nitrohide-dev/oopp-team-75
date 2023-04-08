package server.api.services;

import commons.SubTask;
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

    private SubTask[] subTasks;

    @BeforeEach
    void setUp() throws CannotCreateBoard {
        subTaskRepository = new SubTaskRepositoryTest();
        listRepository = new ListRepositoryTest();
        taskRepository = new TaskRepositoryTest();
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        subTaskService = new SubTaskService(subTaskRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        taskService = new TaskService(taskRepository, listRepository);

        boardService.create(new CreateBoardModel("key1", "title1"   ));
        boardService.create(new CreateBoardModel("key2", "title2"   ));
        boardService.create(new CreateBoardModel("key3", "title3"   ));

        boardService.createList(boardService.findByKey("key1"));
        boardService.createList(boardService.findByKey("key2"));
        boardService.createList(boardService.findByKey("key3"));

        taskList = boardService.getAll().get(0).getTaskLists().get(0);
        taskList2 = boardService.getAll().get(1).getTaskLists().get(0);
        taskList3 = boardService.getAll().get(2).getTaskLists().get(0);

        listService.createTask(taskList, "task1");
        listService.createTask(taskList, "task2");
        listService.createTask(taskList3, "task3");


        subTasks = new SubTask[3];

    }

    @Test
    void getById() {

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