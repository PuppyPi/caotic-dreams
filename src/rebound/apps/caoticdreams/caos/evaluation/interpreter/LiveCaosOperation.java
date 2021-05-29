package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

public abstract class LiveCaosOperation
extends LiveCaosStatement
{
	protected LiveCaosStatement next;
	
	public LiveCaosOperation(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	
	
	public abstract void run(CaosExecutionContext context) throws RuntimeException;
	
	
	
	public LiveCaosStatement getNext()
	{
		return next;
	}
	
	public void setNext(LiveCaosStatement next)
	{
		this.next = next;
	}
}
