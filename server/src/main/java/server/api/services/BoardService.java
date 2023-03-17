package server.api.services;

import commons.Board;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;
import server.exceptions.BoardDoesNotExist;
import server.exceptions.CannotCreateBoard;

import java.util.List;
import java.util.Random;

@Service
public class BoardService {

	private final BoardRepository boardRepository;
	private final Random random;

	public BoardService(Random random, BoardRepository boardRepository) {
		this.random = random;
		this.boardRepository = boardRepository;
	}

	public List<Board> getAllBoards() {
		return boardRepository.findAll();
	}

	public Board getBoardByKey(String key) throws BoardDoesNotExist {
		if (boardRepository.existsById(key)) {
			return boardRepository.getById(key);
		}
		else throw new BoardDoesNotExist("There is no board with the given key.");
	}

	public Board createBoard() throws CannotCreateBoard {
		String key = Board.generateKey(random);
		while (boardRepository.existsById(key)) key = Board.generateKey(random);
		return boardRepository.save(new Board(key));
	}

	public void deleteBoardByKey(String key) throws BoardDoesNotExist {
		if (key == null || !boardRepository.existsById(key))
			throw new BoardDoesNotExist("There is no board with the given key.");
		boardRepository.deleteById(key);
	}
}
