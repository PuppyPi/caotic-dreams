package rebound.apps.caoticdreams.caos.library.core;

public enum EventScriptTerminationType
{
	/**
	 * It just actually finished properly :>
	 */
	Success,
	
	
	/**
	 * It threw an error or BANG or etc. was called XD
	 */
	Error,
	
	
	/**
	 * STOP / STPT
	 */
	StopRequested,
	
	
	/**
	 * MESG WRIT / MESG WRT+
	 */
	Overwritten,
}
