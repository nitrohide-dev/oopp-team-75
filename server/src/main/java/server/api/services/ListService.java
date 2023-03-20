package server.api.services;

import commons.Board;
import commons.TaskList;
import commons.models.CreateListModel;
import org.springframework.stereotype.Service;
import server.database.ListRepository;
import server.exceptions.CannotCreateList;
import server.exceptions.ListDoesNotExist;

import java.util.List;

@Service
public class ListService {

	private final ListRepository listRepository;

	public ListService(ListRepository listRepository) {
		this.listRepository = listRepository;
	}

	/**
	 * Gets all taskLists from the database
	 * @return list of all taskLists
	 */
	public List<TaskList> getAllLists() {
		return listRepository.findAll();
	}

	/**
	 * Finds a taskList by a given id
	 * @param id - the id of the taskList that we want to get
	 * @return the taskList with the given id
	 * @throws ListDoesNotExist - when there is no list with the given id in the db
	 */
	public TaskList getById(long id) throws ListDoesNotExist {
		if (!listRepository.existsById(id))
			throw new ListDoesNotExist("There is no list with this id.");
		return listRepository.findById(id).get();
	}

	/**
	 * Creates a taskList from a given model
	 * @param model - model containing name and board for the taskList
	 * @return The newly created taskList
	 * @throws CannotCreateList - when there is already a taskList with the given name
	 */
	public TaskList createList(CreateListModel model) throws CannotCreateList {
		Board board = model.board;
		boolean usedName = board.getTaskLists().stream()
			.map(list -> list.getTitle())
			.anyMatch(title -> title.equals(model.name));
		if (usedName) {
			throw new CannotCreateList("This list is already in the board.");
		}
		TaskList taskList = board.createTaskList(model.name);
		return listRepository.save(taskList);
	}

	/**
	 * Deletes a taskList from the database
	 * @param id - the key of the taskList that we want to delete
	 * @throws ListDoesNotExist - when there is no list with the given id in the db
	 */
	public void deleteListById(long id) throws ListDoesNotExist {
		if (!listRepository.existsById(id))
			throw new ListDoesNotExist("There is no list with the provided id.");
		listRepository.deleteById(id);
	}
}
