package rebound.apps.caoticdreams.caos.library.content;

import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution;
import rebound.apps.caoticdreams.caos.library.core.AgentMessageSendingConflictResolution.AgentMessageSendingConflictResolutionOverwriteEntirety;
import rebound.exceptions.AlreadyExistsException;

public interface iWorld
{
	/**
	 * Caos:WUID
	 */
	public String getWorldUID();
	
	/**
	 * @throws AlreadyExistsException if that wuid is already taken by another world!
	 */
	public void setWorldUID(String value) throws AlreadyExistsException;
	
	
	
	public Object getGameVariable(Object key);
	public void setGameVariable(Object key, Object value);
	
	/**
	 * The standard is {@link AgentMessageSendingConflictResolutionOverwriteEntirety}
	 */
	public AgentMessageSendingConflictResolution getConflictResolutionForLegacyMessaging();
	
	
	public void scheduleTask(int delayInTicks, PersistableTask task);
	
	
	
	/**
	 * @throws Throwable if anything is thrown, effectively nothing is done on disk and it fails cleanly :3
	 */
	public void save() throws Throwable;
}
