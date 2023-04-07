package server.api.services;

import commons.Board;
import commons.CreateBoardModel;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.database.ListRepository;
import server.database.ListRepositoryTest;
import server.database.TaskRepository;
import server.database.TaskRepositoryTest;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    private TaskList list1;
    private TaskList list2;
    private TaskList list3;

    @BeforeEach
    public void setup() throws CannotCreateBoard, BoardDoesNotExist, ListDoesNotExist, TaskDoesNotExist {
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
    void getAllNew() throws CannotCreateBoard {
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
        boardService.createList(board1, 1L, "1");
        listService.createTask(listRepository.findById(1L).get(), "1");
        assertEquals(1, taskService.getAll().size());
    }

    @Test
    void getAll() {
        assertEquals(3, taskService.getAll().size());
    }

    @Test
    void getAllByList() throws ListDoesNotExist {
        assertEquals(2, listService.getAll().get(0).getTasks().size());
        assertEquals(0, listService.getAll().get(1).getTasks().size());
        assertEquals(1, listService.getAll().get(2).getTasks().size());
    }

    @Test
    void getById() throws ListDoesNotExist, TaskDoesNotExist, BoardDoesNotExist {
        listService.getById(1L).getTasks().forEach(task -> {
            System.out.println("task id: "+task.getid());
            System.out.println("task name: "+task.getTitle());
        });

        assertEquals("task 1", taskService.getById(10L).getTitle());
        assertEquals("task 2", taskService.getById(20L).getTitle());
        assertEquals("task 3", taskService.getById(30L).getTitle());
    }

    @Test
    void deleteById2() throws TaskDoesNotExist, ListDoesNotExist {
        taskService.deleteById(20L);
        assertEquals(1, listService.getById(1L).getTasks().size());
    }

    @Test
    void deleteById() throws TaskDoesNotExist, ListDoesNotExist {
        taskService.deleteById(10L);
        taskService.deleteById(10L);
        assertEquals(1, listService.getById(1L).getTasks().size());
        assertThrows(TaskDoesNotExist.class, () -> taskService.getById(777L));
    }

    @Test
    void save() {
        Task task = new Task(list1,"task 4");
        task.setid(40L);
        taskService.save(task);
        assertEquals(4, taskService.getAll().size());
    }

//    @Test
//    void renameTask() throws TaskDoesNotExist {
//        taskService.renameTask(taskService.getById(10L), "new name");
//        assertEquals("new name", taskService.getById(10L).getTitle());
//    }

//    @Test
//    void moveTask() throws ListDoesNotExist, TaskDoesNotExist {
//        taskService.moveTask(taskService.getById(10L), list2, 2);
//        assertEquals(1, list2.getTasks().size());
//    }
}