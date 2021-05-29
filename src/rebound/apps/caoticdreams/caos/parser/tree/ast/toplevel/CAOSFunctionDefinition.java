package rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel;

import java.util.Map;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSCodeBlock;

// https://github.com/PuppyPi/caotic-dreams/issues/10

public class CAOSFunctionDefinition
extends CAOSToplevelScript
{
	protected final String name;
	protected final int arity;
	protected final boolean global; // local if false
	protected final boolean hasReturnValue;
	
	public CAOSFunctionDefinition(CAOSCodeBlock mainCode, Map<String, CAOSCodeBlock> subroutines, String name, int arity, boolean global, boolean hasReturnValue)
	{
		super(mainCode, subroutines);
		this.name = name;
		this.arity = arity;
		this.global = global;
		this.hasReturnValue = hasReturnValue;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getArity()
	{
		return arity;
	}
	
	public boolean isGlobal()
	{
		return global;
	}
	
	public boolean isHasReturnValue()
	{
		return hasReturnValue;
	}
}
