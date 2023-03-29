package server.api.services;

import commons.TaskList;
import org.springframework.stereotype.Service;
import server.database.ListRepository;
import server.exceptions.ListDoesNotExist;

import java.util.List;

@Service
public class ListService {

	private final ListRepository repo;

	public ListService(ListRepository repo) {
		this.repo = repo;
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
	 * @param id the id of the taskList that we want to get
	 * @return the taskList with the given id
	 * @throws ListDoesNotExist when there is no list with the given id in the db
	 */
	public TaskList getById(long id) throws ListDoesNotExist {
		if (!repo.existsById(id))
			throw new ListDoesNotExist("There is no list with this id.");
		return repo.findById(id).get();
	}

//	/**
//	 * Creates a taskList from a given model
//	 * @param model - model containing name and board for the taskList
//	 * @return The newly created taskList
//	 * @throws CannotCreateList - when there is already a taskList with the given name
//	 */
//	public TaskList createList(TaskListModel model) throws CannotCreateList {
//		Board board = model.board;
//		boolean usedName = board.getTaskLists().stream()
//			.map(TaskList::getTitle)
//			.anyMatch(title -> title.equals(model.name));
//		if (usedName) {
//			throw new CannotCreateList("This list is already in the board.");
//		}
//		TaskList taskList = board.createTaskList(model.name);
//		return listRepository.save(taskList);
//	}

	/**
	 * Deletes a taskList from the database
	 * @param id - the key of the taskList that we want to delete
	 * @throws ListDoesNotExist - when there is no list with the given id in the db
	 */
	public void deleteById(long id) throws ListDoesNotExist {
		if (!repo.existsById(id))
			throw new ListDoesNotExist("There is no list with the provided id.");
		repo.deleteById(id);
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
