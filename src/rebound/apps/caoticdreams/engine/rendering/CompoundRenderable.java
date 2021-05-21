package rebound.apps.caoticdreams.engine.rendering;

import java.awt.geom.Rectangle2D;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

public interface CompoundRenderable
extends Renderable
{
	/**
	 * @param r  In this thing's {@link #getRenderingOffset() coordinate system} or null for everything it has!
	 */
	public void getChildrenFor(@Nullable Rectangle2D r, @Nonnull UnaryFunction<Renderable, ContinueSignal> observeChildRenderable);
	
	
	public default void getAllChildren(@Nonnull UnaryFunction<Renderable, ContinueSignal> observeChildRenderable)
	{
		getChildrenFor(null, observeChildRenderable);
	}
}
