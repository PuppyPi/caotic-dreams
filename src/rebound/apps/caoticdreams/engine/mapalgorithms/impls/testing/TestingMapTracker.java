package rebound.apps.caoticdreams.engine.mapalgorithms.impls.testing;

import static java.util.Objects.*;
import static rebound.util.collections.CollectionUtilities.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import rebound.annotations.semantic.simpledata.NonnullKeys;
import rebound.annotations.semantic.simpledata.NullableValues;
import rebound.apps.caoticdreams.engine.mapalgorithms.MapTrackableLiason;
import rebound.apps.caoticdreams.engine.mapalgorithms.MapTracker;
import rebound.exceptions.NoSuchMappingReturnPath;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

/**
 * The idea of this is to test *both* the caller of a {@link MapTracker} and callee (the actual {@link MapTracker}) for spec-correctness!
 * Namely to ensure that if the {@link MapTracker} doesn't cache the locations and update its cache on {@link MapTracker#trackableUpdated(Object)}, that, when wrapped with one of these, it will do that!
 * And then you can see bugs in the caller's *usage* that might otherwise be invisible, by making all implementations work the same :3
 * 
 * (namely, not calling {@link MapTracker#trackableAdded(Object)} / {@link MapTracker#trackableRemoved(Object)} / {@link MapTracker#trackableUpdated(Object)} like you should, which might work with a lenient impl of {@link MapTracker} but not work with another one!  When wrapped with this, then you'll see the same bugs—namely exceptions and {@link MapTracker#getTrackablesInRegion(Rectangle2D, UnaryFunction)} silently returning things wrongly based on old data from when the last time {@link MapTracker#trackableUpdated(Object)} was called ^^' )
 * 
 * This all comes at the expense of performance (memory and speed) at least a little.
 */
public class TestingMapTracker<T>
implements MapTracker<T>
{
	protected MapTracker<T> underlyingMapTracker;
	protected MapTrackableLiason<T> actualLiason;
	protected @NonnullKeys @NullableValues Map<T, Rectangle2D> officialLocations = new HashMap<>();  //The whole point of this class (well the main point XD) is that we cache the locations so that a MapTracker that refreshes itself in getRenderablesInZOrder() using its liason (eg, NaiveMapTracker) *won't* be refreshed, so it will work the same as a map tracker that does cache it in some way (eg, QuadTreeMapTracker), and you can see if the callER of the MapTracker is calling trackableUpdated() like it should!! :3
	
	protected TestingMapTracker()
	{
	}
	
	
	public static <T> TestingMapTracker<T> makeTestingTracker(UnaryFunction<MapTrackableLiason<T>, MapTracker<T>> makeMapTracker, MapTrackableLiason<T> actualLiason)
	{
		TestingMapTracker<T> tt = new TestingMapTracker<>();
		tt.actualLiason = actualLiason;
		tt.underlyingMapTracker = requireNonNull(makeMapTracker.f(tt.asLiason()));
		return tt;
	}
	
	protected MapTrackableLiason<T> asLiason()
	{
		return t ->
		{
			try
			{
				return getrp(officialLocations, t);
			}
			catch (NoSuchMappingReturnPath exc)
			{
				throw new AssertionError("The MapTracker impl requested the location for a trackable that was not currently tracked!!");
			}
		};
	}
	
	
	protected void checkContainsesMatch(T trackable)
	{
		boolean expected = officialLocations.containsKey(trackable);
		
		if (underlyingMapTracker.containsTrackable(trackable) != expected)
			throw new AssertionError("The MapTracker impl failed!  It's contains() returned "+(!expected)+" when it should have returned "+expected);
	}
	
	@Override
	public void trackableAdded(T trackable)
	{
		checkContainsesMatch(trackable);
		officialLocations.put(trackable, actualLiason.getPresence(trackable));  //do this first because the fake liason the underlying tracker sees uses it!
		underlyingMapTracker.trackableAdded(trackable);
		checkContainsesMatch(trackable);
		
		//check idempotence :3
		underlyingMapTracker.trackableAdded(trackable);
		checkContainsesMatch(trackable);
	}
	
	@Override
	public void trackableRemoved(T trackable)
	{
		checkContainsesMatch(trackable);
		officialLocations.remove(trackable);  //do this first because the fake liason the underlying tracker sees uses it!
		underlyingMapTracker.trackableRemoved(trackable);
		checkContainsesMatch(trackable);
		
		//check idempotence :3
		underlyingMapTracker.trackableRemoved(trackable);
		checkContainsesMatch(trackable);
	}
	
	@Override
	public void trackableUpdated(T trackable)
	{
		checkContainsesMatch(trackable);  //check this before in case the below fails (since then we'd never get to this!)
		
		if (!officialLocations.containsKey(trackable))
			throw new IllegalArgumentException("This trackable has not been added yet!");
		
		
		officialLocations.put(trackable, actualLiason.getPresence(trackable));  //do this first because the fake liason the underlying tracker sees uses it!
		underlyingMapTracker.trackableUpdated(trackable);
	}
	
	
	@Override
	public void getTrackablesInRegion(Rectangle2D regionInMap, UnaryFunction<T, ContinueSignal> observeTrackable)
	{
		underlyingMapTracker.getTrackablesInRegion(regionInMap, observeTrackable);
	}
	
	@Override
	public boolean containsTrackable(T trackable)
	{
		checkContainsesMatch(trackable);
		return officialLocations.containsKey(trackable);
	}
}
