package server.api.services;

import commons.Board;
import commons.Tag;
import commons.models.CreateBoardModel;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;

import java.util.List;
import java.util.Optional;

@Service
public class 	BoardService {

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
	 * @return the board with the given key, or null if it doesn't exist
	 */
	public Board findByKey(String key) {
		if (key == null)
			throw new IllegalArgumentException("key cannot be null");
		Optional<Board> board = repo.findById(key);
		if (board.isEmpty() || board.get() == null) {
			return null;
		}
		return board.get();
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
		return repo.save(new Board(model));
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
	 * Creates a list in the database
	 * @param board - the board the list is in
	 * @return the board the list is in
	 */
	public Board createList(Board board) {
		board.createTaskList();
		repo.save(board);
		return repo.findById(board.getKey()).get();
	}

	/**
	 * Creates a list in the database with a given id
	 * @param board - the board the list is in
	 * @param id - the id of the list
	 * @return the board the list is in
	 */
	public Board createList(Board board, long id) {
		board.createTaskList(id);
		repo.save(board);
		return repo.findById(board.getKey()).get();
	}

	/**
	 * Creates a list in the database with a given id and name
	 * @param board - the board the list is in
	 * @param id - the id of the list
	 * @param name - the name of the list
	 * @return
	 */
	public Board createList(Board board, long id, String name) {
		board.createTaskList(id, name);
		repo.save(board);
		return repo.findById(board.getKey()).get();
	}

	/**
	 * Saves a board to the database.
	 * @param board the board to save
	 * @return the saved board
	 */
	public Board save(Board board) throws BoardDoesNotExist {
		if (board == null)
			throw new BoardDoesNotExist("There is no board to be saved");
		return repo.save(board);
	}

	/**
	 * Creates a tag in the database
	 * @param boardKey the key of the board
	 * @param name the name of the tag
	 * @return the newly created tag
	 */
	public Tag createTag(String boardKey, String name) {
		Board board = findByKey(boardKey);
		Tag tag = board.createTag(name);
		repo.save(board);
		return tag;
	}
}
