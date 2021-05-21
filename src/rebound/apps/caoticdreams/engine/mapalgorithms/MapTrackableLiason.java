package rebound.apps.caoticdreams.engine.mapalgorithms;

import java.awt.geom.Rectangle2D;
import javax.annotation.Nullable;
import rebound.annotations.semantic.reachability.ThrowAwayValue;

public interface MapTrackableLiason<T>
{
	/**
	 * @return null to mean Everywhere (always shows up!), if you want it to be nowhere, just {@link MapTracker#trackableRemoved(Object) remove} it X3
	 */
	@ThrowAwayValue
	public @Nullable Rectangle2D getPresence(T trackable);
}
