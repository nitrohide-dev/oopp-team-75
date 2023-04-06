package server.api.services;

import commons.Board;
import commons.CreateBoardModel;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.database.ListRepository;
import server.database.ListRepositoryTest;
import server.database.TaskRepository;
import server.database.TaskRepositoryTest;
import server.exceptions.CannotCreateBoard;
import server.exceptions.ListDoesNotExist;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ListServiceTest {

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
        boardRepository = new BoardRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepo);

        assertEquals(0,listService.getAll().size());

    }

    @Test
    void getAllCreate1() {
        boardService.createList(board1);
        assertEquals(1, listService.getAll().size());
    }

    @Test
    void getAllCreate2(){
        boardService.createList(board1);
        boardService.createList(board1);
        assertEquals(2, listService.getAll().size());
    }

    @Test
    void getAllCreate3(){
        boardService.createList(board1);
        boardService.createList(board1);
        boardService.createList(board1);
        assertEquals(3, listService.getAll().size());
    }


    @Test
    void getAllInDifferentBoards(){
        boardService.createList(board1);
        boardService.createList(board2);
        boardService.createList(board3);
        assertEquals(3, listService.getAll().size());
    }

    @Test
    void getAllMultipleInDifferentBoards(){
        boardService.createList(board1);
        boardService.createList(board1);
        boardService.createList(board2);
        boardService.createList(board2);
        boardService.createList(board3);
        boardService.createList(board3);
        boardService.createList(board3);
        assertEquals(7, listService.getAll().size());
    }
    @Test
    void getById() {
        taskRepo = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepo);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        assertThrows(ListDoesNotExist.class, () -> listService.getById(Long.valueOf("1")));
    }

    @Test
    void getByIdCreate1() throws ListDoesNotExist {
        boardService.createList(board1, 1000);
        assertEquals(listRepository.findById(1000L), Optional.of(listService.getById(1000L)));
    }

    @Test
    void getByIdCreate2() throws ListDoesNotExist {
        boardService.createList(board1, 1000);
        boardService.createList(board1, 1001);
        assertEquals(listRepository.findById(1001L), Optional.of(listService.getById(1001L)));
        assertEquals(listRepository.findById(1000L), Optional.of(listService.getById(1000L)));
    }

    @Test
    void getByIdMultipleBoards() throws ListDoesNotExist {
        boardService.createList(board1, 1000);
        boardService.createList(board1, 1001);
        boardService.createList(board2, 1002);
        boardService.createList(board2, 1003);
        boardService.createList(board3, 1004);
        boardService.createList(board3, 1005);
        assertEquals(listRepository.findById(1001L), Optional.of(listService.getById(1001L)));
        assertEquals(listRepository.findById(1000L), Optional.of(listService.getById(1000L)));
        assertEquals(listRepository.findById(1002L), Optional.of(listService.getById(1002L)));
        assertEquals(listRepository.findById(1003L), Optional.of(listService.getById(1003L)));
        assertEquals(listRepository.findById(1004L), Optional.of(listService.getById(1004L)));
        assertEquals(listRepository.findById(1005L), Optional.of(listService.getById(1005L)));
    }

    @Test
    void deleteById() {
        taskRepo = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepo);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        assertThrows(ListDoesNotExist.class, () -> listService.deleteById(Long.parseLong("1")));
    }

    @Test
    void deleteByIdCreate1() throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        listService.deleteById(1000L);
        var list = listService.getAll().toArray();
        assertEquals(0, list.length);
        assertThrows(ListDoesNotExist.class, () -> listService.getById(1000L));
    }

    @Test
    void deleteByIdCreate2() throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        boardService.createList(board1, 1001L);
        listService.deleteById(1000L);
        var list = listService.getAll().toArray();
        assertEquals(1, list.length);
        assertThrows(ListDoesNotExist.class, () -> listService.getById(1000L));
        assertEquals(listRepository.findById(1001L), Optional.of(listService.getById(1001L)));
    }

    @Test
    void deleteByIdCreate3()throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        boardService.createList(board1, 1001L);
        boardService.createList(board2, 1002L);
        boardService.createList(board3, 1003L);
        listService.deleteById(1000L);
        listService.deleteById(1003L);
        var list = listService.getAll().toArray();
        assertEquals(2, list.length);
        assertThrows(ListDoesNotExist.class, () -> listService.getById(1000L));
        assertThrows(ListDoesNotExist.class, () -> listService.getById(1003L));
        assertEquals(listRepository.findById(1002L), Optional.of(listService.getById(1002L)));
        assertEquals(listRepository.findById(1001L), Optional.of(listService.getById(1001L)));
    }

    @Test
    void save() throws ListDoesNotExist {
        taskRepo = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest(taskRepo);
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        TaskList list = new TaskList();
        list.setid(1000L);
        listService.save(list);
        assertEquals(list, listService.getById(1000L));
    }

    @Test
    void saveCreate1() throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        TaskList list = new TaskList();
        list.setid(1001L);
        listService.save(list);
        assertEquals(list, listService.getById(1001L));
        assertEquals(listRepository.findById(1000L), Optional.of(listService.getById(1000L)));
        assertEquals(2, listService.getAll().size());
    }

    @Test
    void saveCreate2() throws ListDoesNotExist {
        boardService.createList(board1, 1001L);
        TaskList list = new TaskList();
        list.setid(1001L);
        listService.save(list);
        assertEquals(list, listService.getById(1001L));
        assertEquals(1, listService.getAll().size());
    }

    @Test
    void saveCreate3() throws ListDoesNotExist {
        TaskList list = new TaskList();
        list.setid(1001L);
        listService.save(list);
        TaskList list2 = new TaskList();
        list2.setid(1002L);
        listService.save(list2);
        assertEquals(list, listService.getById(1001L));
        assertEquals(list2, listService.getById(1002L));
        assertEquals(2, listService.getAll().size());
    }

    @Test
    void renameList() {
        listRepository = new ListRepositoryTest(taskRepo);
        taskRepo = new TaskRepositoryTest();
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        assertThrows(ListDoesNotExist.class, () -> listService.renameList(1L, "test"));
    }

    @Test
    void createListName() throws ListDoesNotExist {
        boardService.createList(board1, 1000L, "test");
        assertEquals("test", listService.getById(1000L).getTitle());
    }

    @Test
    void renameListCreate() throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        assertEquals("", listService.getById(1000L).getTitle());
    }

    @Test
    void renameList1() throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        listService.renameList(1000L, "test");
        assertEquals("test", listService.getById(1000L).getTitle());
    }

    @Test
    void createTask() throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        listService.createTask(board1.getTaskLists().get(0), "test");
        assertEquals("test", listService.getById(1000L).getTasks().get(0).getTitle());
    }

    @Test
    void createTask2() throws ListDoesNotExist {
        boardService.createList(board1, 1000L);
        listService.createTask(board1.getTaskLists().get(0), "test");
        listService.createTask(board1.getTaskLists().get(0), "test2");
        assertEquals("test", listService.getById(1000L).getTasks().get(0).getTitle());
        assertEquals("test2", listService.getById(1000L).getTasks().get(1).getTitle());
    }

    @Test
    void createTask3() throws  ListDoesNotExist{
        boardService.createList(board1, 1000L);
        listService.createTask(board1.getTaskLists().get(0), "test");
        // tests if the taskrepository is called
        assertEquals(1, taskRepo.count());
    }
}