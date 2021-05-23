package rebound.apps.caoticdreams.caos.evaluation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.library.content.iEngine;
import rebound.apps.caoticdreams.caos.library.content.iWorld;
import rebound.apps.caoticdreams.caos.library.core.CaosInputStream;
import rebound.apps.caoticdreams.caos.library.core.CaosOutputStream;
import rebound.apps.caoticdreams.caos.library.core.iAgentVM;

/**
 * This is meant for Interpreting CAOS.
 * Compiled CAOS is to load things from this (eg, vaXX) if there's interpreted code before it, and store things back to this (eg, vaXX) if there's interpreted code after it :3
 */
public interface CaosExecutionContext
{
	/**
	 * @param indexZeroBased zero-based so 0 = _P1_, 1 = _P2_, etc.
	 * @return ({@link Long})0 not null for variables that aren't there.
	 */
	public Object get_Px_(@Nonnegative int indexZeroBased);
	
	/**
	 * It's readwrite!
	 * https://github.com/Creatures-Developer-Network/c2e-quirks/issues/26
	 */
	public void set_Px_(@Nonnegative int indexZeroBased, Object value);
	
	/**
	 * Can be an {@link iAgentVM} or null normally, or a {@link String} from NET: WRIT
	 */
	public Object getFROM();
	
	/**
	 * It's readwrite!
	 * https://github.com/Creatures-Developer-Network/c2e-quirks/issues/25
	 */
	public void setFROM(Object value);
	
	
	
	
	/**
	 * null for pray install scripts, pray remove scripts, bootstrap scripts, and possibly CAOS dynamic execution :3
	 */
	public @Nullable iAgentVM getOWNR();
	public @Nonnull iWorld getWorld();
	public @Nonnull iEngine getEngine();
	
	public boolean isINST();
	public void setINST(boolean value);
	
	
	
	public iAgentVM getTARG();
	public void setTARG(iAgentVM value);
	
	public Object getVAxx(int index);
	public void setVAxx(int index, Object value);
	
	
	
	public CaosInputStream getCurrentInputStream();
	public void setCurrentInputStream(CaosInputStream value);
	
	public CaosOutputStream getCurrentOutputStream();
	public void setCurrentOutputStream(CaosOutputStream value);
}
