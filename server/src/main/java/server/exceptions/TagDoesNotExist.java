package server.exceptions;

public class TagDoesNotExist extends Exception {
    static final long serialVersionUID = -3371151291196995087L;

    public TagDoesNotExist(String message) {
        super(message);
    }

}
