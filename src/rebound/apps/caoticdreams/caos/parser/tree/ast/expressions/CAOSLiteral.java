package rebound.apps.caoticdreams.caos.parser.tree.ast.expressions;

import rebound.util.collections.prim.PrimitiveCollections.ImmutableByteArrayList;

public class CAOSLiteral
extends CAOSExpression
{
	/**
	 * {@link String} for Strings
	 * {@link ImmutableByteArrayList} for Byte Strings
	 * {@link Long} for Integers
	 * {@link Double} for Floats
	 * {@link Boolean} for Conditions
	 * 
	 * (NULL is considered a nullary function not a literal)
	 */
	protected final Object literalValue;
	
	
	
	public CAOSLiteral(Object literalValue)
	{
		this.literalValue = literalValue;
	}
	
	public Object getLiteralValue()
	{
		return literalValue;
	}
}
