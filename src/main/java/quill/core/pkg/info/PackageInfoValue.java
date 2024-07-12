package quill.core.pkg.info;

import quill.core.QException;

public class PackageInfoValue {
    
    private final String value;
    
    public PackageInfoValue(String value) {
        this.value = value;
    }
    
    public String String() {
        return value;
    }
    
    public boolean Boolean() throws QException {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            throw new QException(e);
        }
    }
    
    public int Int() throws QException {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new QException(e);
        }
    }
    
    public float Float() throws QException {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            throw new QException(e);
        }
    }
    
    public long Long() throws QException {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new QException(e);
        }
    }
    
    public double Double() throws QException {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            throw new QException(e);
        }
    }
    
    public boolean isNull() {
        return value == null;
    }
    
    public boolean isNotNull() {
        return value != null;
    }
}
