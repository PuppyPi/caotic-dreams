package rebound.apps.caoticdreams.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.library.content.iWorldAgentContent;
import rebound.apps.caoticdreams.caos.library.core.iCoreAgentContent;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsableObject;
import rebound.apps.caoticdreams.engine.rendering.Renderable;
import rebound.exceptions.NotYetImplementedException;

public class AgentContent
implements iCoreAgentContent, iWorldAgentContent, iCDPhysicsableObject
{
	protected final int family, genus, species;
	protected List<Object> objectVariables;
	protected Map<Object, Object> nameVariables;
	
	protected int zPlane;
	protected @Nullable Renderable renderable;
	
	public AgentContent(int family, int genus, int species)
	{
		this.family = family;
		this.genus = genus;
		this.species = species;
	}
	
	
	@Override
	public boolean isCurrentlyAnimating()
	{
		//TODO
		throw new NotYetImplementedException();
	}
	
	
	
	
	public int getFamily()
	{
		return family;
	}
	
	public int getGenus()
	{
		return genus;
	}
	
	public int getSpecies()
	{
		return species;
	}
	
	/**
	 * @return null if it's invisible :3
	 */
	public @Nullable Renderable getRenderable()
	{
		return renderable;
	}
	
	public List<Object> getObjectVariables()
	{
		List<Object> m = objectVariables;
		if (m == null)
		{
			m = new ArrayList<>(100);
			objectVariables = m;
		}
		return objectVariables;
	}
	
	public Map<Object, Object> getNameVariables()
	{
		Map<Object, Object> m = nameVariables;
		if (m == null)
		{
			m = new HashMap<>();
			nameVariables = m;
		}
		return nameVariables;
	}
	
	public int getZPlane()
	{
		return zPlane;
	}
	
	public void setZPlane(int zPlane)
	{
		this.zPlane = zPlane;
	}
	
	public void setRenderable(Renderable renderable)
	{
		this.renderable = renderable;
	}
}
