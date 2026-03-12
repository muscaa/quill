package fluff.core.lib.v1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method should be called when loading a library.
 * This annotation is used to mark the main method(s) of a library that needs to be executed
 * during the library's initialization process.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LibraryMain {
    
}
