package rebound.apps.caoticdreams.caos.library.core.functions;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.StandardCAOS;
import rebound.apps.caoticdreams.caos.library.core.EventScriptTerminationType;

@StandardCAOS
public class STOP
{
	public static void f(CaosExecutionContext context)
	{
		context.getOWNR().stop(EventScriptTerminationType.StopRequested);
	}
}
