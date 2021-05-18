package rebound.apps.caoticdreams.caos.library.core.functions;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.LegacyMessageToScriptNumbers;
import rebound.apps.caoticdreams.caos.library.StandardCAOS;
import rebound.apps.caoticdreams.caos.library.content.PersistableTask;

@StandardCAOS
public class MESG_WRTp
{
	public static void f(CaosExecutionContext context, int messageNumber, Object p1, Object p2, int delay)
	{
		int eventNumber = LegacyMessageToScriptNumbers.getScriptNumberForMessageNumber(messageNumber);
		MESG_WRTp_PersistableTask task = new MESG_WRTp_PersistableTask(context, eventNumber, new Object[]{p1, p2});
		
		if (delay <= 0)
			task.run();
		else
			context.getWorld().scheduleTask(delay, task);
	}
	
	
	
	/**
	 * Needs to be able to be saved to / loaded from disk!
	 */
	public static class MESG_WRTp_PersistableTask
	implements PersistableTask
	{
		protected final CaosExecutionContext context;
		protected final int eventNumber;
		protected final Object[] parameters;
		
		public MESG_WRTp_PersistableTask(CaosExecutionContext context, int eventNumber, Object[] parameters)
		{
			this.context = context;
			this.eventNumber = eventNumber;
			this.parameters = parameters;
		}
		
		@Override
		public void run()
		{
			context.getOWNR().message(context.getOWNR(), eventNumber, false, false, false, parameters, null, context.getWorld().getConflictResolutionForLegacyMessaging());
		}
	}
}
