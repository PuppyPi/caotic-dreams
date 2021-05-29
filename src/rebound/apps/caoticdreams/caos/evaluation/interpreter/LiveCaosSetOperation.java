package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

public class LiveCaosSetOperation
extends LiveCaosOperation
{
	protected LiveCaosReceiverExpression receiverExpression;
	protected LiveCaosExpression providerExpression;
	
	public LiveCaosSetOperation(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	public LiveCaosReceiverExpression getReceiverExpression()
	{
		return receiverExpression;
	}
	
	public void setReceiverExpression(LiveCaosReceiverExpression receiverExpression)
	{
		this.receiverExpression = receiverExpression;
	}
	
	public LiveCaosExpression getProviderExpression()
	{
		return providerExpression;
	}
	
	public void setProviderExpression(LiveCaosExpression providerExpression)
	{
		this.providerExpression = providerExpression;
	}
	
	
	
	
	@Override
	public void run(CaosExecutionContext context) throws RuntimeException
	{
		receiverExpression.set(context, providerExpression.evaluate(context));
	}
}
