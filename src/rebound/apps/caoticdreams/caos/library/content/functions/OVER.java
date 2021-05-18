package rebound.apps.caoticdreams.caos.library.content.functions;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.ImplicitSLOW;
import rebound.apps.caoticdreams.caos.library.StandardCAOS;
import rebound.apps.caoticdreams.caos.library.core.iAgentVM;

@StandardCAOS
@ImplicitSLOW
public class OVER
{
	public static void f(CaosExecutionContext context)
	{
		iAgentVM ownr = context.getOWNR();
		
		ownr.applyWaitState(() ->
		{
			return !ownr.getContent().isCurrentlyAnimating();
		});
		
		context.setINST(false);
	}
}
