package fluff.core.lib.info;

import fluff.core.lib.LibraryException;

/**
 * Utility class for conditionally transforming values during library information processing.
 *
 * @param <F> the type of the original value
 * @param <T> the type of the transformed value
 */
public class LibraryInfoTransformIf<F, T> extends LibraryInfoTransform<F, T> {
    
    protected final ExceptionFunction<Boolean, F> ifFunc;
    protected final ExceptionFunction<T, F> resultFunc;
    
    /**
     * Constructs a new LibraryInfoTransformIf with the specified parent, value, condition function, and result function.
     *
     * @param parent the parent transform
     * @param value the value to transform
     * @param ifFunc the condition function
     * @param resultFunc the result function
     */
    public LibraryInfoTransformIf(LibraryInfoTransform<F, T> parent, F value, ExceptionFunction<Boolean, F> ifFunc, ExceptionFunction<T, F> resultFunc) {
        super(parent, value);
        
        this.ifFunc = ifFunc;
        this.resultFunc = resultFunc;
    }
    
    /**
     * Specifies the transformation to apply if the condition is not met.
     *
     * @param resultFunc the result function for the else case
     * @return a LibraryInfoTransformIf instance representing the else case
     * @throws LibraryException if an error occurs during the transformation
     */
    public LibraryInfoTransformIf<F, T> Else(ExceptionFunction<T, F> resultFunc) throws LibraryException {
        return new LibraryInfoTransformIf<>(this, value, v -> !ifFunc.invoke(v), resultFunc);
    }
    
    @Override
    public T Result() throws LibraryException {
        try {
            if (ifFunc.invoke(value)) {
                return resultFunc.invoke(value);
            }
        } catch (Exception e) {
            throw new LibraryException(e);
        }
        return super.Result();
    }
}
