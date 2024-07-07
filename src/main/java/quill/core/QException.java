package quill.core;

public class QException extends Exception {
	
	private static final long serialVersionUID = 3301116320213646131L;
	
    public QException() {
        super();
    }
    
    public QException(String message) {
        super(message);
    }
    
    public QException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public QException(Throwable cause) {
        super(cause);
    }
}
