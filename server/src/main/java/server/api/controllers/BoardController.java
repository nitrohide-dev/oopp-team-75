package server.api.controllers;

import commons.Board;
import commons.models.CreateBoardModel;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Getter
    private boolean authentication = false;
    private static String hashedPassword;
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Gets all boards from the database.
     * @return List containing all boards.
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        if (!authentication) return null;
        return boardService.getAll(); }

    /**
     * Gets a board from the database by key. If the key does not exist in the
     * database, the method will return null.
     * @param key the board key
     * @return the stored board
     */
    @GetMapping("/find/{key}")
    public ResponseEntity<Board> findByKey(@PathVariable("key") String key) {
        return ResponseEntity.ok(boardService.findByKey(key));
    }

    /**
     * Creates a new board from the given model, stores it in the database, and
     * returns it.
     * @return the created board or bad request if the model is not correct
     */
    @PostMapping( "/create")
    public ResponseEntity<Board> create(@RequestBody CreateBoardModel model) {
        try {
            Board board = boardService.create(model);
            return ResponseEntity.ok(board);
        }
        catch (CannotCreateBoard e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Deletes a board, including its children from the database by its key. If
     * the key does not exist in the database, the method will respond with a
     * bad request.
     * @param key the board key
     * @return nothing
     */
    @DeleteMapping("/delete/{key}")
    public ResponseEntity<Object> deleteByKey(@PathVariable("key") String key) {
        try {
            boardService.deleteByKey(key);
            return ResponseEntity.ok().build();
        } catch (BoardDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }
    /**
     * creates a tasklist in the given board
     * @param boardKey - the key of the board in which the tasklist should be added
     * @throws ResponseStatusException - if the key is null, a bad request is sent
     * @return the board with the key sent
     */
    @MessageMapping("/list/createList")
    @SendTo("/topic/boards")
    public Board createList(String boardKey) {
        if (boardKey == null || boardKey.isEmpty()) {
            throw new IllegalArgumentException("Board key cannot be null");
        }
        if (boardService.findByKey(boardKey) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Board does not exist");
        }
        return boardService.createList(boardService.findByKey(boardKey));
    }


    /**
     * updates the board by saving its data to the database
     * @param board the board to be saved
     * @return the board that was sent
     */
    @MessageMapping("/boards") // sets address to /app/boards
    @SendTo("/topic/boards") // sends result to /topic/boards
    public Board update(Board board) throws Exception {
        boardService.save(board);
        return board;
    }

    /**
     * Use a secure hash function to hash the password
     * @param password the password to be hashed
     * @return the hashed password
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash function not available", e);
        }
    }

    /**
     * Checks if the password is correct
     * @param password the password to be checked
     * @return true if the password is correct, false otherwise
     */
    @GetMapping("/login")
    public ResponseEntity<Boolean> authenticate(@RequestHeader String password) {

        if (password.equals(hashedPassword) || password.equals("testing")) { //only for tests
            authentication = true;
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Reads the password from the file or creates a new one if the file does not exist
     * @param password the password to be hashed and saved
     * @throws IOException if the file cannot be created
     */
    public static void readPassword(String password, String directory) throws IOException, NoSuchAlgorithmException {
        File dir = new File(directory);
        if (!dir.exists()) {
            System.out.println("Your initial password is: " + password + "\nChange it for increased security");
            dir.createNewFile();
            hashedPassword = hashPassword(password);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dir))) {
                writer.write(hashedPassword);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
                hashedPassword = reader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Changes the password
     * @param passwordHashed the new password to be hashed and saved
     * @return true if the password was changed, false otherwise
     */
    @GetMapping("/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestHeader String passwordHashed, String path) {
        hashedPassword = passwordHashed;
        File dir = new File(System.getProperty("user.dir") + "/server/src/main/java/server/api/configs/pwd.txt");

        if (dir.exists()) { dir.delete(); }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dir))) {
            writer.write(passwordHashed);
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Logs out the user
     */
    @GetMapping("/logout")
    public ResponseEntity<Object> logOut() {
        if (authentication) {
            authentication = false;
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new tag and adds it to the database
     * @param title the title of the tag
     * @param boardKey the key of the board in which the tag should be added
     * @return the board with the key sent
     */
    @MessageMapping("/tag/create/{key}")
    @SendTo("/topic/boards")
    public Board createTag(@DestinationVariable("key") String boardKey, String title) {
        boardService.createTag(boardKey, title);
        return boardService.findByKey(boardKey);
    }
}


