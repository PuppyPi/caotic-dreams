package rebound.apps.caoticdreams.caos.library.core.functions;

import static rebound.util.BasicExceptionUtilities.*;
import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.DreamCAOS;
import rebound.apps.caoticdreams.caos.library.ImplicitSLOW;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionWaitForCurrent;
import rebound.apps.caoticdreams.caos.library.core.EventScriptTerminationType;
import rebound.util.container.ContainerInterfaces.BooleanContainer;
import rebound.util.container.SimpleContainers.SimpleBooleanContainer;
import rebound.util.functional.FunctionInterfaces.UnaryProcedure;

/**
 * CALL on TARG instead of OWNR!!  ;DD
 * I always wanted that XD :'>
 * 		—PP, 2021-05-10 04:51:10 z
 * 
 * This always does an implicit SLOW and uses the Message Queue if TARG is currently executing a script (so we wait patiently until ours finishes :3 )
 * However, if we were in an INST upon calling this, then the new script will immediately be in an INST once it does eventually start!  And same with the LOCK state.
 * 
 * Sets the ov variable 'returnValueVariable' to 0 if the called script succeeded normally, 1 if it was STOPped/STPTed, 2 if it failed in error, and 3 if it was interrupted and destroyed by another message or something
 */
@DreamCAOS
@ImplicitSLOW
public class CALT
{
	public static void f(CaosExecutionContext context, int eventNumber, Object p1, Object p2, int returnValueVariable)
	{
		BooleanContainer finished_C = new SimpleBooleanContainer(false);
		
		UnaryProcedure<EventScriptTerminationType> callbackOnCompletion = t ->
		{
			finished_C.set(true);
			
			int v;
			{
				if (t == EventScriptTerminationType.Success)
					v = 0;
				else if (t == EventScriptTerminationType.StopRequested)
					v = 1;
				else if (t == EventScriptTerminationType.Error)
					v = 2;
				else if (t == EventScriptTerminationType.Overwritten)
					v = 3;
				else
					throw newUnexpectedHardcodedEnumValueExceptionOrNullPointerException(t);
			}
			
			context.getOWNR().getContent().setOVxx(returnValueVariable, v);
		};
		
		context.getTARG().message(context.getOWNR(), eventNumber, false, context.isINST(), context.getOWNR().isLOCKed(), new Object[]{p1, p2}, callbackOnCompletion, AgentMessageSendingConflictResolutionWaitForCurrent.I);
		
		context.getOWNR().applyWaitState(() -> finished_C.get());
		
		context.setINST(false);
	}
}
