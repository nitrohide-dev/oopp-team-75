package server.api.controllers;
import commons.Board;
import commons.TaskList;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
     * renames a list to the given name
     * @param name - the new name of the list
     * @param id - the id of the list which name should be change
     * @return the board the list belongs to
     */
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
    /**
     * creates task with a given name in a given list
     * @param listID - the id of the list to add the task to
     * @param name - the name of the task to be created
     * @return the board the new task should belong too
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
     * @param id - the id of the tasklist
     * @return the board the tasklist belongs to
     */
    @MessageMapping("/list/delete")
    @SendTo("/topic/boards")
    public Board deleteById(Long id) {
        try {
            return listService.deleteById(id);
        } catch (NumberFormatException | ListDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
