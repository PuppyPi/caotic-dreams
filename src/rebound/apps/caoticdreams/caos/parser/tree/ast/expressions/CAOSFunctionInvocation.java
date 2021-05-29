package rebound.apps.caoticdreams.caos.parser.tree.ast.expressions;

import java.util.List;
import javax.annotation.Nonnull;
import rebound.annotations.semantic.simpledata.Emptyable;

public class CAOSFunctionInvocation
extends CAOSExpression
{
	protected final @Nonnull String functionName;
	protected final @Nonnull @Emptyable List<CAOSExpression> parameters;
	
	
	
	public CAOSFunctionInvocation(String functionName, List<CAOSExpression> parameters)
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
