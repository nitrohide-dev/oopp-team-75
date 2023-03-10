package server.api;

import commons.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import server.database.BoardRepository;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository repo;

    public BoardController(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * Gets a board from the database by key. If the key does not exist in the
     * database, the method will respond with a bad request.
     * @param key the board key
     * @return the stored board
     */
    @GetMapping("/{key}")
    public ResponseEntity<Board> getByKey(@PathVariable("key") String key) {
        if (key == null || !repo.existsById(key))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.findById(key).get());
    }

    /**
     * Stores a board, but not its tasklists in the database. Adding and
     * updating tasklists is done using the listcontroller. This is done so
     * that updates to boards and their subcomponents can be saved to the
     * database independently.
     * @param board the board to store
     * @return the stored board
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {
        if (board == null || board.getKey() == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.save(board));
    }

    /**
     * Deletes a board, including all its tasklists and tasks from the database
     * by its key. If the key does not exist in the database, the method will
     * respond with a bad request.
     * @param key the board key
     * @return nothing
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Board> deleteByKey(@PathVariable("key") String key) {
        if (key == null || !repo.existsById(key))
            return ResponseEntity.badRequest().build();
        repo.deleteById(key);
        return ResponseEntity.ok().build();
    }



}
