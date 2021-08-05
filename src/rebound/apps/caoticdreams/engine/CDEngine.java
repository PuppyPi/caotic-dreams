package rebound.apps.caoticdreams.engine;

import rebound.apps.caoticdreams.caos.library.content.iEngine;
import rebound.apps.caoticdreams.caos.library.content.iWorld;
import rebound.apps.caoticdreams.caos.library.core.CaosNamedVariableBank;
import rebound.apps.caoticdreams.engine.content.CDWorld;
import rebound.apps.caoticdreams.engine.physics.iCDPhysicsEngine;
import rebound.util.functional.ContinueSignal;


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
	protected CDWorld currentWorld;
	protected CaosNamedVariableBank eameVariables = new CaosNamedVariableBank();
	protected Runnable closeWindowAndShutdown;
	
	public void tick()
	{
		tickPhysics();
		tickAgentVMs();
	}
	
	public void tickPhysics()
	{
		currentWorld.getAllAgents(agent ->
		{
			physicsEngine.tickObject(agent.getContent());
			return ContinueSignal.Continue;
		});
	}
	
	public void tickAgentVMs()
	{
		currentWorld.getAllAgents(agent ->
		{
			agent.tickCaos();
			return ContinueSignal.Continue;
		});
	}
	
	
	
	
	public void handleErrorInAgentEventScript(AgentVM agent, Throwable t)
	{
		//TODO pop up the GUI or something :3
		t.printStackTrace();
	}
	
	
	
	
	
	
	
	
	public void userInputMouseButtonStateChanged(int buttonIndex, boolean newState)
	{
		//TODO
	}
	
	public void userInputMousePresenceStateChanged(boolean newState)
	{
		//TODO
	}
	
	public void userInputKeyStateChanged(int keyCode, boolean newState)
	{
		//TODO
	}
	
	public void userInputFocusAndKeyboardStateChanged(boolean newState)
	{
		//TODO
	}
	
	public void userInputWindowCloseClicked()
	{
		//TODO
	}
	
	
	
	
	
	
	
	@Override
	public void loadWorld(String uid) throws Throwable
	{
		//TODO
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public iCDPhysicsEngine getPhysicsEngine()
	{
		return physicsEngine;
	}
	
	@Override
	public CDWorld getCurrentWorld()
	{
		return currentWorld;
	}
	
	public Runnable getCloseWindowAndShutdown()
	{
		return closeWindowAndShutdown;
	}
	
	public void setCloseWindowAndShutdown(Runnable closeWindowAndShutdown)
	{
		this.closeWindowAndShutdown = closeWindowAndShutdown;
	}
}
