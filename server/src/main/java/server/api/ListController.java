package server.api;

import commons.TaskList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import server.database.ListRepository;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListRepository repo;

    public ListController(ListRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = { "", "/" })
    public List<TaskList> getAll() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getById(@PathVariable("id") String id) {
        try {
            return getById(Long.parseLong(id));
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<TaskList> getById(long id) {
        if (!repo.existsById(id))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<TaskList> add(@RequestBody TaskList taskList) {
        if (taskList == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.save(taskList));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskList> deleteById(@PathVariable("id") String id) {
        try {
            return deleteById(Long.parseLong(id));
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<TaskList> deleteById(long id) {
        if (!repo.existsById(id))
            return ResponseEntity.badRequest().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
