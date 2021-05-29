package rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel;

import java.util.Map;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSCodeBlock;

public class CAOSEventScript
extends CAOSToplevelScript
{
	protected final long family, genus, species, eventNumber;
	
	public CAOSEventScript(CAOSCodeBlock mainCode, Map<String, CAOSCodeBlock> subroutines, long family, long genus, long species, long eventNumber)
	{
		super(mainCode, subroutines);
		this.family = family;
		this.genus = genus;
		this.species = species;
		this.eventNumber = eventNumber;
	}
	
	public long getFamily()
	{
		return family;
	}
	
	public long getGenus()
	{
		return genus;
	}
	
	public long getSpecies()
	{
		return species;
	}
	
	public long getEventNumber()
	{
		return eventNumber;
	}
}
