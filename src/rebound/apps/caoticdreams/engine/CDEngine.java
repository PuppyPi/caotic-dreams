package rebound.apps.caoticdreams.engine;

import rebound.apps.caoticdreams.caos.library.content.iEngine;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsEngine;

/**
 * The Engine is to the World kind of like the AgentVM is to the AgentContent :3
 * The execution state vs. the game-related content
 * Code vs data, kind of :3
 * 
 * (though AgentVM's are saved to disk unlike the Engine)
 */
public class CDEngine
implements iEngine
{
	protected iCDPhysicsEngine physicsEngine;
	protected CDWorld world;
	
	public void tickPhysics()
	{
		for (AgentVM agent : world.getAgents())
			physicsEngine.tickObject(agent.getContent());
	}
	
	public void tickAgentVMs()
	{
		for (AgentVM agent : world.getAgents())
			agent.tickCaos();
	}
	
	
	
	
	public void handleErrorInAgentEventScript(AgentVM agent, Throwable t)
	{
		//TODO pop up the GUI or something :3
	}
	
	
	
	
	public iCDPhysicsEngine getPhysicsEngine()
	{
		return physicsEngine;
	}
	
	public CDWorld getWorld()
	{
		return world;
	}
}
