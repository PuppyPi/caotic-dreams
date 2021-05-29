package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

public class LiveCaosExpressionReturnValueDiscardingOperation
extends LiveCaosOperation
{
	protected LiveCaosExpression expression;
	
	public LiveCaosExpressionReturnValueDiscardingOperation(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	public LiveCaosExpression getExpression()
	{
		return expression;
	}
	
	public void setExpression(LiveCaosExpression expression)
	{
		this.expression = expression;
	}
	
	
	
	@Override
	public void run(CaosExecutionContext context) throws RuntimeException
	{
		expression.evaluate(context);  //discard the return-value since that's what we are :3
	}
}
