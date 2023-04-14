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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    private TagRepository tagRepository;
    private Board board1;

    @BeforeEach
    void setUp() throws CannotCreateBoard, ListDoesNotExist, TaskDoesNotExist, SubTaskDoesNotExist, BoardDoesNotExist {
        boardRepository = new BoardRepositoryTest();
        subTaskRepository = new SubTaskRepositoryTest();
        tagRepository = new TagRepositoryTest();
        taskRepository = new TaskRepositoryTest(subTaskRepository);
        listRepository = new ListRepositoryTest(taskRepository);
        boardService = new BoardService(boardRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        taskService = new TaskService(taskRepository, listRepository, tagRepository);
        subTaskService = new SubTaskService(subTaskRepository, taskRepository, boardRepository);
        board1 = boardService.create(new CreateBoardModel("1", "1"));

        boardService.createList(board1, 1L, "1");
        boardService.createList(board1, 2L, "2");
        boardService.createList(board1, 3L, "3");

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

        tasks[0] = listService.getById(1L).getTasks().get(0);
        tasks[1] = listService.getById(1L).getTasks().get(1);
        tasks[2] = listService.getById(3L).getTasks().get(0);

        tasks[0].setId(1L);
        tasks[1].setId(2L);
        tasks[2].setId(3L);

        subTasks = new SubTask[3];

        taskService.createSubTask(tasks[0], "subtask1");
        taskService.createSubTask(tasks[0], "subtask2");
        taskService.createSubTask(tasks[2], "subtask3");

        subTasks[0] = taskService.getById(1L).getSubtasks().get(0);
        subTasks[1] = taskService.getById(1L).getSubtasks().get(1);
        subTasks[2] = taskService.getById(3L).getSubtasks().get(0);

        subTasks[0].setId(10L);
        subTasks[1].setId(20L);
        subTasks[2].setId(30L);

        boardService.save(board1);
        subTaskRepository.saveAll(Arrays.asList(subTasks));

    }

    @Test
    void getById() throws SubTaskDoesNotExist {
        assertEquals(subTasks[0], subTaskService.getById(10L));
    }

    @Test
    void getByIdNotFound() {
        try {
            subTaskService.getById(100L);
        } catch (SubTaskDoesNotExist e) {
            assertEquals("There exists no subtask with the provided id.", e.getMessage());
        }
    }


    @Test
    void getAllSubTasksOfTask() throws TaskDoesNotExist {
        List<SubTask> subTasks = subTaskService.getAllSubTasksOfTask(1L);
    }

//    @Test
//    void deleteById() throws SubTaskDoesNotExist, TaskDoesNotExist {
//        // delete subtask 1
//        subTaskService.deleteById(10L);
//        assertEquals(2, subTaskRepository.findAll().size());
//    }

    @Test
    void deleteByIdNotFound() {
        assertThrows(SubTaskDoesNotExist.class, () -> subTaskService.deleteById(100L));
    }

    @Test
    void renameSubTask() throws SubTaskDoesNotExist {
        // tests renaming subtask 1
        subTaskService.renameSubTask(10L, "newName");
        assertEquals("newName", subTaskService.getById(10L).getTitle());
    }

    @Test
    void renameSubTaskNotFound() {
        assertThrows(SubTaskDoesNotExist.class, () -> subTaskService.renameSubTask(100L, "newName"));
    }

    @Test
    void getAll() {
        List<SubTask> subTasks = subTaskService.getAll();
        assertEquals(3, subTasks.size());
        assertEquals(this.subTasks[0], subTasks.get(0));
        assertEquals(this.subTasks[1], subTasks.get(1));
        assertEquals(this.subTasks[2], subTasks.get(2));
    }

    @Test
    void getAllNew() throws ListDoesNotExist, CannotCreateBoard {
        boardRepository = new BoardRepositoryTest();
        subTaskRepository = new SubTaskRepositoryTest();
        taskRepository = new TaskRepositoryTest(subTaskRepository);
        listRepository = new ListRepositoryTest(taskRepository);
        boardService = new BoardService(boardRepository);
        listService = new ListService(listRepository, taskRepository, boardRepository);
        taskService = new TaskService(taskRepository, listRepository, tagRepository);
        subTaskService = new SubTaskService(subTaskRepository, taskRepository, boardRepository);
        board1 = boardService.create(new CreateBoardModel("1", "1"));

        boardService.createList(board1, 1L, "1");
        listService.save(board1.getTaskLists().get(0));
        taskList = listService.getById(1L);
        listService.createTask(taskList, "task1");

        Task task = listService.getById(1L).getTasks().get(0);

        task.setId(1L);

        assertEquals(0, subTaskService.getAll().size());
    }
}