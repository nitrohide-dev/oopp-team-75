package server.api.controllers;

import commons.Board;
import commons.CreateBoardModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public List<Board> getAll() { return boardService.getAll(); }

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

    @MessageMapping("/boards") // sets address to /app/boards
    @SendTo("/topic/boards") // sends result to /topic/boards
    public Board update(Board board) throws Exception {
        boardService.save(board);
        return board;
    }

}
