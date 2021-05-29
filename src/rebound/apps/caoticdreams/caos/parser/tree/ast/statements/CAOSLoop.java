package rebound.apps.caoticdreams.caos.parser.tree.ast.statements;

public class CAOSLoop
extends CAOSStatement
{
	/**
	 * Eg, LOOP, REPS, ENUM, etc.
	 */
	protected final CAOSCommandInvocation start;
	
	/**
	 * Eg, EVER/UNTL, REPE, NEXT, etc.
	 */
	protected final CAOSCommandInvocation end;
	
	protected final CAOSCodeBlock loopBody;
	
	
	
	public CAOSLoop(CAOSCommandInvocation start, CAOSCommandInvocation end, CAOSCodeBlock loopBody)
	{
		this.start = start;
		this.end = end;
		this.loopBody = loopBody;
	}
	
	public CAOSCommandInvocation getStart()
	{
		return start;
	}
	
	public CAOSCommandInvocation getEnd()
	{
		return end;
	}
	
	public CAOSCodeBlock getLoopBody()
	{
		return loopBody;
	}
}
