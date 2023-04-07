package server.api.controllers;
import commons.SubTask;
import commons.TaskList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.SubTaskService;
import server.exceptions.SubTaskDoesNotExist;
import java.util.Set;
@RestController
@RequestMapping("/api/subtasks")
public class SubTaskController {
    private final SubTaskService SubtaskService;

    public SubTaskController(SubTaskService SubtaskService) {
        this.SubtaskService = SubtaskService;
    }

    /**
     * Gets a subtask from the database by id. If the id does not exist in the
     * database, the method will respond with a bad request.
     * @param id the subtask key
     * @return the stored subtask
     */
    @PostMapping("/get/{id}")
    public ResponseEntity<SubTask> getById(@PathVariable("id") String id) throws SubTaskDoesNotExist {
        try {
            SubTask subTask = SubtaskService.getById(Long.parseLong(id));
            return ResponseEntity.ok(subTask);

        } catch (NumberFormatException | SubTaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    /**
     * Gets a set of subtasks appended to a given task, using its id
     * @param id the task id
     * @return the stored subtask list
     */
    @GetMapping("/getbytask/{id}")
    public ResponseEntity<Set<SubTask>> getByTask(@PathVariable("id") String id) {
        Set<SubTask> subtasks = SubtaskService.getAllSubTasksOfTask(Long.parseLong(id));
        return ResponseEntity.ok(subtasks);
    }
    /**
     * Deletes a subtask from the database by its id. If
     * the id does not exist in the database or has a wrong format, the method will respond with a
     * bad request.
     * @param id the subtask id
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskList> deleteById(@PathVariable("id") String id) throws SubTaskDoesNotExist {
        try {
            SubtaskService.deleteById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException | SubTaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }



}
