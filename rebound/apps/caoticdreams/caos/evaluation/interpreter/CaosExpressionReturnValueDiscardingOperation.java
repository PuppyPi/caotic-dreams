package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

public class CaosExpressionReturnValueDiscardingOperation
extends CaosOperation
{
	protected CaosExpression expression;
	
	public CaosExpressionReturnValueDiscardingOperation(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	public CaosExpression getExpression()
	{
		return expression;
	}
	
	public void setExpression(CaosExpression expression)
	{
		this.expression = expression;
	}
	
	
	
	@Override
	public void run(CaosExecutionContext context) throws RuntimeException
	{
		expression.evaluate(context);  //discard the return-value since that's what we are :3
	}
}
