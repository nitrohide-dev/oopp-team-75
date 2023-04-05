package server.api.services;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.ListRepository;
import server.database.TaskRepository;
import server.exceptions.ListDoesNotExist;

import java.util.List;

@Service
public class ListService {

	private final ListRepository repo;
	private final TaskRepository taskRepo;
	private final BoardRepository boardRepo;
	public ListService(ListRepository repo,TaskRepository taskRepo,BoardRepository boardRepo) {
		this.repo = repo;
		this.taskRepo = taskRepo;
		this.boardRepo = boardRepo;
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
		if (repo.findById(id).isEmpty())
			throw new ListDoesNotExist("There is no list with this id.");
		return repo.findById(id).get();
	}

	/**
	 * changes the name of the list with the given id in the database
	 * @param id - the id of the list
	 * @param name - the new name of the list
	 * @return the list with the given id
	 * @throws ListDoesNotExist when there is no list with the given id in the db
	 */
	public TaskList renameList(long id,String name) throws ListDoesNotExist {
		if (!repo.existsById(id))
			throw new ListDoesNotExist("There is no list with this id.");
		TaskList list = getById(id);
		list.setTitle(name);
		repo.save(list);
		return list;
	}
	/**
	 * Deletes a taskList from the database
	 * @param id - the id of the taskList that we want to delete
	 * @throws ListDoesNotExist - when there is no list with the given id in the db
	 */
	public Board deleteById(long id) throws ListDoesNotExist {
		if (!repo.existsById(id))
	     	throw new ListDoesNotExist("There is no list with the provided id.");
		TaskList list = getById(id);
        Board board = list.getBoard();
		board.getTaskLists().remove(list);
		boardRepo.save(board);
		return board;
	}
	/**
	 * creates a task with the given name in the database
	 * @param list the list in which the task is
	 * @param name - the name of the task
	 * @return the key of the board in which the task is
	 */
	public String createTask(TaskList list,String name)  {
		Task task = list.createTask();
		task.setTitle(name);
		repo.save(list);
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
