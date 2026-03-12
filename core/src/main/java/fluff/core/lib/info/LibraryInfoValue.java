package fluff.core.lib.info;

import fluff.core.lib.LibraryException;

/**
 * Represents a value parsed from library information.
 */
public class LibraryInfoValue {
    
    private final String value;
    
    /**
     * Constructs a new LibraryInfoValue with the specified value.
     *
     * @param value the value to be represented
     */
    public LibraryInfoValue(String value) {
        this.value = value;
    }
    
    /**
     * Transforms the value to the specified type.
     *
     * @param clazz the class representing the desired type
     * @param <V> the type to transform to
     * @return a LibraryInfoTransform instance with the transformed value
     */
    public <V> LibraryInfoTransform<String, V> transform(Class<V> clazz) {
        return new LibraryInfoTransform<>(null, value);
    }
    
    /**
     * Retrieves the value as a string.
     *
     * @return the value as a string
     */
    public String String() {
        return value;
    }
    
    /**
     * Retrieves the value as a boolean.
     *
     * @return the value as a boolean
     * @throws LibraryException if the value cannot be parsed as a boolean
     */
    public boolean Boolean() throws LibraryException {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            throw new LibraryException(e);
        }
    }
    
    /**
     * Retrieves the value as an integer.
     *
     * @return the value as an integer
     * @throws LibraryException if the value cannot be parsed as an integer
     */
    public int Int() throws LibraryException {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new LibraryException(e);
        }
    }
    
    /**
     * Retrieves the value as a float.
     *
     * @return the value as a float
     * @throws LibraryException if the value cannot be parsed as a float
     */
    public float Float() throws LibraryException {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            throw new LibraryException(e);
        }
    }
    
    /**
     * Retrieves the value as a long.
     *
     * @return the value as a long
     * @throws LibraryException if the value cannot be parsed as a long
     */
    public long Long() throws LibraryException {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new LibraryException(e);
        }
    }
    
    /**
     * Retrieves the value as a double.
     *
     * @return the value as a double
     * @throws LibraryException if the value cannot be parsed as a double
     */
    public double Double() throws LibraryException {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            throw new LibraryException(e);
        }
    }
    
    /**
     * Checks if the value is null.
     *
     * @return true if the value is null, false otherwise
     */
    public boolean isNull() {
        return value == null;
    }
    
    /**
     * Checks if the value is not null.
     *
     * @return true if the value is not null, false otherwise
     */
    public boolean isNotNull() {
        return value != null;
    }
}
