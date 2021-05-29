package rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel;

import java.util.Map;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSCodeBlock;

public class CAOSToplevelScriptParameterless
extends CAOSToplevelScript
{
	protected final CAOSParameterlessScriptType scriptType;
	
	public static enum CAOSParameterlessScriptType
	{
		Install,
		Remove,
		//No others, right?  We could just add new ones in with the Local/Global Function system! X3
	}
	
	
	
	
	public CAOSToplevelScriptParameterless(CAOSCodeBlock mainCode, Map<String, CAOSCodeBlock> subroutines, CAOSParameterlessScriptType scriptType)
	{
		super(mainCode, subroutines);
		this.scriptType = scriptType;
	}
	
	public CAOSParameterlessScriptType getScriptType()
	{
		return scriptType;
	}
}
