package rebound.apps.caoticdreams.caos.library.core.variables;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;

public class GAME
{
	public static Object get(CaosExecutionContext context, String variableName)
	{
		return context.getWorld().getGameVariables().get(variableName);
	}
	
	public static void set(CaosExecutionContext context, String variableName, Object value)
	{
		context.getWorld().getGameVariables().put(variableName, value);
	}
}
