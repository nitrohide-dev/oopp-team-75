package server.api.controllers;

import commons.Board;
import commons.CreateBoardModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

    private boolean authentication;
    private static String hashedPassword;
    private final BoardService boardService;

    public BoardController(BoardService boardService) throws IOException {
        this.boardService = boardService;
        this.authentication= false;
    }

    /**
     * Gets all boards from the database.
     * @return List containing all boards.
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        if(!authentication) return null;
        return (List<Board>) boardService.getAll(); }

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
        if(!authentication) return null;
        try {
            boardService.deleteByKey(key);
            return ResponseEntity.ok().build();
        } catch (BoardDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    @MessageMapping("/boards") // sets address to /app/boards
    @SendTo("/topic/boards") // sends result to /topic/boards
    public Board update(Board board) throws Exception {
        boardService.save(board);
        return board;
    }

    public static String hashPassword(String password) {
        // Use a secure hash function to hash the password
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash function not available", e);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> authenticate(@RequestHeader String password) {
        System.out.println("received");
        if (password.equals(hashedPassword)) {
            authentication=true;
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    public static void readPassword(String password) throws IOException {
        File dir = new File(System.getProperty("user.dir") + "/server/src/main/java/server/api/configs/pwd.txt");
        if(!dir.exists()) {
            System.out.println("Your initial password is: "+password+"\nChange it for increased security");
            dir.createNewFile();
            hashedPassword = hashPassword(password);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dir))) {
                writer.write(hashedPassword);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            try (BufferedReader reader = new BufferedReader(new FileReader(dir))) {
                hashedPassword= reader.readLine();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @GetMapping("/changePassword")
    public ResponseEntity<Boolean> changePassword(@RequestHeader String passwordHashed){
        hashedPassword = passwordHashed;
        File dir = new File(System.getProperty("user.dir") + "/server/src/main/java/server/api/configs/pwd.txt");
        if(dir.exists()){dir.delete();}
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dir))) {
            writer.write(passwordHashed);
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<Object> logOut() {
        System.out.println("received");
        if (authentication) {
            authentication=false;
        }
        return ResponseEntity.ok().build();
    }
}


