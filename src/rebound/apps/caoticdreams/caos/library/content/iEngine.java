package rebound.apps.caoticdreams.caos.library.content;

import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.temporal.ConstantReturnValue;
import rebound.annotations.semantic.temporal.PossiblyChangingReturnValue;

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
	
	
	/**
	 * See {@link CDApiUtilities#getNamedVariable(Map, Object)} / {@link CDApiUtilities#setNamedVariable(Map, Object, Object)} for how to how to access it in a caotic way (namely imagining infinite variables with value integer-0 instead of absent or null-valued)
	 */
	@ConstantReturnValue
	public @Nonnull Map<Object, Object> getEngineVariables();
	
	
	public void quit();
	
	
	@ConstantReturnValue
	public @Nonnull iWorldLibrary getWorldLibrary();
	
	
	/**
	 * Get the available worlds with the {@link #getWorldLibrary() world library}
	 * Get the new world after loading with {@link #getCurrentWorld()}.
	 * @throws Throwable if anything is thrown, effectively nothing is done on disk and it fails cleanly :3
	 */
	public void loadWorld(String uid) throws Throwable;
	
	/**
	 * If null, use {@link #loadWorld(String)} to load the world switcher or similar :3
	 */
	@PossiblyChangingReturnValue
	public @Nullable iWorld getCurrentWorld();
}
