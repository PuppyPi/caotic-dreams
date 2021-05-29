package rebound.apps.caoticdreams.caos.parser.tree.ast.expressions;

import java.util.List;
import javax.annotation.Nonnull;
import rebound.annotations.semantic.simpledata.Emptyable;

public class CAOSSpecialFunctionInvocation
extends CAOSExpression
{
	protected final @Nonnull CAOSSpecialFunction function;
	protected final @Nonnull @Emptyable List<CAOSExpression> parameters;
	
	
	public static enum CAOSSpecialFunction
	{
		EQ,
		NE,
		LT,
		LE,
		GT,
		GE,
		
		AND,
		OR,
		//NOT is considered a normal function X3
		
		ANDShortCircuiting,  //DreamCAOS :3
		ORShortCircuiting,  //DreamCAOS :3
		
		
		CFUNorDFUN,  //first parameter is always a CAOSLiteral string for CFUN X3
		PFUNorAFUN,  //first parameter is always a CAOSLiteral string for PFUN X3
	}
	
	
	
	
	public CAOSSpecialFunctionInvocation(CAOSSpecialFunction function, List<CAOSExpression> parameters)
	{
		this.function = function;
		this.parameters = parameters;
	}
	
	public CAOSSpecialFunction getFunction()
	{
		return function;
	}
	
	public List<CAOSExpression> getParameters()
	{
		return parameters;
	}
}
