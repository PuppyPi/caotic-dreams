package rebound.apps.caoticdreams.caos.library.content;

public interface iEngine
{
	/**
	 * GNAM
	 */
	public String getGameName();
	
	/**
	 * It's CaoticDreams!  We do everything in CAOS!  ;D
	 */
	public void setGameName(String newValue);
	
	
	public Object getEngineVariable(Object name);
	public void setEngineVariable(Object name, Object value);
}
