package rebound.apps.caoticdreams.engine.mapalgorithms.impls.naive;

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;
import rebound.apps.caoticdreams.engine.mapalgorithms.MapTrackableLiason;
import rebound.apps.caoticdreams.engine.mapalgorithms.MapTracker;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

public class NaiveMapTracker<T>
implements MapTracker<T>
{
	protected Set<T> trackables = new HashSet<>();
	protected MapTrackableLiason liason;
	
	public NaiveMapTracker(MapTrackableLiason liason)
	{
		this.liason = liason;
	}
	
	
	@Override
	public void trackableAdded(T trackable)
	{
		trackables.add(trackable);
	}
	
	@Override
	public void trackableRemoved(T trackable)
	{
		trackables.remove(trackable);
	}
	
	@Override
	public void trackableUpdated(T trackable)
	{
		//nothing XD'
	}
	
	@Override
	public void getTrackablesInRegion(Rectangle2D regionInMap, UnaryFunction<T, ContinueSignal> observeTrackable)
	{
		for (T trackable : trackables)
		{
			Rectangle2D r = liason.getPresence(trackable);
			
			if (r == null || regionInMap == null || r.intersects(regionInMap))  //Rectangle2D.intersects() means "intersects-or-contains"—I checked :3
			{
				if (observeTrackable.f(trackable) == ContinueSignal.Stop)
					break;
			}
		}
	}
	
	@Override
	public boolean containsTrackable(T trackable)
	{
		return trackables.contains(trackable);
	}
}
