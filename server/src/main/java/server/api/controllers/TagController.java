package server.api.controllers;

import commons.Tag;
import commons.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.TagService;
import server.api.services.TaskService;
import server.exceptions.TaskDoesNotExist;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
//    @GetMapping(path = { "", "/" })
//    public List<Tag> getAll() {
//        return tagService.getAll();
//    }
//
//    /**
//     * Gets a task from the database by id. If the id does not exist in the
//     * database, the method will respond with a bad request.
//     * @param id the task key
//     * @return the stored task
//     */
//    @PostMapping("/create/{task_id}")
//    public ResponseEntity<Task> getById(@PathVariable("id") String id,String title) {
//        try {
//            Task task = taskService.getById(Long.parseLong(id));
//            return ResponseEntity.ok(task);
//        } catch (NumberFormatException | TaskDoesNotExist e ) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
//        }
//    }

}
