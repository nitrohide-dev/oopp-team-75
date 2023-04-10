package server.api.services;

import commons.SubTask;
import commons.Tag;
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

	public TaskService(TaskRepository repo, ListRepository listRepo) {
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

	/**
	 * Checks if it is possible to change the name of a given task and saves it in the database
	 * @param id - the id of the task
	 * @param name - the new name for the task
	 * @throws TaskDoesNotExist - when there is no task with such id
	 */
    public void renameTask(long id, String name) throws TaskDoesNotExist {
	    if (!repo.existsById(id))
		    throw new TaskDoesNotExist("There exists no task with the provided id.");
	    Task task = getById(id);
	    task.setTitle(name);
	    repo.save(task);
    }

	/**
	 * Checks if it is possible to change the description of a given task and saves it in the database
	 * @param id - the id of the task
	 * @param newDesc - the new description for the task
	 * @throws TaskDoesNotExist - when there is no task with such id
	 */
	public void changeTaskDesc(long id, String newDesc) throws TaskDoesNotExist {
		if (!repo.existsById(id))
			throw new TaskDoesNotExist("There exists no task with the provided id.");
		Task task = getById(id);
		task.setDesc(newDesc);
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
	 * @param targetList the list to which the task should be moved
	 * @param order the place in the new list which the task should occupy
     * @throws TaskDoesNotExist if the task with the given id doesn't exist in the database
	 */
	public void moveTask(Task task, TaskList targetList, int order) throws TaskDoesNotExist
	{
		if (!repo.existsById(task.getId()))
			throw new TaskDoesNotExist("There is no task with the provided id.");
		int initOrder = repo.getOrderById(task.getId());
		repo.updateInitialListOrder(initOrder,task.getId());
		repo.updateTargetListOrder(order,targetList.getId());
		repo.moveTask(task.getId(),targetList.getId(),order);
	}

	/**
	 * Adds a tag to a task
	 * @param id the id of the task
	 * @param tag the tag to add
	 * @return the key of the board in which the task is
	 */
	public String addTag(Long id, Tag tag) {
		Task task = repo.findById(id).get();
		task.addTag(tag);
		repo.save(task);
		return listRepo.getBoardByListID(task.getTaskList().getId());
	}

	/**
	 * Saves a task to the database.
	 * @param task the task to save
	 * @return the saved task
	 */
	public Task save(Task task) {
		return repo.save(task);
	}

	/**
	 * Creates a subtask in the database from a title string
	 * @param title - the name of the subtask
	 * @return The key of the board in which the subtask is
	 */
	public String createSubTask(Task task, String title){
		SubTask subTask = task.createSubTask(title);
		repo.save(task);
		return listRepo.getBoardByListID(task.getTaskList().getId());
	}

}
