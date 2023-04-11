package server.api.controllers;


import commons.Board;
import commons.models.CreateBoardModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.database.BoardRepository;
import server.database.BoardRepositoryTest;
import server.database.TagRepository;
import server.database.TagRepositoryTest;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardControllerTest {

    @TempDir
    static Path tempDir;


    private BoardController boardController;
    private BoardRepository boardRepository;
    private BoardService boardService;
    private TagRepository tagRepository;
    @BeforeEach
    public void setup() throws IOException {
        tagRepository = new TagRepositoryTest();
        boardRepository = new BoardRepositoryTest();
        boardService = new BoardService(boardRepository);
        this.boardController = new BoardController(boardService);
        boardController.authenticate("testing");

        boardController.create(new CreateBoardModel("key", "name"));
        boardController.create(new CreateBoardModel("key2", "name2"));
        boardController.create(new CreateBoardModel("key3", "name3"));
        try {
            clearDirectoryContent(tempDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void testGetAll() throws CannotCreateBoard {
        List<Board> boards = boardController.getAll();
        assertEquals(3, boards.size());
        assertTrue(boards.get(0).equals(new Board(new CreateBoardModel("key", "name"))));
        assertTrue(boards.get(1).equals(new Board(new CreateBoardModel("key2", "name2"))));
        assertTrue(boards.get(2).equals(new Board(new CreateBoardModel("key3", "name3"))));
    }

    @Test
    void testGetAll1() throws CannotCreateBoard, IOException {
        var boardService = new BoardService(new BoardRepositoryTest());
        var boardController2 = new BoardController(boardService);
        boardController2.authenticate("testing");
        List<Board> boards = boardController2.getAll();
        assertEquals( new ArrayList<>(), boards);
    }

    @Test
    public void testGetAllNotAuthenticated() throws IOException {
        var boardController2 = new BoardController(boardService);
        boardController2.authenticate("wrong password");
        boardController2.create(new CreateBoardModel("key11", "name"));
        List<Board> result = boardController2.getAll();
        // Verify
        assertNull(result);
    }

    @Test
    void findByKey() {
        Board board = boardController.findByKey("key").getBody();
        assertEquals(new Board(new CreateBoardModel("key", "name")), board);
        Board board1 = boardController.findByKey("key2").getBody();
        assertEquals(new Board(new CreateBoardModel("key2", "name2")), board1);
        Board board2 = boardController.findByKey("key3").getBody();
        assertEquals(new Board(new CreateBoardModel("key3", "name3")), board2);
    }

    @Test
    void findByKeyEmpty() {
        Board board = boardController.findByKey("key4").getBody();
        assertEquals(null, board);
    }

    @Test
    void create() throws CannotCreateBoard {
        Board board = boardController.create(new CreateBoardModel("key4", "name4")).getBody();
        assertEquals(new Board(new CreateBoardModel("key4", "name4")), board);
    }

    @Test
    void createEmpty() throws CannotCreateBoard {
        assertThrows(ResponseStatusException.class, () -> boardController.create(new CreateBoardModel()));
    }

    @Test
    void createDuplicate() throws CannotCreateBoard {
        assertThrows(ResponseStatusException.class, () -> boardController.create(new CreateBoardModel("key", "name")));
    }

    @Test
    void delete() throws CannotCreateBoard {
        boardController.create(new CreateBoardModel("key4", "name4"));
        boardController.deleteByKey("key4");
        Board board = boardController.findByKey("key4").getBody();
        assertEquals(null, board);
    }

    @Test
    void deleteMultiple() throws  CannotCreateBoard {
        boardController.deleteByKey("key");
        boardController.deleteByKey("key2");
        boardController.deleteByKey("key3");
        List<Board> boards = boardController.getAll();
        assertEquals(0, boards.size());
        Board board = boardController.findByKey("key").getBody();
        assertEquals(null, board);
        Board board1 = boardController.findByKey("key2").getBody();
        assertEquals(null, board1);
        Board board2 = boardController.findByKey("key3").getBody();
        assertEquals(null, board2);
    }

    @Test
    void createList() {
        boardController.createList("key");
        assertEquals(1, boardController.findByKey("key").getBody().getTaskLists().size());
    }

    @Test
    void createListEmpty() {
        assertThrows(IllegalArgumentException.class, () -> boardController.createList(""));
    }

    @Test
    void createListNotFound() {
        assertThrows(ResponseStatusException.class, () -> boardController.createList("wrongKey"));
    }

    @Test
    void createList3() {
        boardController.createList("key");
        boardController.createList("key");
        boardController.createList("key2");
        assertEquals(2, boardController.findByKey("key").getBody().getTaskLists().size());
        assertEquals(1, boardController.findByKey("key2").getBody().getTaskLists().size());
    }

    @Test
    void updateEmpty() {
        Board board = null;
        assertThrows(BoardDoesNotExist.class, () -> boardController.update(board));
    }

    @Test
    void updateBoard() throws Exception {
        Board board = boardController.findByKey("key").getBody();
        board.setTitle("newName");
        boardController.update(board);
        assertEquals("newName", boardController.findByKey("key").getBody().getTitle());
    }

    @Test
    void hashPassword() throws NoSuchAlgorithmException {
        String password = "password";
        String password2 = "password2";
        String hashedPassword = BoardController.hashPassword(password);
        String hashedPassword2 = BoardController.hashPassword(password2);
        assertNotEquals(password, hashedPassword);
        assertNotEquals(password2, hashedPassword2);
    }

    @Test
    void hashPasswordEmpty() {
        String password = "";
        String password1 = null;
        assertThrows(IllegalArgumentException.class, () -> BoardController.hashPassword(password));
        assertThrows(IllegalArgumentException.class, () -> BoardController.hashPassword(password1));
    }

    @Test
    void hashPasswordSame() throws NoSuchAlgorithmException {
        String password = "password";
        String hashedPassword = BoardController.hashPassword(password);
        String hashedPassword1 = BoardController.hashPassword(password);
        assertEquals(hashedPassword, hashedPassword1);
    }

//    private final static String PASSWORD_FILE = System.getProperty("user.dir")
//    + "/server/src/main/java/server/api/configs/pwd.txt";
//    @Test
//    public void testReadPasswordCreatesFileAndWritesHashedPassword() throws IOException {
//        String password = "test123";
//        File file = new File(PASSWORD_FILE);
//        if (file.exists()) {
//            file.delete();
//        }
//        boardController.readPassword(password);
//        assertTrue(file.exists());
//        String hashedPassword = boardController.hashPassword(password);
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            assertEquals(hashedPassword, reader.readLine());
//        }
//    }

    @Test
    void testAuthenticate() throws IOException {
        assertTrue(boardController.isAuthentication());
    }
    //TODO: fix test
//    @Test
//    void testAuthenticateWrongPassword() throws IOException {
//        var boardController2 = new BoardController(boardService);
//        boardController2.authenticate("wrong password");
//        assertFalse(boardController.isAuthentication());
//    }

    @Test
    void testLogOut() throws IOException {
        boardController.logOut();
        assertFalse(boardController.isAuthentication());
    }



    @Test
    public void testReadPassword() throws IOException, NoSuchAlgorithmException {
        String testPassword = "testPassword";
        File dir = new File(tempDir.toString() + "/pwd.txt");

        BoardController.readPassword(testPassword,tempDir.toString()+"/pwd.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
            String hashedPassword = reader.readLine();
            assertEquals(BoardController.hashPassword(testPassword), hashedPassword);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
 // TO-DO this method shouldn't even exist
//    @Test
//    public void testChangePassword() throws IOException, NoSuchAlgorithmException {
//        String newPasswordHashed = "newPasswordHashed";
//        File dir = new File(tempDir.toString() + "/pwd.txt");
//        System.setProperty("user.dir", tempDir.toString());
//
//        ResponseEntity<Boolean> response = boardController.changePassword(BoardController
//                .hashPassword(newPasswordHashed),tempDir.toString()+"/pwd.txt");
//
//        assertTrue(response.getBody());
//        try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
//            String newPasswordFromFile = reader.readLine();
//            assertEquals(BoardController.hashPassword(newPasswordHashed), newPasswordFromFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void clearDirectoryContent(Path tempDir) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tempDir)) {
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    clearDirectoryContent(path);
                }
                Files.delete(path);
            }
        }
    }
}

