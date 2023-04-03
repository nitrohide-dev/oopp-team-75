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

import java.util.ArrayList;
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
    @MessageMapping("/list/rename/{name}")
    @SendTo("/topic/boards")
    public Board renameList(Long id,@DestinationVariable("name") String name) {
        try {
            TaskList taskList = listService.renameList(id,name);
            return taskList.getBoard();
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    @MessageMapping("/list/createlist")
    @SendTo("/topic/boards")
    public Board createList(Board board) {
        return listService.createList(board);
    }
    /**
     *
     *
     *
     *
     */
    @MessageMapping("/list/createTask/{name}")
    @SendTo("/topic/boards")
    public Board createTask(Long listID,@DestinationVariable("name") String name) throws ListDoesNotExist{
        TaskList list = (listService.getById(listID));
       String id = listService.createTask(list,name);
       return boardService.findByKey(id);
    }
    /**
     * Deletes a taskList, including its children from the database by its id. If
     * the id does not exist in the database or has a wrong format, the method will respond with a
     * bad request.
     * @param id the taskList id
     * @return nothing
     */
    @MessageMapping("/list/delete")
    @SendTo("/topic/boards")
    public Board deleteById(@PathVariable("id") Long id) {
        try {
            return listService.deleteById(id);
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
