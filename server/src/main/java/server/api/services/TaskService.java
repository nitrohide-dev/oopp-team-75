package server.api.services;

import commons.Task;
import commons.TaskList;
import commons.TaskMoveModel;
import org.springframework.stereotype.Service;
import server.database.ListRepository;
import server.database.TaskRepository;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.List;

@Service
public class TaskService {

   private final ListRepository listRepo;
	private final TaskRepository repo;

	public TaskService(TaskRepository repo,ListRepository listRepo) {
		this.repo = repo;
		this.listRepo = listRepo;
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
   public void renameTask(long id,String name) throws TaskDoesNotExist {
	   if (!repo.existsById(id))
		   throw new TaskDoesNotExist("There exists no task with the provided id.");
	   Task task = getById(id);
	   task.setTitle(name);
	   repo.save(task);
   }

	/**
	 * Deletes a task from the database
	 * @param id - the id of the task that we want to delete
	 * @throws TaskDoesNotExist - when there is no task with the given id
	 */
	public void deleteById(long id) throws TaskDoesNotExist {
		if (!repo.existsById(id))
			throw new TaskDoesNotExist("There is no task with the provided id.");
		repo.deleteTask(id);
	}


	/**
	 * Saves a task to the database.
	 * @param task the task to save
	 * @return the saved task
	 */
	public Task save(Task task) {
		return repo.save(task);
	}
     public void moveTask(Task task,TaskList targetlist,int order) throws TaskDoesNotExist
	 {
		 if (!repo.existsById(task.getid()))
			 throw new TaskDoesNotExist("There is no task with the provided id.");
		 int initOrder = repo.getOrderById(task.getid());
		 repo.updateInitialListOrder(initOrder,task.getid());
		 repo.updateTargetListOrder(order,targetlist.getid());
		 repo.moveTask(task.getid(),targetlist.getid(),order);
		 System.out.println(3);
	 }
}
