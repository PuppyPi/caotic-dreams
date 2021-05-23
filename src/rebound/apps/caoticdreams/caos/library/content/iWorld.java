package rebound.apps.caoticdreams.caos.library.content;

import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionOverwriteEntirety;

public interface iWorld
{
	/**
	 * WUID
	 */
	public String getWorldUID();
	
	public Object getGameVariable(Object key);
	public void setGameVariable(Object key, Object value);
	
	/**
	 * The standard is {@link AgentMessageSendingConflictResolutionOverwriteEntirety}
	 */
	public AgentMessageSendingConflictResolution getConflictResolutionForLegacyMessaging();
	
	
	public void scheduleTask(int delayInTicks, PersistableTask task);
}
