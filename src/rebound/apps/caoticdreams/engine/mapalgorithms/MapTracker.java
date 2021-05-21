package rebound.apps.caoticdreams.engine.mapalgorithms;

import java.awt.geom.Rectangle2D;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.allowedoperations.ReadonlyValue;
import rebound.annotations.semantic.temporal.IdempotentOperation;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

/**
 * @param <T> must be hashable!  (the default Java "identity hashcode" works fine for this, you just have to make sure hashCode() and equals() are actually implemented like they..heh are supposed to always be implemented in Java XD'' )
 */
public interface MapTracker<T>
{
	public @IdempotentOperation void trackableAdded(@Nonnull T trackable);
	public @IdempotentOperation void trackableRemoved(@Nonnull T trackable);
	public @IdempotentOperation void trackableUpdated(@Nonnull T trackable);
	
	public boolean containsTrackable(@Nonnull T trackable);
	
	
	
	/**
	 * @param regionInMap null means the infinite rectangle—ie, get all the trackables XD
	 */
	public void getTrackablesInRegion(@ReadonlyValue @Nullable Rectangle2D regionInMap, @Nonnull UnaryFunction<T, ContinueSignal> observeTrackable);
	
	public default void getAllTrackables(@Nonnull UnaryFunction<T, ContinueSignal> observeTrackable)
	{
		getTrackablesInRegion(null, observeTrackable);
	}
}
