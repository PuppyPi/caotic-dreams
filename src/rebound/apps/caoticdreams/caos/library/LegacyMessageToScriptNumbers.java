package rebound.apps.caoticdreams.caos.library;

public class LegacyMessageToScriptNumbers
{
	public static int getScriptNumberForMessageNumber(int mesgNumber)
	{
		// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/51
		
		if (mesgNumber >= 0 && mesgNumber <= 2)
			return (mesgNumber + 1) % 3;   // I did a fanceh ;D
		else
			return mesgNumber;
	}
}
