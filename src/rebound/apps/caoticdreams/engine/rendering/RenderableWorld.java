package rebound.apps.caoticdreams.engine.rendering;

import java.awt.geom.Rectangle2D;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.allowedoperations.ReadonlyValue;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

public interface RenderableWorld
{
	public void getRenderablesInZOrder(@ReadonlyValue Rectangle2D regionInWorldMap, @Nullable Integer onlyThisMetaroom, @Nonnull UnaryFunction<Renderable, ContinueSignal> observeRenderable);
}
