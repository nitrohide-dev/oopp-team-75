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

import static org.junit.jupiter.api.Assertions.assertEquals;


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
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);

        listRepository = new ListRepositoryTest();
        taskRepository = new TaskRepositoryTest();
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
    void getAllMultiple(){
        boardService.createList(board1);
        boardService.createList(board1);
        boardService.createList(board1);
        System.out.println("HEREREREEEEREREERREER "+boardService.getAll()+listService.getAll());
        assertEquals(3, listService.getAll().size());
    }

    @Test
    void getAllMultipleInDifferentBoards(){
        boardService.createList(board1);
        boardService.createList(board2);
        boardService.createList(board3);
        System.out.println("HEREHEREREEEERERREER "+boardService.getAll()+listService.getAll());
        assertEquals(3, listService.getAll().size());
    }
    @Test
    void getById() {

    }

    @Test
    void deleteById() {
    }

    @Test
    void save() {
    }
}