package server.api.services;

import commons.Board;
import commons.TaskList;
import commons.models.CreateListModel;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.database.ListRepository;
import server.exceptions.CannotCreateList;
import server.exceptions.ListDoesNotExist;

import java.util.List;

@Service
public class ListService {

	private final ListRepository listRepository;
	private final BoardRepository boardRepository;

	public ListService(ListRepository listRepository, BoardRepository boardRepository) {
		this.listRepository = listRepository;
		this.boardRepository = boardRepository;
	}

	public List<TaskList> getAllLists() {
		return listRepository.findAll();
	}

	public TaskList getById(long id) throws ListDoesNotExist {
		if (!listRepository.existsById(id))
			throw new ListDoesNotExist("There is no list with this id.");
		return listRepository.findById(id).get();
	}

	public TaskList createList(CreateListModel model) throws CannotCreateList {
		Board board = model.board;
		boolean usedName = board.getTaskLists().stream()
			.map(list -> list.getTitle())
			.anyMatch(title -> title.equals(model.name));
		if (usedName) {
			throw new CannotCreateList("This list is already in the board.");
		}
		TaskList taskList = board.createTaskList(model.name);
		boardRepository.save(board);
		return taskList;
	}

	public void deleteListById(long id) throws ListDoesNotExist {
		if (!listRepository.existsById(id))
			throw new ListDoesNotExist("There is no list with the provided id.");
		listRepository.deleteById(id);
	}
}
