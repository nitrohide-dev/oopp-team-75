package server.exceptions;

public class CannotCreateBoard extends Exception {
	static final long serialVersionUID = -3371151293132995948L;

	public CannotCreateBoard(String message) {
		super(message);
	}
}
