package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

public class CaosSetOperation
extends CaosOperation
{
	protected CaosReceiverExpression receiverExpression;
	protected CaosExpression providerExpression;
	
	public CaosSetOperation(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	public CaosReceiverExpression getReceiverExpression()
	{
		return receiverExpression;
	}
	
	public void setReceiverExpression(CaosReceiverExpression receiverExpression)
	{
		this.receiverExpression = receiverExpression;
	}
	
	public CaosExpression getProviderExpression()
	{
		return providerExpression;
	}
	
	public void setProviderExpression(CaosExpression providerExpression)
	{
		this.providerExpression = providerExpression;
	}
	
	
	
	
	@Override
	public void run(CaosExecutionContext context) throws RuntimeException
	{
		receiverExpression.set(context, providerExpression.evaluate(context));
	}
}
