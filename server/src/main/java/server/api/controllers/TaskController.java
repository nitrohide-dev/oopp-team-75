package server.api.controllers;

import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.TaskMoveModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.TaskService;
import server.exceptions.CannotCreateBoard;
import server.exceptions.ListDoesNotExist;
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
    @MessageMapping("/task/get")
    @SendTo("/topic/task/get")
    public ResponseEntity<Task> getById(String id) {
        try {
            Task task = taskService.getById(Long.parseLong(id));
            return ResponseEntity.ok(task);
        } catch (NumberFormatException | TaskDoesNotExist e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @MessageMapping("/task/rename/{id}")
    @SendTo("/topic/task/rename/{id}")
    public ResponseEntity<Task> renameTask(@PathVariable("id") String id,String name) {
        try {
            Task task = taskService.renameTask(Long.parseLong(id),name);
            return ResponseEntity.ok(task);
        } catch (NumberFormatException | TaskDoesNotExist e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    /**
     * method used to move a task from one tasklist to another.
     * @param model the TaskMoveModel with the parameters needed to move a task inbetween lists
     * @return the task that has been moved
     */
    @MessageMapping("/task/move")
    @SendTo("/topic/task/move")
    public ResponseEntity<Task> moveTask(TaskMoveModel model) throws TaskDoesNotExist
    {
        Task task = taskService.moveTask(model);
        return ResponseEntity.ok(task);
    }


    /**
     * Deletes a task by its id. If the id does not exist in the database
     * or has a wrong format, the method will respond with a bad request.
     * @param id the task id
     * @return nothing
     */
    @MessageMapping("/task/delete")
    public ResponseEntity<Object> deleteById(String id) {
        try {
            taskService.deleteById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (TaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
