package rebound.apps.caoticdreams.caos.evaluation;

public class CaosRuntimeException
extends RuntimeException
{
	private static final long serialVersionUID = 1l;
	
	
	public CaosRuntimeException()
	{
		super();
	}
	
	public CaosRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public CaosRuntimeException(String message)
	{
		super(message);
	}
	
	public CaosRuntimeException(Throwable cause)
	{
		super(cause);
	}
}
