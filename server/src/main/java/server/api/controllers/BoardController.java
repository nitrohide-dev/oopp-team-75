package server.api.controllers;

import commons.Board;
import commons.models.CreateBoardModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PostMapping( "/create")
    public ResponseEntity<Board> create(@RequestBody CreateBoardModel model) {
        try {
            Board board = boardService.createBoard(model);
            return ResponseEntity.ok(board);
        }
        catch (CannotCreateBoard e) {
            return ResponseEntity.badRequest().build();
        }
    }

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
