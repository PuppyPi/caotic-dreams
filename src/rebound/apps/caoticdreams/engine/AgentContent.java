package rebound.apps.caoticdreams.engine;

import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.library.content.iWorldAgentContent;
import rebound.apps.caoticdreams.caos.library.core.CaosIndexedVariableBank;
import rebound.apps.caoticdreams.caos.library.core.CaosNamedVariableBank;
import rebound.apps.caoticdreams.caos.library.core.iCoreAgentContent;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsableObject;
import rebound.apps.caoticdreams.engine.rendering.Renderable;

public class AgentContent
implements iCoreAgentContent, iWorldAgentContent, iCDPhysicsableObject
{
	protected final int family, genus, species;
	
	protected CaosIndexedVariableBank objectVariables = new CaosIndexedVariableBank();
	protected CaosNamedVariableBank nameVariables = new CaosNamedVariableBank();
	
	
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
