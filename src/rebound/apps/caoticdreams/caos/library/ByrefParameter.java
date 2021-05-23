package rebound.apps.caoticdreams.caos.library;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import rebound.util.container.ContainerInterfaces.ObjectContainer;

/**
 * The parameter must then be typed as {@link ObjectContainer}!
 * And an anonymous class might be provided to it as glue code :3
 * (This isn't the fastest thing in the world without JIT inlining..but..honestly, neither is multiple-return-values in Java without JIT stack-allocation X'D )
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ByrefParameter
{
}
