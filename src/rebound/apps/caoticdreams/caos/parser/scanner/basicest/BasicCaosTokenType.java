package rebound.apps.caoticdreams.caos.parser.scanner.basicest;

public enum BasicCaosTokenType
{
	CommentSingleLine,
	CommentMultiLine,
	
	Code,
	
	StringLiteral,
	ByteStringLiteral,
	CharLiteral,
	;
	
	
	
	public boolean isComment()
	{
		return this == CommentSingleLine || this == CommentMultiLine;
	}
	
	public boolean isLiteral()
	{
		return this == StringLiteral || this == ByteStringLiteral || this == CharLiteral;
	}
}
