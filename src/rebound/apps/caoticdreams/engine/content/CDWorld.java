package rebound.apps.caoticdreams.engine.content;

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.allowedoperations.ReadonlyValue;
import rebound.apps.caoticdreams.caos.library.content.iWorld;
import rebound.apps.caoticdreams.engine.AgentVM;
import rebound.apps.caoticdreams.engine.Scriptorium;
import rebound.apps.caoticdreams.engine.rendering.Renderable;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

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
	
	
	public void getRenderablesInZOrder(@ReadonlyValue Rectangle2D regionInWorldMap, @Nullable Integer onlyThisMetaroom, @Nonnull UnaryFunction<Renderable, ContinueSignal> observeRenderable)
	{
		
	}
}
