package server.api.services;

import commons.Task;
import commons.TaskList;
import commons.TaskMoveModel;
import org.springframework.stereotype.Service;
import server.database.TaskRepository;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.List;

@Service
public class TaskService {

	private final TaskRepository repo;

	public TaskService(TaskRepository repo) {
		this.repo = repo;
	}

	/**
	 * Gets all tasks from the database
	 * @return list of all tasks
	 */
	public List<Task> getAll() {
		return repo.findAll();
	}

	/**
	 * Finds a task by a given id
	 * @param id - the id of the task that we want to get
	 * @return the task with the given id
	 * @throws TaskDoesNotExist - when there is no task with the given id in the db
	 */
	public Task getById(long id) throws TaskDoesNotExist {
		if (!repo.existsById(id))
			throw new TaskDoesNotExist("There exists no task with the provided id.");
		return repo.findById(id).get();
	}
   public Task renameTask(long id,String name) throws TaskDoesNotExist{
	   if (!repo.existsById(id))
		   throw new TaskDoesNotExist("There exists no task with the provided id.");
	   repo.renameTask(id,name);
	   return repo.findById(id).get();
   }
	/**
	 *
	 * moves a task from one list to another by changing the corresponding values and relations in the repository.
	 * @param model the TaskMoveModel containing all the necessary informating for the move
	 * @return the successfully moved task
	 * @throws TaskDoesNotExist - when there is no task with the given id
	 */
	public Task moveTask(TaskMoveModel model) throws TaskDoesNotExist {
		repo.updateInitialListOrder(model.getOld_task_order(),model.getTask_id());
		repo.updateTargetListOrder(model.getNew_task_order(),model.getTasklist().getid());
		repo.moveTask(model.getTask_id(),model.getTasklist().getid(),model.getNew_task_order());
		return repo.findById(model.getTask_id()).get();
	}


	/**
	 * Deletes a task from the database
	 * @param id - the id of the task that we want to delete
	 * @throws TaskDoesNotExist - when there is no task with the given id
	 */
	public void deleteById(long id) throws TaskDoesNotExist {
		if (!repo.existsById(id))
			throw new TaskDoesNotExist("There is no task with the provided id.");
		repo.deleteById(id);
	}


	/**
	 * Saves a task to the database.
	 * @param task the task to save
	 * @return the saved task
	 */
	public Task save(Task task) {
		return repo.save(task);
	}

}
