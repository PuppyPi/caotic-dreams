package rebound.apps.caoticdreams.caos.library.content;

import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.library.core.iCoreAgentContent;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsableObject;

public interface iWorldAgentContent
extends iCoreAgentContent, iCDPhysicsableObject
{
	public void setAccelerationDueToGravity(double value);
	public void setSufferingCollisions(boolean value);
	public void setFloatRelativeTo(@Nullable iCDPhysicsableObject value);
	
	/**
	 * For OVER
	 */
	public boolean isCurrentlyAnimating();
	
	public int getZPlane();
	public void setZPlane(int plne);
}
