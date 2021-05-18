package rebound.apps.caoticdreams.caos.library.core;

import rebound.annotations.semantic.temporal.Mutable;

/**
 * May be a mutable or immutable object!
 */
@Mutable
public interface AgentVMWaitState
{
	/**
	 * @return true if we should run, otherwise false if we should keep waiting.
	 */
	public boolean tick();
}
