package rebound.apps.caoticdreams.caos.library.core.functions;

import rebound.apps.caoticdreams.caos.evaluation.CaosExecutionContext;
import rebound.apps.caoticdreams.caos.library.NoImplicitSLOW;
import rebound.apps.caoticdreams.caos.library.StandardCAOS;

@NoImplicitSLOW
@StandardCAOS
public class FTOI
{
	public static long f(CaosExecutionContext context, double value)
	{
		return (long)value;  //TODO should this round it?
	}
}
