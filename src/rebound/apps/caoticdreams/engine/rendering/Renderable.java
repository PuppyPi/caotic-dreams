package rebound.apps.caoticdreams.engine.rendering;

import java.awt.geom.Point2D;
import rebound.annotations.semantic.reachability.ThrowAwayValue;

public interface Renderable
{
	/**
	 * In the world map's coordinates for toplevel things, offset from the parent's one for pieces of compound things.
	 */
	@ThrowAwayValue
	public Point2D getRenderingOffset();
}
