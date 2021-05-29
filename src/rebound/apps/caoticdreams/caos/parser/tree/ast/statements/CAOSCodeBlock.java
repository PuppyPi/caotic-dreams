package rebound.apps.caoticdreams.caos.parser.tree.ast.statements;

import java.util.List;
import rebound.apps.caoticdreams.caos.parser.tree.ast.CAOSSyntacticStructureRememberingLocationInformation;

public class CAOSCodeBlock
extends CAOSSyntacticStructureRememberingLocationInformation
{
	protected final List<CAOSStatement> statements;
	
	public CAOSCodeBlock(List<CAOSStatement> statements)
	{
		this.statements = statements;
	}
	
	public List<CAOSStatement> getStatements()
	{
		return statements;
	}
}
