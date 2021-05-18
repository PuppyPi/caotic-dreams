package rebound.apps.caoticdreams.caos.library.core;

import static java.util.Objects.*;
import rebound.util.functional.FunctionInterfaces.UnaryFunction;

public interface AgentMessageSendingConflictResolution
{
	/**
	 * A la normal creatures X'D
	 */
	public static enum AgentMessageSendingConflictResolutionOverwriteEntirety
	implements AgentMessageSendingConflictResolution
	{
		I
	}
	
	/**
	 * Our new way ;D
	 */
	public static enum AgentMessageSendingConflictResolutionWaitForCurrent
	implements AgentMessageSendingConflictResolution
	{
		I
	}
	
	
	/**
	 * A la CALL
	 */
	public static class AgentMessageSendingConflictResolutionInterruptAndPushCurrent
	implements AgentMessageSendingConflictResolution
	{
		protected final UnaryFunction<EventScriptTerminationType, Boolean> push;
		
		public AgentMessageSendingConflictResolutionInterruptAndPushCurrent(UnaryFunction<EventScriptTerminationType, Boolean> push)
		{
			this.push = requireNonNull(push);
		}
		
		public boolean shouldResumeIfNewTerminatesLikeSo(EventScriptTerminationType newEventTerminationType)
		{
			return push.f(newEventTerminationType);
		}
	}
}
