package server.api.controllers;


import commons.SubTask;
import commons.Task;
import commons.Board;
import commons.TaskList;
import commons.models.TaskMoveModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;
import server.api.services.BoardService;
import server.api.services.ListService;
import server.api.services.TaskService;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final BoardService boardService;
    private final ListService listService;
    private final HashMap<Long, List<DeferredResult<List<SubTask>>>> pollConsumers;

    public TaskController(TaskService taskService, BoardService boardService,
                          ListService listService, HashMap<Long, List<DeferredResult<List<SubTask>>>> pollConsumers) {
        this.taskService = taskService;
        this.boardService = boardService;
        this.listService = listService;
        this.pollConsumers = pollConsumers;
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

    /**
     * Gets a task from the database by id.
     * @param id the id of the task to be deleted
     * @return ResponseEntity with the task
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<Task> findById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(taskService.getById(id));
        } catch (NumberFormatException | TaskDoesNotExist e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * renames a task in the database
     * if the task does not exist in the database, the method responds with a bad request
     * @param id - the id of the task
     * @param name - the new name of the task
     * @param boardKey - the key of the board in which the task is
     * @return the board the task is in
     */
    @MessageMapping("/task/rename/{boardKey}/{name}")
    @SendTo("/topic/boards")
    public Board renameTask(Long id, @DestinationVariable("name")String name, @DestinationVariable("boardKey") String boardKey) {
        try {
            taskService.renameTask(id,name);
            return boardService.findByKey(boardKey);
        } catch (NumberFormatException | TaskDoesNotExist e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * changes task description in the database
     * if the task does not exist in the database, the method responds with a bad request
     * @param id - the id of the task
     * @param newDesc - the new description of the task
     * @param boardKey - the key of the board in which the task is
     * @return the board the task is in
     */
    @MessageMapping("/task/desc/{boardKey}/{id}")
    @SendTo("/topic/boards")
    public Board changeTaskDesc(String newDesc, @DestinationVariable("id") Long id,
                                @DestinationVariable("boardKey") String boardKey) {
        try {
            taskService.changeTaskDesc(id, newDesc);
            return boardService.findByKey(boardKey);
        } catch (NumberFormatException | TaskDoesNotExist e ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * method used to move a task from one task list to another.
     * @param model the TaskMoveModel with the parameters needed to move a task between lists
     * @param boardKey - the key of the board the task is in
     * @return the board the task is in
     */
    @MessageMapping("/task/move/{key}")
    @SendTo("/topic/boards")
    public Board moveTask(TaskMoveModel model,@DestinationVariable("key")String boardKey) throws TaskDoesNotExist,ListDoesNotExist
    {
        Task task =  getById(model.getTask_id());
        TaskList list = listService.getById(model.getTasklist_id());
        int order = model.getNew_task_order();
        if (order == Integer.MAX_VALUE) {
            order = list.getTasks().size();
            if (list.getId() == task.getTaskList().getId())
                order--;
        } else {
            order--;
        }
        taskService.moveTask(task,list,order);
        return boardService.findByKey(boardKey);
    }

    /**
     * Deletes a task by its id. If the id does not exist in the database
     * or has a wrong format, the method will respond with a bad request.
     * @param id - the id of the task
     * @return the board the task was in
     */
    @MessageMapping("/task/delete")
    @SendTo("/topic/boards")
    public Board deleteById(Long id) {
        try {
            return boardService.findByKey(taskService.deleteById(id));
        } catch (TaskDoesNotExist e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    /**
     * Adds a tag to a task
     * @param tagId - the id of the tag to be added
     * @param taskId - the id of the task to which the tag is to be added
     * @return the board the task is in
     */
    @MessageMapping("/task/addTag/{key}")
    @SendTo("/topic/boards")
    public Board addTag(Long tagId, @DestinationVariable("key") Long taskId) {
        var boardKey =  taskService.addTag(taskId, tagId);
        return boardService.findByKey(boardKey);
    }

    /**
     * Removes a tag from a task
     * @param tagId - the id of the tag to be removed
     * @param taskId - the id of the task from which the tag is to be removed
     * @return the board the task is in
     */
    @MessageMapping("/task/removeTag/{key}")
    @SendTo("/topic/boards")
    public Board removeTag(Long tagId, @DestinationVariable("key") Long taskId) {
        var boardKey =  taskService.removeTag(taskId, tagId);
        return boardService.findByKey(boardKey);
    }


    /**
     * creates a subtask, and adds to a task
     * @param taskID - the id of the task to which the subtask is to be added
     * @param title - the title of the subtask
     * @return the board the task is in
     * @throws TaskDoesNotExist - if the task does not exist in the database
     */
    @MessageMapping("/subtask/create/{title}")
    @SendTo("/topic/boards")
    public Board createSubTask(Long taskID,@DestinationVariable("title") String title) throws TaskDoesNotExist {
        Task task = taskService.getById(taskID);
        String id = taskService.createSubTask(task,title);
       // task.getSubtasks().add(new SubTask(task,title));
        task = taskService.getById(taskID);
        if(pollConsumers.containsKey(task.getId()))
        {
            for(DeferredResult<List<SubTask>> dr  : pollConsumers.get(task.getId())){
                dr.setResult(task.getSubtasks());
            }
            pollConsumers.get(task.getId()).clear();
        }
        return boardService.findByKey(id);
    }
}
