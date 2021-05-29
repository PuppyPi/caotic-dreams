package rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel;

import java.util.Map;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSCodeBlock;

public abstract class CAOSToplevelScript
{
	protected final CAOSCodeBlock mainCode;
	protected final Map<String, CAOSCodeBlock> subroutines;
	
	public CAOSToplevelScript(CAOSCodeBlock mainCode, Map<String, CAOSCodeBlock> subroutines)
	{
		this.mainCode = mainCode;
		this.subroutines = subroutines;
	}
	
	public CAOSCodeBlock getMainCode()
	{
		return mainCode;
	}
	
	public Map<String, CAOSCodeBlock> getSubroutines()
	{
		return subroutines;
	}
}
