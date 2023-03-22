package server.exceptions;

public class CannotCreateTask extends Exception {
	static final long serialVersionUID = -3371779291196995948L;

	public CannotCreateTask(String message) {
		super(message);
	}
}
