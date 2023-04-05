package server.api.services;

import commons.SubTask;
import org.springframework.stereotype.Service;
import server.database.SubTaskRepository;
import server.exceptions.SubTaskDoesNotExist;
import java.util.Set;
@Service
public class SubTaskService {
    private final SubTaskRepository repo;


    public SubTaskService(SubTaskRepository repo) {
        this.repo = repo;
    }
    /**
     * Creates a subtask in the database from a title string
     * @param title - the name of the subtask
     * @return The newly created subtask
     */
    public SubTask createSubTask(String title){
        SubTask SubTask = new SubTask(title);
        return repo.save(SubTask);
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
    public Set<SubTask> getAllSubTasksOfTask(long task_id){
        Set<SubTask> allSubTasks = (Set<SubTask>) repo.getSubTasksOfTask(task_id);
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
        repo.deleteById(id);
    }
}