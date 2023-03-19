package server.exceptions;

public class ListDoesNotExist extends Exception {
	static final long serialVersionUID = -5441151293132995948L;

	public ListDoesNotExist(String s) {
		super(s);
	}
}
