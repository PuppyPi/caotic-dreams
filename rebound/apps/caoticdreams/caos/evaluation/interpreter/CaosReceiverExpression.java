package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.evaluation.CaosRuntimeException;

/**
 * aka L-Value expression
 */
public abstract class CaosReceiverExpression
{
	public abstract void set(CaosExecutionContext context, Object value) throws CaosRuntimeException;
}
