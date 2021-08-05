package rebound.apps.caoticdreams.caos.library.content;

import java.util.Map;
import javax.annotation.Nonnull;
import rebound.annotations.semantic.temporal.ConstantReturnValue;
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
	
	
	
	/**
	 * See {@link CDApiUtilities#getNamedVariable(Map, Object)} / {@link CDApiUtilities#setNamedVariable(Map, Object, Object)} for how to how to access it in a caotic way (namely imagining infinite variables with value integer-0 instead of absent or null-valued)
	 */
	@ConstantReturnValue
	public @Nonnull Map<Object, Object> getGameVariables();
	
	
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
