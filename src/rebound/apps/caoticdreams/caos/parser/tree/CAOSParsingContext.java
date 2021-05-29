package rebound.apps.caoticdreams.caos.parser.tree;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSCommandInvocation;

public interface CAOSParsingContext
{
	/**
	 * @return null if unrecognized
	 */
	public @Nullable Integer getArityForCommand(String commandName);
	
	public @Nullable Integer getArityForFunction(String functionName);
	
	
	
	/**
	 * Returns null if it's not a loop opener, otherwise returns a test for whether something is a loop closer!
	 */
	public Predicate<String> isLoopOpener(CAOSCommandInvocation loopOpenerCandidate);
}
