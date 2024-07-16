package quill.core.pkg.info;

import java.io.IOException;

/**
 * A utility class for reading and writing various data types in a binary format.
 */
public class StandaloneBinary {
	
    /**
     * Reads a boolean value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the boolean value
     * @throws IOException if an I/O error occurs
     */
    public static boolean Boolean(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return readNext.invoke() != 0;
    }
    
    /**
     * Reads a byte value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the byte value
     * @throws IOException if an I/O error occurs
     */
    public static byte Byte(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return (byte) readNext.invoke();
    }
    
    /**
     * Reads a char value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the char value
     * @throws IOException if an I/O error occurs
     */
    public static char Char(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return (char) (
                (readNext.invoke() & 0xFF) <<  8 |
                (readNext.invoke() & 0xFF)
                );
    }
    
    /**
     * Reads a short value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the short value
     * @throws IOException if an I/O error occurs
     */
    public static short Short(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return (short) (
                (readNext.invoke() & 0xFF) <<  8 |
                (readNext.invoke() & 0xFF)
                );
    }
    
    /**
     * Reads an int value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the int value
     * @throws IOException if an I/O error occurs
     */
    public static int Int(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return (int) (
                (readNext.invoke() & 0xFF) << 24 |
                (readNext.invoke() & 0xFF) << 16 |
                (readNext.invoke() & 0xFF) <<  8 |
                (readNext.invoke() & 0xFF)
                );
    }
    
    /**
     * Reads a float value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the float value
     * @throws IOException if an I/O error occurs
     */
    public static float Float(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return Float.intBitsToFloat(Int(readNext));
    }
    
    /**
     * Reads a long value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the long value
     * @throws IOException if an I/O error occurs
     */
    public static long Long(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return (long) (
                (readNext.invoke() & 0xFF) << 56 |
                (readNext.invoke() & 0xFF) << 48 |
                (readNext.invoke() & 0xFF) << 40 |
                (readNext.invoke() & 0xFF) << 32 |
                (readNext.invoke() & 0xFF) << 24 |
                (readNext.invoke() & 0xFF) << 16 |
                (readNext.invoke() & 0xFF) <<  8 |
                (readNext.invoke() & 0xFF)
                );
    }
    
    /**
     * Reads a double value from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the double value
     * @throws IOException if an I/O error occurs
     */
    public static double Double(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return Double.longBitsToDouble(Long(readNext));
    }
    
    /**
     * Reads a length-prefixed string from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the string value
     * @throws IOException if an I/O error occurs
     */
    public static String LenString(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return String(readNext, Int(readNext));
    }
    
