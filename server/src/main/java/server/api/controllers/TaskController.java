package server.api.controllers;

import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import server.database.TaskRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<Task> getAll() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") String id) {
        try {
            return getById(Long.parseLong(id));
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Task> getById(long id) {
        if (!repo.existsById(id))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Task> add(@RequestBody Task task) {
        if (task == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.save(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteById(@PathVariable("id") String id) {
        try {
            return deleteById(Long.parseLong(id));
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Task> deleteById(long id) {
        if (!repo.existsById(id))
            return ResponseEntity.badRequest().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
