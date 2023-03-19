package server.exceptions;

public class TaskDoesNotExist extends Exception {
	static final long serialVersionUID = -3371151291196995007L;

	public TaskDoesNotExist(String message) {
		super(message);
	}

}
