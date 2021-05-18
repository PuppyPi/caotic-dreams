package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.util.functional.FunctionInterfaces.UnaryProcedure;

public class CaosCompiledOperation
extends CaosOperation
{
	protected UnaryProcedure<CaosExecutionContext> operation;
	
	public CaosCompiledOperation(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	public UnaryProcedure<CaosExecutionContext> getOperation()
	{
		return operation;
	}
	
	public void setOperation(UnaryProcedure<CaosExecutionContext> operation)
	{
		this.operation = operation;
	}
	
	
	@Override
	public void run(CaosExecutionContext context) throws RuntimeException
	{
		operation.f(context);
	}
}