    /**
     * Reads a string of the specified length from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @param length the length of the string
     * @return the string value
     * @throws IOException if an I/O error occurs
     */
    public static String String(StandaloneTIntFunc<IOException> readNext, int length) throws IOException {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = Char(readNext);
        }
        return new String(chars);
    }
    
    /**
     * Reads a length-prefixed byte array from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @return the byte array
     * @throws IOException if an I/O error occurs
     */
    public static byte[] LenBytes(StandaloneTIntFunc<IOException> readNext) throws IOException {
        return Bytes(readNext, Int(readNext));
    }
    
    /**
     * Reads a byte array of the specified length from the given reader function.
     *
     * @param readNext the function to read the next byte
     * @param length the length of the byte array
     * @return the byte array
     * @throws IOException if an I/O error occurs
     */
    public static byte[] Bytes(StandaloneTIntFunc<IOException> readNext, int length) throws IOException {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
        	bytes[i] = Byte(readNext);
        }
        return bytes;
    }
    
    /**
     * Writes a boolean value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the boolean value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Boolean(StandaloneTVoidFunc1Int<IOException> writeNext, boolean value) throws IOException {
    	writeNext.invoke(value ? 1 : 0);
    }
    
    /**
     * Writes a byte value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the byte value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Byte(StandaloneTVoidFunc1Int<IOException> writeNext, byte value) throws IOException {
    	writeNext.invoke(value);
    }
    
    /**
     * Writes a char value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the char value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Char(StandaloneTVoidFunc1Int<IOException> writeNext, char value) throws IOException {
    	writeNext.invoke((value >>  8) & 0xFF);
    	writeNext.invoke((value      ) & 0xFF);
    }
    
    /**
     * Writes a short value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the short value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Short(StandaloneTVoidFunc1Int<IOException> writeNext, short value) throws IOException {
    	writeNext.invoke((value >>  8) & 0xFF);
    	writeNext.invoke((value      ) & 0xFF);
    }
    
    /**
     * Writes an int value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the int value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Int(StandaloneTVoidFunc1Int<IOException> writeNext, int value) throws IOException {
    	writeNext.invoke((value >> 24) & 0xFF);
    	writeNext.invoke((value >> 16) & 0xFF);
    	writeNext.invoke((value >>  8) & 0xFF);
    	writeNext.invoke((value      ) & 0xFF);
    }
    
    /**
     * Writes a float value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the float value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Float(StandaloneTVoidFunc1Int<IOException> writeNext, float value) throws IOException {
        Int(writeNext, Float.floatToIntBits(value));
    }
    
    /**
     * Writes a long value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the long value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Long(StandaloneTVoidFunc1Int<IOException> writeNext, long value) throws IOException {
    	writeNext.invoke((int) ((value >> 56) & 0xFF));
    	writeNext.invoke((int) ((value >> 48) & 0xFF));
    	writeNext.invoke((int) ((value >> 40) & 0xFF));
    	writeNext.invoke((int) ((value >> 32) & 0xFF));
    	writeNext.invoke((int) ((value >> 24) & 0xFF));
    	writeNext.invoke((int) ((value >> 16) & 0xFF));
    	writeNext.invoke((int) ((value >>  8) & 0xFF));
    	writeNext.invoke((int) ((value      ) & 0xFF));
    }
    
    /**
     * Writes a double value using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the double value to write
     * @throws IOException if an I/O error occurs
     */
    public static void Double(StandaloneTVoidFunc1Int<IOException> writeNext, double value) throws IOException {
        Long(writeNext, Double.doubleToLongBits(value));
    }
    
    /**
     * Writes a length-prefixed string using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the string value to write
     * @throws IOException if an I/O error occurs
     */
    public static void LenString(StandaloneTVoidFunc1Int<IOException> writeNext, String value) throws IOException {
        Int(writeNext, value.length());
        String(writeNext, value, value.length());
    }
    
    /**
     * Writes a string of the specified length using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the string value to write
     * @param length the length of the string
     * @throws IOException if an I/O error occurs
     */
    public static void String(StandaloneTVoidFunc1Int<IOException> writeNext, String value, int length) throws IOException {
        for (int i = 0; i < length; i++) {
            Char(writeNext, value.charAt(i));
        }
    }
    
    /**
     * Writes a length-prefixed byte array using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the byte array to write
     * @throws IOException if an I/O error occurs
     */
    public static void LenBytes(StandaloneTVoidFunc1Int<IOException> writeNext, byte[] value) throws IOException {
        Int(writeNext, value.length);
        Bytes(writeNext, value, value.length);
    }
    
    /**
     * Writes a byte array of the specified length using the given writer function.
     *
     * @param writeNext the function to write the next byte
     * @param value the byte array to write
     * @param length the length of the byte array
     * @throws IOException if an I/O error occurs
     */
    public static void Bytes(StandaloneTVoidFunc1Int<IOException> writeNext, byte[] value, int length) throws IOException {
    	for (int i = 0; i < length; i++) {
            Byte(writeNext, value[i]);
        }
    }
    
    @FunctionalInterface
    public static interface StandaloneTIntFunc<T extends Throwable> {
    	
    	int invoke() throws T;
    }
    
    @FunctionalInterface
    public static interface StandaloneTVoidFunc1Int<T extends Throwable> {
    	
    	void invoke(int p1) throws T;
    }
}
