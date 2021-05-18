package rebound.apps.caoticdreams.engine.physics;

import rebound.annotations.semantic.operationspecification.HashableValue;
import rebound.annotations.semantic.operationspecification.IdentityHashableType;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

public interface MapAsbPhysicsEngine
{
	public void findPossibleCollidableRegionsForObject(double rectX, double rectY, double rectW, double rectH, UnaryFunction<CollidableRegionAsbPhysicsEngine, ContinueSignal> observe);
	
	public void registerMapUpdateListener(CollidableRegionAsbPhysicsEngine region, @HashableValue Runnable onUpdate);
	public void unregisterMapUpdateListener(CollidableRegionAsbPhysicsEngine region, @HashableValue Runnable onUpdate);
	
	
	
	
	@IdentityHashableType
	public static interface CollidableRegionAsbPhysicsEngine
	{
		public void observePolyLines(UnaryFunction<CollidableRegionAsbPhysicsEngine, ContinueSignal> observe);
		
		
		public static interface RegionBoundaryPolylineObserver
		{
			public ContinueSignal observe(double startX, double startY, double endX, double endY, int perm);
		}
	}
}
