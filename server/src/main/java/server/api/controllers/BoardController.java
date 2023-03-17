package server.api.controllers;

import commons.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * Gets all boards from the database.
     * @return List containing all boards.
     */
    @GetMapping(path = { "", "/" })
    public List<Board> getAll() { return boardService.getAllBoards(); }

    /**
     * Gets a board from the database by key. If the key does not exist in the
     * database, the method will respond with a bad request.
     * @param key the board key
     * @return the stored board
     */
    @GetMapping("/{key}")
    public ResponseEntity<Board> getByKey(@PathVariable("key") String key) {
        try {
            Board board = boardService.getBoardByKey(key);
            return ResponseEntity.ok(board);
        }
        catch (BoardDoesNotExist e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Creates a new board with a random key, stores it in the database, and
     * returns it. This method has a worst-case time complexity of O(∞).
     * @return the created board
     */
    @PostMapping(path = { "create", "create/" })
    public ResponseEntity<Board> create() {
        try {
            Board board = boardService.createBoard();
            return ResponseEntity.ok(board);
        }
        catch (CannotCreateBoard e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    /**
//     * Creates a new board with a specified key, stores it in the database, and
//     * returns it. If the key already exists in the database, the method will
//     * respond with a bad request.
//     * @return the created board
//     */
//    @GetMapping("create/{key}")
//    public ResponseEntity<Board> create(@PathVariable("key") String key) {
//        if (boardService.existsById(key))
//            return ResponseEntity.badRequest().build();
//        return ResponseEntity.ok(boardService.save(new Board(key)));
//    }

//    /**
//     * Stores a board, but not its children in the database. Adding and
//     * updating tasklists is done using the listcontroller. This is done so
//     * that updates to boards and their subcomponents can be saved to the
//     * database independently.
//     * @param board the board to store
//     * @return the stored board
//     */
//    @PostMapping(path = { "", "/" })
//    public ResponseEntity<Board> save(@RequestBody Board board) {
//        if (board == null || board.getKey() == null)
//            return ResponseEntity.badRequest().build();
//        return ResponseEntity.ok(boardService.save(board));
//    }

    /**
     * Deletes a board, including its children from the database by its key. If
     * the key does not exist in the database, the method will respond with a
     * bad request.
     * @param key the board key
     * @return nothing
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Object> deleteByKey(@PathVariable("key") String key) {
        try {
            boardService.deleteBoardByKey(key);
            return ResponseEntity.ok().build();
        }
        catch (BoardDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

}
