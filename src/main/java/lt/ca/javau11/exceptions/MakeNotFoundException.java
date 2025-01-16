package lt.ca.javau11.exceptions;

public class MakeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MakeNotFoundException(String message) {
        super(message);
    }
}
