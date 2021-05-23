package rebound.apps.caoticdreams.engine.content;

import static rebound.math.SmallIntegerMathUtilities.*;
import static rebound.testing.WidespreadTestingUtilities.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.allowedoperations.ReadonlyValue;
import rebound.apps.caoticdreams.caos.library.content.iWorld;
import rebound.apps.caoticdreams.caos.library.core.CaosNamedVariableBank;
import rebound.apps.caoticdreams.engine.AgentVM;
import rebound.apps.caoticdreams.engine.Scriptorium;
import rebound.apps.caoticdreams.engine.mapalgorithms.MapTracker;
import rebound.apps.caoticdreams.engine.physics.MapAsbPhysicsEngine;
import rebound.apps.caoticdreams.engine.rendering.Renderable;
import rebound.apps.caoticdreams.engine.rendering.RenderableWorld;
import rebound.util.collections.AccessibleArrayList;
import rebound.util.collections.Slice;
import rebound.util.container.ContainerInterfaces.BooleanContainer;
import rebound.util.container.SimpleContainers.SimpleBooleanContainer;
import rebound.util.functional.ContinueSignal;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

/**
 * Everything in here is saved to / loaded from disk :3
 */
public class CDWorld
implements iWorld, RenderableWorld, MapAsbPhysicsEngine
{
	protected MapTracker<CDMetaroom> metarooms;
	protected MapTracker<AgentVM> worldAgents;
	protected Set<AgentVM> worldlessAgents = new HashSet<>();  //always unrendered and unphysicked X3
	protected CaosNamedVariableBank gameVariables = new CaosNamedVariableBank();
	
	protected Scriptorium scriptorium;
	
	
	public Scriptorium getScriptorium()
	{
		return scriptorium;
	}
	
	
	
	public void getAllAgents(@Nonnull UnaryFunction<AgentVM, ContinueSignal> observeAgent)
	{
		for (AgentVM agent : worldlessAgents)
			if (observeAgent.f(agent) == ContinueSignal.Stop)
				return;
		
		worldAgents.getAllTrackables(observeAgent);
	}
	
	
	
	
	
	protected final AccessibleArrayList<AgentVM> agentSortingBufferForRendering = new AccessibleArrayList<>();
	protected final Comparator<Object> agentZOrderComparator = (a, b) -> cmp(((AgentVM)a).getContent().getZPlane(), ((AgentVM)b).getContent().getZPlane());
	
	public void getRenderablesInZOrder(@ReadonlyValue Rectangle2D regionInWorldMap, @Nullable Integer onlyThisMetaroom, @Nonnull UnaryFunction<Renderable, ContinueSignal> observeRenderable)
	{
		//Metaroom backgrounds are *always* first (bottom) in Z-Order :33
		//But their Z-Order amongst each other is arbitrary (they really shouldn't overlap! XD )
		
		//Metaroom backgrounds!
		{
			BooleanContainer stop_C = new SimpleBooleanContainer(false);
			
			metarooms.getTrackablesInRegion(regionInWorldMap, m ->
			{
				Renderable r = m.background;
				
				if (r != null)
				{
					ContinueSignal c = observeRenderable.f(r);
					if (c == ContinueSignal.Stop)
						stop_C.set(true);
					return c;
				}
				else
				{
					return ContinueSignal.Continue;
				}
			});
			
			if (stop_C.get())
				return;
		}
		
		
		//Agents!
		{
			agentSortingBufferForRendering.clear();
			
			worldAgents.getTrackablesInRegion(regionInWorldMap, a ->
			{
				if (a.getContent().getRenderable() != null)
					agentSortingBufferForRendering.add(a);
				
				return ContinueSignal.Continue;
			});
			
			Slice<Object[]> array = agentSortingBufferForRendering.getLiveContiguousArrayBackingUNSAFE();
			Object[] u = array.getUnderlying();
			int o = array.getOffset();
			int l = array.getLength();
			
			Arrays.sort(u, o, o+l, agentZOrderComparator);  //Todo check performance on this ^^'''    Note that there are many other ways to do it that don't involve array sorting on each frame ^^''', from the MapTracker handling this, to using a separate MapTracker for each plane used!!
			
			for (int i = 0; i < l; i++)
			{
				AgentVM a = (AgentVM) u[o+i];
				Renderable r = a.getContent().getRenderable();
				asrt(r != null);  //we already filtered away null-renderable (invisible) agents!
				
				if (observeRenderable.f(r) == ContinueSignal.Stop)
					return;
			}
		}
	}
}
