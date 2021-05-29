package rebound.apps.caoticdreams.caos.parser.tree.ast.statements;

import java.util.List;
import javax.annotation.Nonnull;
import rebound.annotations.semantic.simpledata.Emptyable;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSExpression;

public class CAOSCommandInvocation
extends CAOSStatement
{
	protected final @Nonnull String functionName;
	protected final @Nonnull @Emptyable List<CAOSExpression> parameters;
	
	
	public CAOSCommandInvocation(String functionName, List<CAOSExpression> parameters)
	{
		this.functionName = functionName;
		this.parameters = parameters;
	}
	
	public String getFunctionName()
	{
		return functionName;
	}
	
	public List<CAOSExpression> getParameters()
	{
		return parameters;
	}
}
