package rebound.apps.caoticdreams.caos.parser.tree.ast.statements;

import java.util.List;
import javax.annotation.Nonnull;
import rebound.annotations.semantic.simpledata.Emptyable;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSExpression;

public class CAOSSpecialCommandInvocation
extends CAOSStatement
{
	protected final @Nonnull CAOSSpecialCommand command;
	protected final @Nonnull @Emptyable List<CAOSExpression> parameters;
	
	
	public static enum CAOSSpecialCommand
	{
		GSUB,
		
		CCMDorDCMD,  //first parameter is always a CAOSLiteral string for CCMD X3
		PCMDorACMD,  //first parameter is always a CAOSLiteral string for PCMD X3
		
		InstIfCompiledStart,  //no parameters
		InstIfCompiledEnd,  //no parameters
		
		RequireCompilationStart,  //no parameters
		RequireCompilationEnd,  //no parameters
	}
	
	
	public CAOSSpecialCommandInvocation(CAOSSpecialCommand command, List<CAOSExpression> parameters)
	{
		this.command = command;
		this.parameters = parameters;
	}
	
	public CAOSSpecialCommand getCommand()
	{
		return command;
	}
	
	public List<CAOSExpression> getParameters()
	{
		return parameters;
	}
}
