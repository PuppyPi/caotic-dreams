package rebound.apps.caoticdreams.caos.evaluation.interpreter.util;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.content.iEngine;
import rebound.apps.caoticdreams.caos.library.content.iWorld;
import rebound.apps.caoticdreams.caos.library.core.iAgentVM;

public class SimpleCaosExecutionContext
implements CaosExecutionContext
{
	protected final Object[] _Px_;
	protected final Object from;
	
	protected final iAgentVM ownr;
	protected final iWorld world;
	protected final iEngine engine;
	
	public SimpleCaosExecutionContext(Object[] _Px_, Object from, iAgentVM ownr, iWorld world, iEngine engine)
	{
		this._Px_ = _Px_;
		this.from = from;
		this.ownr = ownr;
		this.world = world;
		this.engine = engine;
	}
	
	
	//TODO _Px_ and vaxx defaults to integer 0 not null!
	//TODO Handle auto-expanding _Px_ and vaXX!
}
