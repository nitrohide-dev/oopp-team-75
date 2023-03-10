package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import commons.Board;
import commons.Quote;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.database.BoardRepository;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class MainController {

    private final BoardRepository boardRepo;

    public MainController(BoardRepository boardRepo) {
        this.boardRepo = boardRepo;
    }

    /**
     * Gets a board from the database by key. If the key does not exist in the database, the method will respond with a bad request.
     * @param key the board key
     * @return the board
     */
    @GetMapping("/api/boards/{key}")
    public ResponseEntity<Board> getBoardById(@PathVariable("key") String key) {
        if (!boardRepo.existsById(key)) {
            return ResponseEntity.badRequest().build();
        }
        Board board = boardRepo.findById(key).get();
        return ResponseEntity.ok(board);
    }

    /**
     * Stores a board, its tasklists and its tasks in the database. This updates any existing boards, tasklists and tasks with the same key or id. This DOES NOT remove previously stored tasklists or tasks. To do this, you first need to delete the board by key, and then add the board to the database.
     * @param board
     * @return
     */
    @PostMapping(path = { "/api/boards", "/api/boards/" })
    public ResponseEntity<Board> addBoard(@RequestBody Board board) {

        if (board.getKey() == null) {
            return ResponseEntity.badRequest().build();
        }

        Board saved = boardRepo.save(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * Deletes a board, its tasklists and its tasks from the database by its key. If the key does not exist in the database, the method will respond with a bad request.
     * @param key
     * @return
     */
    @DeleteMapping("/api/boards/{key}")
    public ResponseEntity<Board> deleteBoardById(@PathVariable("key") String key) {

        if (!boardRepo.existsById(key)) {
            return ResponseEntity.badRequest().build();
        }

        Board board = boardRepo.findById(key).get();
        boardRepo.deleteById(key);
        return ResponseEntity.ok(board);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}

