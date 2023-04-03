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
import server.api.services.TaskService;
import server.exceptions.CannotCreateBoard;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final BoardService boardService;
    private final ListService listService;

    public TaskController(TaskService taskService, BoardService boardService,ListService listService) {
        this.taskService = taskService;
        this.boardService = boardService;
        this.listService = listService;
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
    public Task getById(Long id) {
        try {
            return taskService.getById(id);
        } catch (NumberFormatException | TaskDoesNotExist e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    @MessageMapping("/task/rename/{boardKey}/{name}")
    @SendTo("/topic/boards")
    public Board renameTask(Long id,@DestinationVariable("name")String name,@DestinationVariable("boardKey") String boardKey) {
        try {
            taskService.renameTask(id,name);
            return boardService.findByKey(boardKey);
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
    @SendTo("/topic/boards")
    public Board moveTask(TaskMoveModel model) throws TaskDoesNotExist,ListDoesNotExist
    {
        Task task = getById(model.getTask_id());
        TaskList initiallist = task.getTaskList();
        TaskList targetlist = listService.getById(model.getTasklist_id());
        Board board = initiallist.getBoard();
        int target_order = model.getNew_task_order();
        initiallist.getTasks().remove(task);
        if(target_order>targetlist.getTasks().size())
        {
            targetlist.getTasks().add(task);
        }else {
            targetlist.getTasks().add(target_order, task);
        }
        taskService.moveTask(task,targetlist);
        return board;
    }


    /**
     * Deletes a task by its id. If the id does not exist in the database
     * or has a wrong format, the method will respond with a bad request.
     * @param id the task id
     * @return nothing
     */
    @MessageMapping("/task/delete/{key}")
    @SendTo("/topic/boards")
    public Board deleteById(Long id,@DestinationVariable("key") String boardKey) {
        try {
            taskService.deleteById(id);
            return boardService.findByKey(boardKey);
        } catch (TaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
