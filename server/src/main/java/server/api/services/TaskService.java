package server.api.services;

import commons.Task;
import commons.TaskList;
import org.springframework.stereotype.Service;
import server.database.ListRepository;
import server.database.TaskRepository;
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
	 * @return the key of the board in which the task is
	 */
	public String deleteById(long id) throws TaskDoesNotExist {
		if (!repo.existsById(id))
			throw new TaskDoesNotExist("There is no task with the provided id.");
		Task task = getById(id);
		TaskList list = task.getTaskList();
		list.getTasks().remove(task);
		listRepo.save(list);
		return listRepo.getBoardByListID(list.getId());
	}

	/**
	 * moves a task to the target list and places it in a specific place
	 * @param task the task to move
	 * @param targetlist the list to which the task should be moved
	 * @param order the place in the new list which the task should occupy
     * @throws TaskDoesNotExist if the task with the given id doesn't exist in the database
	 */
	public void moveTask(Task task,TaskList targetlist,int order) throws TaskDoesNotExist
	{
		if (!repo.existsById(task.getId()))
			throw new TaskDoesNotExist("There is no task with the provided id.");
		int initOrder = repo.getOrderById(task.getId());
		repo.updateInitialListOrder(initOrder,task.getId());
		repo.updateTargetListOrder(order,targetlist.getId());
		repo.moveTask(task.getId(),targetlist.getId(),order);
		System.out.println(3);
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
