package rebound.apps.caoticdreams.caos.parser.tree.ast;

import javax.annotation.Nullable;

public abstract class CAOSSyntacticStructureRememberingLocationInformation
{
	protected @Nullable Integer startingPointInSource;
	protected @Nullable Integer lengthInSource;
	
	public Integer getStartingPointInSource()
	{
		return startingPointInSource;
	}
	
	public void setStartingPointInSource(Integer startingPointInSource)
	{
		this.startingPointInSource = startingPointInSource;
	}
	
	public Integer getLengthInSource()
	{
		return lengthInSource;
	}
	
	public void setLengthInSource(Integer lengthInSource)
	{
		this.lengthInSource = lengthInSource;
	}
}
