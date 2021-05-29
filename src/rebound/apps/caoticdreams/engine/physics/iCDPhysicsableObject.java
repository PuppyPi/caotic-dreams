package rebound.apps.caoticdreams.engine.physics;

import java.util.List;
import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.library.content.iWorldAgentContent;
import rebound.math.geom2d.ImmutableLineSegment2D;

public interface iCDPhysicsableObject
{
	public double getPositionX();
	public void setPositionX(double value);
	
	public double getPositionY();
	public void setPositionY(double value);
	
	public double getVelocityX();
	public void setVelocityX(double value);
	
	public double getVelocityY();
	public void setVelocityY(double value);
	
	public List<ImmutableLineSegment2D> getBoundary();
	
	/**
	 * Agents with lower perm can pass through borders with higher perm.
	 * 
	 * (Not to be confused with {@link iWorldAgentContent#getZPlane() plane})
	 *  (or Verm)
	 *   (XD)
	 */
	public int getPermeability();
	
	
	
	public double getAccelerationDueToGravity();
	
	/**
	 * Physics isn't something to suffer through >.>
	 * XD
	 */
	public boolean isExperiencingPhysics();
	public boolean isExperiencingCollisions();
	
	public @Nullable iCDPhysicsableObject getFloatRelativeTo();
	
	
	/**
	 * Opaque stuff that only the physics engine know what is :3
	 */
	public Object getPhysicsEngineStuff();
	public void setPhysicsEngineStuff(Object value);
}
