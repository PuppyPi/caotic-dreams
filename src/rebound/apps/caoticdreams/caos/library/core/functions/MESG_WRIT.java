package rebound.apps.caoticdreams.caos.library.core.functions;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.LegacyMessageToScriptNumbers;
import rebound.apps.caoticdreams.caos.library.StandardCAOS;

@StandardCAOS
public class MESG_WRIT
{
	public static void f(CaosExecutionContext context, int messageNumber)
	{
		int eventNumber = LegacyMessageToScriptNumbers.getScriptNumberForMessageNumber(messageNumber);
		
		context.getOWNR().message(context.getOWNR(), eventNumber, false, false, false, new Object[]{}, null, context.getWorld().getConflictResolutionForLegacyMessaging());
	}
}
