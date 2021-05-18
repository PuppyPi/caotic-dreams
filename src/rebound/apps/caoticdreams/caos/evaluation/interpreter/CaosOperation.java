package rebound.apps.caoticdreams.caos.evaluation.interpreter;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

public abstract class CaosOperation
extends CaosStatement
{
	protected CaosStatement next;
	
	public CaosOperation(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	
	
	public abstract void run(CaosExecutionContext context) throws RuntimeException;
	
	
	
	public CaosStatement getNext()
	{
		return next;
	}
	
	public void setNext(CaosStatement next)
	{
		this.next = next;
	}
}
