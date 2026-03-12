package fluff.core.lib.info;

import fluff.core.lib.LibraryException;

/**
 * Utility class for transforming values during library information processing.
 *
 * @param <F> the type of the original value
 * @param <T> the type of the transformed value
 */
public class LibraryInfoTransform<F, T> {
    
    protected final LibraryInfoTransform<F, T> parent;
    protected final F value;
    
    /**
     * Constructs a new LibraryInfoTransform with the specified parent and value.
     *
     * @param parent the parent transform
     * @param value the value to transform
     */
    public LibraryInfoTransform(LibraryInfoTransform<F, T> parent, F value) {
        this.parent = parent;
        this.value = value;
    }
    
    /**
     * Specifies a condition and transformation function for the value.
     *
     * @param ifFunc the condition function
     * @param resultFunc the transformation function
     * @return a LibraryInfoTransformIf instance
     * @throws LibraryException if an error occurs during the transformation
     */
    public LibraryInfoTransformIf<F, T> If(ExceptionFunction<Boolean, F> ifFunc, ExceptionFunction<T, F> resultFunc) throws LibraryException {
        return new LibraryInfoTransformIf<>(this, value, ifFunc, resultFunc);
    }
    
    /**
     * Transforms the value to the specified type.
     *
     * @param clazz the class representing the desired type
     * @param <V> the type to transform to
     * @return a LibraryInfoTransform instance with the transformed value
     * @throws LibraryException if an error occurs during the transformation
     */
    public <V> LibraryInfoTransform<T, V> transform(Class<V> clazz) throws LibraryException {
        return new LibraryInfoTransform<>(null, Result());
    }
    
    /**
     * Retrieves the transformed value.
     *
     * @return the transformed value
     * @throws LibraryException if an error occurs during the transformation
     */
    public T Result() throws LibraryException {
        return parent != null ? parent.Result() : null;
    }
}
