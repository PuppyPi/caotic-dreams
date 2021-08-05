package rebound.apps.caoticdreams.caos.library.content;

import java.util.Set;
import javax.annotation.Nullable;
import rebound.exceptions.NotFoundException;

public interface iWorldLibrary
{
	public Set<String> getWUIDs();
	
	
	/**
	 * Arbitrary JSON-compatible metadata that can be loaded without loading the whole world!
	 * Eg, for the world switcher to use :3
	 */
	public @Nullable Object getWorldMetadata(String wuid) throws NotFoundException;
	
	public void setWorldMetadata(String wuid, @Nullable Object gds) throws NotFoundException;
}
