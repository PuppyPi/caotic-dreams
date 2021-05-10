package rebound.apps.caoticdreams.caos.library.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.library.content.iWorldAgentContent;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionWaitForCurrent;
import rebound.util.functional.FunctionInterfaces.UnaryProcedure;

/**
 * Core agent things!
 */
public interface iAgentVM
{
	public iWorldAgentContent getContent();
	
	
	/**
	 * It's the caller's responsibility to call {@link #isLOCKed()} and decide what to do then!
	 * 
	 * + Note that the script/event number is looked up immediately and if the scriptorium changes by the time this executes (if the {@link AgentMessageSendingConflictResolutionWaitForCurrent the queue} is used), then the old script—the one it maps to right now—is used, so no messages on the queue silently disappear (this having returned true (script number found) but later script number not being found).
	 * 		Unless deferredEventNumberLookup is true, in which case if this returns false now nothing is queued, but if this returns true and there's no script for that number later, then the message will be silently discarded
	 * 
	 * @return if that eventNumber was found.  if false this does nothing at all (not even call the callback!)
	 */
	public boolean message(@Nullable Object from, int eventNumber, boolean deferredEventNumberLookup, boolean inst, boolean lock, @Nonnull Object[] params, @Nullable UnaryProcedure<EventScriptTerminationType> callbackOnCompletion, @Nonnull AgentMessageSendingConflictResolution conflictResolution);
	
	
	
	/**
	 * Note that this may return false between finishing a script and starting the next one on the {@link #getSizeOfPendingMessageQueue() queue}, so combined with that function you can really check if the agent is quiescent :3
	 */
	public boolean isInEventScript();
	
	/**
	 * @return null if the agent is not executing any scripts
	 */
	public @Nullable Integer getCurrentlyExecutingEventScriptNumber();
	
	public int getSizeOfPendingMessageQueue();
	
	/**
	 * Pauses the currently-running script (and any {@link AgentMessageSendingConflictResolutionWaitForCurrent pending scripts on the queue}!) until this returns true :3
	 */
	public void applyWaitState(AgentVMWaitState waitState);
	
	public void stop(EventScriptTerminationType terminationType);
	
	
	
	/**
	 * This doesn't actually do anything directly XD
	 * It's up to those who call {@link #message(Object, int, boolean, boolean, Object[], UnaryProcedure, AgentMessageSendingConflictResolution)} to check this and alter their behavior as appropriate!
	 */
	public boolean isLOCKed();
	public void setLOCKed(boolean value);
}
