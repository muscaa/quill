package fluff.core.lib;

/**
 * Exception thrown for errors related to library management and resolution.
 */
public class LibraryException extends Exception {
    
    private static final long serialVersionUID = -8193416847606969193L;
    
    /**
     * Constructs a new LibraryException.
     */
    public LibraryException() {
        super();
    }
    
    /**
     * Constructs a new LibraryException with the specified detail message.
     *
     * @param message the detail message.
     */
    public LibraryException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new LibraryException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause.
     */
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new LibraryException with the specified cause.
     *
     * @param cause the cause.
     */
    public LibraryException(Throwable cause) {
        super(cause);
    }
}
