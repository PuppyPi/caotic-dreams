package rebound.apps.caoticdreams.engine;

import java.util.HashSet;
import java.util.Set;
import rebound.apps.caoticdreams.caos.library.content.iWorld;

/**
 * Everything in here is saved to / loaded from disk :3
 */
public class CDWorld
implements iWorld
{
	protected Set<AgentVM> agents = new HashSet<>(100000);
	protected Scriptorium scriptorium;
	
	public Set<AgentVM> getAgents()
	{
		return agents;
	}
	
	public Scriptorium getScriptorium()
	{
		return scriptorium;
	}
}
