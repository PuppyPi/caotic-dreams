package rebound.apps.caoticdreams.caos.library.core;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import rebound.annotations.semantic.temporal.ConstantReturnValue;
import rebound.apps.caoticdreams.caos.library.content.CDApiUtilities;

public interface iCoreAgentContent
{
	/**
	 * OVxx / MVxx
	 * 
	 * See {@link CDApiUtilities#getIndexedVariable(List, int)} / {@link CDApiUtilities#setIndexedVariable(List, int, Object)} for how to how to access it in a caotic way (namely imagining infinite variables with value integer-0 instead of absent or null-valued, and extending the list as needed (with integer-0's!))
	 */
	@ConstantReturnValue
	public @Nonnull List<Object> getObjectVariables();
	
	/**
	 * See {@link CDApiUtilities#getNamedVariable(Map, Object)} / {@link CDApiUtilities#setNamedVariable(Map, Object, Object)} for how to how to access it in a caotic way (namely imagining infinite variables with value integer-0 instead of absent or null-valued)
	 */
	@ConstantReturnValue
	public @Nonnull Map<Object, Object> getNameVariables();
	
	
	public long getUNID();
	public void setUNID(long uuid);
}
