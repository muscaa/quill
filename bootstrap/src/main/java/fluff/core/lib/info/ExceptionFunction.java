package fluff.core.lib.info;

/**
 * Functional interface for representing a function that may throw an Exception.
 *
 * @param <R> the type of the result
 * @param <V> the type of the input value
 */
@FunctionalInterface
public interface ExceptionFunction<R, V> {
    
    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     * @throws Exception if an error occurs during the function application
     */
    R invoke(V value) throws Exception;
}
