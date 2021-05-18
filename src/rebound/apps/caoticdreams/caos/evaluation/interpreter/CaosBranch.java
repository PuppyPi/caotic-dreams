package rebound.apps.caoticdreams.caos.evaluation.interpreter;

public class CaosBranch
extends CaosStatement
{
	protected CaosExpression condition;
	protected CaosStatement nextIfTrue;
	protected CaosStatement nextIfFalse;
	
	public CaosBranch(int positionInSourceCode, int lengthInSourceCode)
	{
		super(positionInSourceCode, lengthInSourceCode);
	}
	
	public CaosExpression getCondition()
	{
		return condition;
	}
	
	public void setCondition(CaosExpression condition)
	{
		this.condition = condition;
	}
	
	public CaosStatement getNextIfTrue()
	{
		return nextIfTrue;
	}
	
	public void setNextIfTrue(CaosStatement nextIfTrue)
	{
		this.nextIfTrue = nextIfTrue;
	}
	
	public CaosStatement getNextIfFalse()
	{
		return nextIfFalse;
	}
	
	public void setNextIfFalse(CaosStatement nextIfFalse)
	{
		this.nextIfFalse = nextIfFalse;
	}
}
