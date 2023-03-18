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

	private final BoardRepository boardRepository;

	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	/**
	 * Gets all boards from the database
	 * @return list of all boards
	 */
	public List<Board> getAllBoards() {
		return boardRepository.findAll();
	}

	/**
	 * Finds a board by a given key
	 * @param key - the key of the board that we want to get
	 * @return the board with the given key
	 * @throws BoardDoesNotExist - when there is no board with the given key in the db
	 */
	public Board getBoardByKey(String key) throws BoardDoesNotExist {
		if (boardRepository.existsById(key)) {
			return boardRepository.getById(key);
		}
		else throw new BoardDoesNotExist("There is no board with the given key.");
	}

	/**
	 * Creates a board from a given model
	 * @param model - model containing key, name and boolean for the board
	 * @return The newly created board
	 * @throws CannotCreateBoard - when the model is not valid or there is already a board with the given key
	 */
	public Board createBoard(CreateBoardModel model) throws CannotCreateBoard {
		if (!model.isValid()) throw new CannotCreateBoard("Some of the provided fields are invalid.");
		if (boardRepository.existsById(model.key)) throw new CannotCreateBoard("This key is already used.");
		return boardRepository.save(new Board(model.name, model.key, model.locked));
	}

	/**
	 * Deletes a board from the database
	 * @param key - the key of the board that we want to delete
	 * @throws BoardDoesNotExist - when there is no board with the given key
	 */
	public void deleteBoardByKey(String key) throws BoardDoesNotExist {
		if (key == null || !boardRepository.existsById(key))
			throw new BoardDoesNotExist("There is no board with the given key.");
		boardRepository.deleteById(key);
	}
}
