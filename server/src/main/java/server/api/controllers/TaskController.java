package server.api.controllers;

import commons.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.TaskService;
import server.exceptions.TaskDoesNotExist;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = { "", "/" })
    public List<Task> getAll() {
        return taskService.getAll();
    }

    /**
     * Gets a task from the database by id. If the id does not exist in the
     * database, the method will respond with a bad request.
     * @param id the task key
     * @return the stored task
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") String id) {
        try {
            Task task = taskService.getById(Long.parseLong(id));
            return ResponseEntity.ok(task);
        } catch (NumberFormatException | TaskDoesNotExist e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

//    /**
//     * Creates a new task from the given model, stores it in the database, and
//     * returns it.
//     * @return the created task or bad request if the model is not correct
//     */
//    @PostMapping("/create")
//    public ResponseEntity<Task> create(@RequestBody TaskModel model) {
//        try {
//            Task task = taskService.createTask(model);
//            return ResponseEntity.ok(task);
//        } catch (ListDoesNotExist | CannotCreateTask e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
//        }
//    }

    /**
     * Deletes a task by its id. If the id does not exist in the database
     * or has a wrong format, the method will respond with a bad request.
     * @param id the task id
     * @return nothing
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable("id") String id) {
        try {
            taskService.deleteById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (TaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
