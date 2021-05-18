package rebound.apps.caoticdreams.caos.evaluation.interpreter;

public abstract class CaosStatement
{
	protected final int positionInSourceCode;
	protected final int lengthInSourceCode;
	
	public CaosStatement(int positionInSourceCode, int lengthInSourceCode)
	{
		this.positionInSourceCode = positionInSourceCode;
		this.lengthInSourceCode = lengthInSourceCode;
	}
	
	
	
	/**
	 * For CODP :3
	 */
	public int getPositionInSourceCode()
	{
		return positionInSourceCode;
	}
	
	/**
	 * Why not? :}
	 */
	public int getLengthInSourceCode()
	{
		return lengthInSourceCode;
	}
}
