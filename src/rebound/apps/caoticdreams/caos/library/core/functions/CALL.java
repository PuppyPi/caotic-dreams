package rebound.apps.caoticdreams.caos.library.core.functions;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.ImplicitSLOW;
import rebound.apps.caoticdreams.caos.library.StandardCAOS;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionInterruptAndPushCurrent;
import rebound.apps.caoticdreams.caos.library.core.EventScriptTerminationType;

@ImplicitSLOW
@StandardCAOS
public class CALL
{
	public static void f(CaosExecutionContext context, int eventNumber, Object p1, Object p2)
	{
		context.getOWNR().message(context.getOWNR(), eventNumber, false, context.isINST(), context.getOWNR().isLOCKed(), new Object[]{p1, p2}, null, new AgentMessageSendingConflictResolutionInterruptAndPushCurrent(t -> t == EventScriptTerminationType.Success));
	}
}
