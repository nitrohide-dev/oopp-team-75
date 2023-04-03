package server.api.services;

import commons.Board;
import commons.Task;
import commons.TaskList;
import commons.TaskMoveModel;
import org.springframework.stereotype.Service;
import server.database.ListRepository;
import server.database.TaskRepository;
import server.exceptions.ListDoesNotExist;
import server.exceptions.TaskDoesNotExist;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListService {

	private final ListRepository repo;
	private final TaskRepository taskRepo;

	public ListService(ListRepository repo,TaskRepository taskRepo) {
		this.repo = repo;
		this.taskRepo = taskRepo;
	}

	/**
	 * Gets all taskLists from the database
	 * @return list of all taskLists
	 */
	public List<TaskList> getAll() {
		return repo.findAll();
	}

	/**
	 * Finds a taskList by a given id
	 * @return the taskList with the given id
	 * @throws ListDoesNotExist when there is no list with the given id in the db
	 */
	public TaskList getById(Long id) throws ListDoesNotExist {
		if (repo.findById(id)==null)
			throw new ListDoesNotExist("There is no list with this id.");
		return repo.findById(id).get();
	}

	public TaskList renameList(long id,String name) throws ListDoesNotExist {
		if (!repo.existsById(id))
			throw new ListDoesNotExist("There is no list with this id.");
		repo.renameList(id,name);
		return repo.findById(id).get();
	}
	/**
	 * Deletes a taskList from the database
	 * @param id - the key of the taskList that we want to delete
	 * @throws ListDoesNotExist - when there is no list with the given id in the db
	 */
	public Board deleteById(long id) throws ListDoesNotExist {
		TaskList list = getById(id);
		Board board = list.getBoard();
		board.removeTaskList(list);
		list.setTasks(new ArrayList<Task>());
		repo.save(list);
		if (!repo.existsById(id))
			throw new ListDoesNotExist("There is no list with the provided id.");
		repo.deleteList(id);
		return board;
	}
	public Board createList(Board board){
		TaskList list = new TaskList(board);
		repo.save(list);
		return board;
	}
	public String createTask(TaskList list,String name)  {
		Task task = list.createTask();
		task.setTitle(name);
		taskRepo.save(task);
		return repo.getBoardByListID(list.getid());
	}
	/**
	 * Saves a taskList to the database.
	 * @param taskList the taskList to save
	 * @return the saved taskList
	 */
	public TaskList save(TaskList taskList) {
		return repo.save(taskList);
	}
}
