package rebound.apps.caoticdreams.engine.physics.impls.local;

import rebound.annotations.semantic.operationspecification.IdentityHashableType;
import rebound.apps.caoticdreams.engine.physics.MapAsbPhysicsEngine;
import rebound.apps.caoticdreams.engine.physics.MapAsbPhysicsEngine.CollidableRegionAsbPhysicsEngine;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsEngine;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsableObject;

public class LocalOnlyCDPhysicsEngine
implements iCDPhysicsEngine
{
	protected final MapAsbPhysicsEngine map;
	
	public LocalOnlyCDPhysicsEngine(MapAsbPhysicsEngine map)
	{
		this.map = map;
	}
	
	
	
	
	
	public void objectPhysicsPropertiesUpdated(iCDPhysicsableObject obj)
	{
		Object restingPossiblyAffectedListener = obj.getPhysicsEngineStuff();
		
		if (restingPossiblyAffectedListener != null)
			((Runnable)restingPossiblyAffectedListener).run();
	}
	
	public void tickObject(iCDPhysicsableObject obj)
	{
		if (obj.isExperiencingPhysics())
		{
			if (obj.getPhysicsEngineStuff() == null)
			{
				//Do the actual physics tick! XD :D
				CollidableRegionAsbPhysicsEngine newGround = tickObjectCoreCode(obj);
				
				
				if (newGround != null)
				{
					//We're either newly resting or still resting on ground after a map update in our region that turned out not to affect us :3
					
					@IdentityHashableType
					class Listener
					implements Runnable
					{
						@Override
						public void run()
						{
							obj.setPhysicsEngineStuff(null);
							map.unregisterMapUpdateListener(newGround, this);
						}
					}
					
					Runnable onMapUpdate = new Listener();
					
					map.registerMapUpdateListener(newGround, onMapUpdate);
					
					obj.setPhysicsEngineStuff(onMapUpdate);
				}
			}
		}
	}
	
	
	
	
	/**
	 * @return non-null if we're now at rest after the tick!
	 */
	protected CollidableRegionAsbPhysicsEngine tickObjectCoreCode(iCDPhysicsableObject obj)
	{
		//TODO :D
	}
	
	
	
	
	
	public MapAsbPhysicsEngine getMap()
	{
		return map;
	}
}
