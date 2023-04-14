package server.api.controllers;

import commons.Board;
import commons.SubTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.api.services.SubTaskService;
import server.exceptions.ListDoesNotExist;
import server.exceptions.SubTaskDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/subtasks")
public class SubTaskController {
    private final SubTaskService subtaskService;

    private final HashMap<Long, List<DeferredResult<List<SubTask>>>> pollConsumers;
    private final BoardService boardService;
    private final long TIMEOUT_MS = 5000L;

    public SubTaskController(SubTaskService subtaskService, BoardService boardService,
                             HashMap<Long, List<DeferredResult<List<SubTask>>>> pollConsumers) {
        this.subtaskService = subtaskService;
        this.pollConsumers = pollConsumers;
        this.boardService = boardService;
    }

    /**
     * Gets a subtask from the database by id. If the id does not exist in the
     * database, the method will respond with a bad request.
     * @param id the subtask key
     * @return the stored subtask
     */
    @GetMapping("/getById/{id}")
    public SubTask getById(@PathVariable("id") String id) throws SubTaskDoesNotExist {
        try {
            return subtaskService.getById(Long.parseLong(id));

        } catch (NumberFormatException | SubTaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Gets a set of subtasks appended to a given task, using its id
     * @param id the task id
     * @return the stored subtask list
     */
    @GetMapping("/getByTask/{id}")
    public ResponseEntity<List<SubTask>> getByTask(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(subtaskService.getAllSubTasksOfTask(Long.parseLong(id)));
        } catch (NumberFormatException | TaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Deletes a subtask from the database by its id. If
     * the id does not exist in the database or has a wrong format, the method will respond with a
     * bad request.
     * @param subTaskId the subtask id
     */
    @MessageMapping("/subtask/delete/{boardKey}")
    @SendTo("/topic/boards")
    public Board deleteById(Long subTaskId, @DestinationVariable String boardKey) throws SubTaskDoesNotExist {
        try {
            subtaskService.deleteById(subTaskId);
            return boardService.findByKey(boardKey);
        } catch (NumberFormatException | SubTaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * renames a subtask in the database
     * if the subtask does not exist in the database, the method responds with a bad request
     * @param id - the id of the subtask
     * @param name - the new name of the subtask
     * @param boardKey - the key of the board in which the subtask is
     * @return the board the subtask is in
     */
    @MessageMapping("/subtask/rename/{boardKey}/{name}")
    @SendTo("/topic/boards")
    public Board renameSubTask(Long id, @DestinationVariable("name")String name,
                               @DestinationVariable("boardKey") String boardKey) {
        try {
            subtaskService.renameSubTask(id, name);
            return boardService.findByKey(boardKey);
        } catch (SubTaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @MessageMapping("/subtask/check/{boardKey}")
    @SendTo("/topic/boards")
    public Board changeCheckSubTask(Long id, @DestinationVariable String boardKey) {
        try {
            subtaskService.changeCheckSubTask(id);
            return boardService.findByKey(boardKey);
        } catch (SubTaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @MessageMapping("/subtask/moveup/{id}")
    @SendTo("/topic/boards")
    public Board movesubTaskUp(int order, @DestinationVariable("id")long subTaskId) throws TaskDoesNotExist, ListDoesNotExist
    {

        return subtaskService.movesubTaskUp(order,subTaskId);
    }
    @MessageMapping("/subtask/movedown/{id}")
    @SendTo("/topic/boards")
    public Board movesubTaskDown(int order, @DestinationVariable("id")long subTaskId) throws TaskDoesNotExist, ListDoesNotExist
    {

        return subtaskService.movesubTaskDown(order,subTaskId);
    }
    /**
     * Updates a subtask in the database. If the subtask does not exist in the
     * database, the method will respond with a bad request.
     * @param id of the subtask to be updated
     * @return the updated subtask
     */
    @GetMapping("/{id}/poll")
    public DeferredResult<List<SubTask>> longPoll(@PathVariable Long id) {
        DeferredResult<List<SubTask>> output = new DeferredResult<>(TIMEOUT_MS);
        try {
            if (!pollConsumers.containsKey(id))
                pollConsumers.put(id, Collections.synchronizedList(new ArrayList<>()));
            pollConsumers.get(id).add(output);
            output.onTimeout(() -> {
                pollConsumers.get(id).remove(output);
                output.setErrorResult("Timeout");
            });
        } catch (Exception e) {
            output.setErrorResult("OOPS!");
        }
        return output;
    }
}
