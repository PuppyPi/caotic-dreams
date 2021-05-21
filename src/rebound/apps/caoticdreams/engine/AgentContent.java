package rebound.apps.caoticdreams.engine;

import java.util.Map;
import rebound.apps.caoticdreams.caos.library.content.iWorldAgentContent;
import rebound.apps.caoticdreams.caos.library.core.iCoreAgentContent;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsableObject;
import rebound.apps.caoticdreams.engine.rendering.Renderable;

public class AgentContent
implements iCoreAgentContent, iWorldAgentContent, iCDPhysicsableObject
{
	protected final int family, genus, species;
	
	protected Object[] objectVariables;  //TODO MAKE SURE TO DEFAULT THEM TO 0(LONG) AND AUTO-EXPAND
	protected Map<String, Object> nameVariables;  //TODO MAKE SURE TO DEFAULT THEM TO 0(LONG)
	
	
	public AgentContent(int family, int genus, int species)
	{
		this.family = family;
		this.genus = genus;
		this.species = species;
	}
	
	
	
	public int getZPlane()
	{
		
	}
	
	/**
	 * @return null if it's invisible :3
	 */
	public @Nullable Renderable getRenderable()
	{
		
	}
}
