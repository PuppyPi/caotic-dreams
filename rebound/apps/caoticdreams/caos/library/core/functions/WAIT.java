package rebound.apps.caoticdreams.caos.library.core.functions;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.ImplicitSLOW;
import rebound.apps.caoticdreams.caos.library.StandardCAOS;
import rebound.apps.caoticdreams.caos.library.core.AgentVMWaitState;

@ImplicitSLOW
@StandardCAOS
public class WAIT
{
	public static void f(CaosExecutionContext context, int nTicks)
	{
		context.getOWNR().applyWaitState(new AgentVMWaitState()
		{
			//TODO make it persistable XD''
			
			int nTicksRemaining = nTicks;
			
			@Override
			public boolean tick()
			{
				boolean done = nTicksRemaining == 0;
				
				if (!done)
					nTicksRemaining--;
				
				return done;
			}
		});
		
		context.setINST(false);
	}
}
