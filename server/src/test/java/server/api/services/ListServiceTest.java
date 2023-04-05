package server.api.services;

import commons.Board;
import commons.CreateBoardModel;
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
    private TaskRepository taskRepository;
    private  TaskService taskService;

    private Board board1;
    private Board board2;
    private Board board3;


    @BeforeEach
    private void setup() throws CannotCreateBoard {

        listRepository = new ListRepositoryTest();
        taskRepository = new TaskRepositoryTest();
        boardRepository = new BoardRepositoryTest((ListRepositoryTest) listRepository);
        boardService = new BoardService(boardRepository);

        listService = new ListService(listRepository, taskRepository, boardRepository);


        taskService = new TaskService(taskRepository, listRepository);

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
        listRepository = new ListRepositoryTest();

        assertEquals(0,listService.getAll().size());

    }

    @Test
    void getAllCreate1() {
        boardService.createList(board1);
        System.out.println("HEREREREREREERREER "+boardService.getAll()+listService.getAll());
        assertEquals(1, listService.getAll().size());
    }

    @Test
    void getAllCreate2(){
        boardService.createList(board1);
        boardService.createList(board1);
        System.out.println("HEREREREEEEREREERREER "+boardService.getAll()+listService.getAll());
        assertEquals(2, listService.getAll().size());
    }

    @Test
    void getAllCreate3(){
        boardService.createList(board1);
        boardService.createList(board1);
        boardService.createList(board1);
        System.out.println("HEREREREEEEREREERREER "+boardService.getAll()+listService.getAll());
        assertEquals(3, listService.getAll().size());
    }


    @Test
    void getAllInDifferentBoards(){
        boardService.createList(board1);
        boardService.createList(board2);
        boardService.createList(board3);
        System.out.println("HEREHEREREEEERERREER "+boardService.getAll()+listService.getAll());
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
        System.out.println("HEREHEREREEEERERREER "+boardService.getAll()+listService.getAll());
        assertEquals(7, listService.getAll().size());
    }
    @Test
    void getById() {
        taskRepository = new TaskRepositoryTest();
        listRepository = new ListRepositoryTest();
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
    }

    @Test
    void save() {
    }
}