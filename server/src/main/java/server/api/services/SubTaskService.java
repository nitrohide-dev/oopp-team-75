package server.api.services;

import commons.Board;
import commons.SubTask;
import commons.Task;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.SubTaskRepository;
import server.database.TaskRepository;
import server.exceptions.SubTaskDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.List;

@Service
public class SubTaskService {

    private final SubTaskRepository repo;
    private final TaskRepository taskRepo;
    private final BoardRepository boardRepo;

    public SubTaskService(SubTaskRepository repo, TaskRepository taskRepo, BoardRepository boardRepo) {
        this.repo = repo;
        this.taskRepo = taskRepo;
        this.boardRepo = boardRepo;
    }

    /**
     * gets a subtask from the database by its id
     * @param id - the id of the subtask
     * @return The SubTask object corresponding to it
     * @throws SubTaskDoesNotExist when there is no subtask for the given subtask id
     */
    public SubTask getById(long id) throws SubTaskDoesNotExist {
        if (!repo.existsById(id))
            throw new SubTaskDoesNotExist("There exists no subtask with the provided id.");
        return repo.findById(id).get();
    }

    /**
     * gets all subtasks of a task from the database
     * @param task_id - the id of the task to look up subtasks for
     * @return a set of Subtasks of the task
     */
    public List<SubTask> getAllSubTasksOfTask(long task_id) throws TaskDoesNotExist {
        List<SubTask> allSubTasks = repo.getSubTasksOfTask(task_id);
        if (allSubTasks.isEmpty())
            throw new TaskDoesNotExist("There exists no task for the provided task id.");
        return allSubTasks;
    }

    /**
     * delete a subtask from the database by its id
     * @param id - the id of the subtask
     * @throws SubTaskDoesNotExist when there is no subtask for the given subtask id already
     */
    public void deleteById(long id) throws SubTaskDoesNotExist {
        if (!repo.existsById(id))
            throw new SubTaskDoesNotExist("There is no subtask with the provided id.");
        System.out.println("service");
        SubTask subTask = repo.findById(id).get();
        Task task = subTask.getTask();
        task.removeSubTask(subTask);
        taskRepo.save(task);
    }
    /**
     * renames a subtask
     * @param id - the id of the subtask
     * @param name - the new name of the subtask
     * @throws SubTaskDoesNotExist when there is no subtask for the given subtask id already
     */

    public void renameSubTask(long id,String name) throws SubTaskDoesNotExist {
        if (!repo.existsById(id))
            throw new SubTaskDoesNotExist("There exists no subtask with the provided id.");
        SubTask subTask = getById(id);
        subTask.setTitle(name);
        repo.save(subTask);
    }

    public List<SubTask> getAll(){
        return repo.findAll();
    }

    public void changeCheckSubTask(Long id) throws SubTaskDoesNotExist {
        if (!repo.existsById(id))
            throw new SubTaskDoesNotExist("There exists no subtask with the provided id.");
        SubTask subTask = getById(id);
        subTask.setChecked(!subTask.getChecked());
        repo.save(subTask);
    }
    public Board movesubTaskUp(int order, long subtaskId){
        SubTask subTask = repo.findById(subtaskId).get();
        if(order!=0) {
            repo.movesubTaskUp1(order-1);
            repo.movesubTaskUp2(subtaskId);
        }
        return boardRepo.findById(subTask.getTask().getTaskList().getBoard().getKey()).get();
    }
    public Board movesubTaskDown(int order, long subtaskId){
        SubTask subTask = repo.findById(subtaskId).get();
        repo.movesubTaskDown1(order+1);
        repo.movesubTaskDown2(subtaskId);
        return boardRepo.findById(subTask.getTask().getTaskList().getBoard().getKey()).get();
    }
}
