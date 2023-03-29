package server.exceptions;

public class SubTaskDoesNotExist extends Exception {
    static final long serialVersionUID = -3371151232196995087L;

    public SubTaskDoesNotExist(String message) {
        super(message);
    }

}
