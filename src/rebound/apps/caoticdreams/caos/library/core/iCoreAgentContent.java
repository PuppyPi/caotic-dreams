package rebound.apps.caoticdreams.caos.library.core;

public interface iCoreAgentContent
{
	public Object getOVxx(int index);
	public void setOVxx(int index, Object value);
	
	public Object getNameVariable(Object key);
	public void setNameVariable(Object key, Object value);
	
	public long getUNID();
	public void setUNID(long uuid);
}
