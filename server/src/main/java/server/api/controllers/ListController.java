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
import server.api.services.BoardService;
import server.api.services.ListService;
import server.exceptions.ListDoesNotExist;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListService listService;
    private final BoardService boardService;

    public ListController(ListService listService,BoardService boardService) {
        this.listService = listService;
        this.boardService = boardService;
    }

    @GetMapping(path = { "", "/" })
    public List<TaskList> getAll() { return listService.getAll(); }

    /**
     * Gets a taskList from the database by id. If the id does not exist in the
     * database, the method will respond with a bad request.
     * @param id the list id
     * @return the stored taskList
     */
//    @MessageMapping("/list/get")
//    @SendTo("/topic/list/get")
//    public ResponseEntity<TaskList> getById(Long id) {
//        try {
//            TaskList taskList = listService.getById(id);
//            return ResponseEntity.ok(taskList);
//        } catch (NumberFormatException | ListDoesNotExist e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
//        }
//    }
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
    @MessageMapping("/list/createTask/{name}/{boardkey}")
    @SendTo("/topic/boards")
    public Board createTask(int listID,@DestinationVariable("name") String name,@DestinationVariable("boardkey") String boardKey) throws ListDoesNotExist{
        TaskList list = listService.getById(boardKey,listID);
       String id = listService.createTask(list,name);
       return boardService.findByKey(boardKey);
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
