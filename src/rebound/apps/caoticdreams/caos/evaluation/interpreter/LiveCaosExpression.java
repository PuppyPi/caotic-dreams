package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

/**
 * aka R-Value expression
 */
public interface LiveCaosExpression
{
	public Object evaluate(CaosExecutionContext context) throws RuntimeException;
}
