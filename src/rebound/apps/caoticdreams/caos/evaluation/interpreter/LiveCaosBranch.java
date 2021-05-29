package rebound.apps.caoticdreams.caos.evaluation.interpreter;

public class LiveCaosBranch
extends LiveCaosStatement
{
	protected LiveCaosExpression condition;
	protected LiveCaosStatement nextIfTrue;
	protected LiveCaosStatement nextIfFalse;
	
	public LiveCaosBranch(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	public LiveCaosExpression getCondition()
	{
		return condition;
	}
	
	public void setCondition(LiveCaosExpression condition)
	{
		this.condition = condition;
	}
	
	public LiveCaosStatement getNextIfTrue()
	{
		return nextIfTrue;
	}
	
	public void setNextIfTrue(LiveCaosStatement nextIfTrue)
	{
		this.nextIfTrue = nextIfTrue;
	}
	
	public LiveCaosStatement getNextIfFalse()
	{
		return nextIfFalse;
	}
	
	public void setNextIfFalse(LiveCaosStatement nextIfFalse)
	{
		this.nextIfFalse = nextIfFalse;
	}
}
