package server.api.controllers;

import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.TaskMoveModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.ListService;
import server.exceptions.ListDoesNotExist;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping(path = { "", "/" })
    public List<TaskList> getAll() { return listService.getAll(); }

    /**
     * Gets a taskList from the database by id. If the id does not exist in the
     * database, the method will respond with a bad request.
     * @param id the list id
     * @return the stored taskList
     */
    @MessageMapping("/list/get")
    @SendTo("/topic/list/get")
    public ResponseEntity<TaskList> getById(String id) {
        try {
            TaskList taskList = listService.getById(Long.parseLong(id));
            return ResponseEntity.ok(taskList);
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    @MessageMapping("/list/rename/{id}")
    @SendTo("/topic/list/rename/{id}")
    public ResponseEntity<TaskList> renameList(@PathVariable("id") String id,String name) {
        try {
            TaskList taskList = listService.renameList(Long.parseLong(id),name);
            return ResponseEntity.ok(taskList);
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    /**
     *
     *
     *
     *
     */
    @MessageMapping("/list/createlist")
    public void createList(Board board) {
        listService.createList(board);
    }
    @MessageMapping("/list/createTask")
    public void createTask(TaskList list) {
        listService.createTask(list);
    }
    /**
     * Deletes a taskList, including its children from the database by its id. If
     * the id does not exist in the database or has a wrong format, the method will respond with a
     * bad request.
     * @param id the taskList id
     * @return nothing
     */
    @MessageMapping("/list/delete")
    public ResponseEntity<TaskList> deleteById(@PathVariable("id") String id) {
        try {
            listService.deleteById(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
