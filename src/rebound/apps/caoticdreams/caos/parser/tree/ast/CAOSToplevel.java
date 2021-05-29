package rebound.apps.caoticdreams.caos.parser.tree.ast;

import java.util.List;
import rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel.CAOSToplevelScript;

/**
 * eg, a .cos file or something passed to the dynamic "CAOS" function
 */
public class CAOSToplevel
{
	protected final List<CAOSToplevelScript> scripts;
	
	public CAOSToplevel(List<CAOSToplevelScript> scripts)
	{
		this.scripts = scripts;
	}
	
	public List<CAOSToplevelScript> getScripts()
	{
		return scripts;
	}
}
