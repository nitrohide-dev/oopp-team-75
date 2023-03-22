package server.api.services;

import commons.Board;
import commons.models.CreateBoardModel;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;

import java.util.List;

@Service
public class BoardService {

	private final BoardRepository repo;

	public BoardService(BoardRepository repo) {
		this.repo = repo;
	}

	/**
	 * Gets all boards from the database
	 * @return list of all boards
	 */
	public List<Board> getAll() {
		return repo.findAll();
	}

	/**
	 * Finds a board by a given key
	 * @param key the key of the board that we want to get
	 * @return the board with the given key
	 * @throws BoardDoesNotExist when there is no board with the given key in the db
	 */
	public Board getByKey(String key) throws BoardDoesNotExist {
		if (!repo.existsById(key))
			throw new BoardDoesNotExist("There is no board with the given key.");
		return repo.getById(key);
	}

	/**
	 * Creates a board from a given model
	 * @param model model containing key, name and boolean for the board
	 * @return The newly created board
	 * @throws CannotCreateBoard when the model is not valid or there is already a board with the given key
	 */
	public Board create(CreateBoardModel model) throws CannotCreateBoard {
		if (!model.isValid())
			throw new CannotCreateBoard("Some of the provided fields are invalid.");
		if (repo.existsById(model.key))
			throw new CannotCreateBoard("This key is already used.");
		return repo.save(new Board(model.key, model.title, model.password));
	}

	/**
	 * Deletes a board from the database
	 * @param key the key of the board that we want to delete
	 * @throws BoardDoesNotExist when there is no board with the given key
	 */
	public void deleteByKey(String key) throws BoardDoesNotExist {
		if (key == null || !repo.existsById(key))
			throw new BoardDoesNotExist("There is no board with the given key.");
		repo.deleteById(key);
	}

	/**
	 * Saves a board to the database.
	 * @param board the board to save
	 * @return the saved board
	 */
	public Board save(Board board) {
		return repo.save(board);
	}
}
