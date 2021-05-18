package rebound.apps.caoticdreams.caos.library.content;

public interface iEngine
{
	/**
	 * GNAM
	 */
	public String getGameName();
	
	public Object getEngineVariable(String name);
	public void setEngineVariable(String name, Object value);
}
