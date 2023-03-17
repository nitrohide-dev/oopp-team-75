package server.exceptions;

public class BoardDoesNotExist extends Exception {
	static final long serialVersionUID = -3371151293132425948L;

	/**
	 * Constructor for the exception.
	 * @param message the error message
	 */
	public BoardDoesNotExist(String message) {
		super(message);
	}

}
