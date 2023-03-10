package server.api;

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

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardRepository repo;

    public BoardController(BoardRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{key}")
    public ResponseEntity<Board> getById(@PathVariable("key") String key) {
        if (!repo.existsById(key)) {
            return ResponseEntity.badRequest().build();
        }
        Board board = repo.findById(key).get();
        return ResponseEntity.ok(board);
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {

        if (isNullOrEmpty(board.getKey())) {
            return ResponseEntity.badRequest().build();
        }

        Board saved = repo.save(board);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Board> deleteById(@PathVariable("key") String key) {

        if (isNullOrEmpty(key) || !repo.existsById(key)) {
            return ResponseEntity.badRequest().build();
        }

        Board board = repo.findById(key).get();
        repo.deleteById(key);
        return ResponseEntity.ok(board);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

}