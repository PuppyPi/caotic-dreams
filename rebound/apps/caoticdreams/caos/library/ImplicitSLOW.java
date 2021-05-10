package rebound.apps.caoticdreams.caos.library;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Knowing which functions do this is crucial for the compiler, which can only compile INST code!!
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ImplicitSLOW
{
}
