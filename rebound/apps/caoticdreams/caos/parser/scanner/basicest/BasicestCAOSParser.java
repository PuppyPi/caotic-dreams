package rebound.apps.caoticdreams.caos.parser.scanner.basicest;

import static rebound.apps.caoticdreams.caos.parser.scanner.basicest.BasicestCAOSParser.LeState.*;
import java.util.ArrayList;
import java.util.List;
import rebound.annotations.semantic.allowedoperations.ReadonlyValue;
import rebound.exceptions.UnexpectedHardcodedEnumValueException;
import rebound.text.StringUtilities;
import rebound.text.lexing.apis.tokenstream.flat.FlatlyTypedWherefulToken;
import rebound.text.lexing.apis.tokenstream.flat.util.FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation;
import rebound.text.lexing.apis.tokenstream.util.TokenStreamTestingUtilities;
import rebound.text.lexing.apis.tokenstream.util.TokenStreamUtilities;
import rebound.text.lexing.code.clike.superbasicclikescanner.BasicClikeTokenType;

public class BasicestCAOSParser
{
	protected static enum LeState
	{
		CODE,
		
		
		CHARLITERAL,
		CHARLITERAL_ESCAPECHAR,
		
		STRINGLITERAL,
		STRINGLITERAL_ESCAPECHAR,
		
		
		CODE_SINGLESLASH,
		SINGLELINECOMMENT,
		
		BLOCKCOMMENT,
		BLOCKCOMMENT_ASTERISK,
	}
	
	
	
	
	/**
	 * <code>PutterBackerTogetherer</code> is deprecated (as of ten seconds after creation XD), use {@link StringUtilities#concatList(Iterable)} or similar ^_^
	 */
	@ReadonlyValue
	public static List<FlatlyTypedWherefulToken<BasicClikeTokenType>> parse(String source)
	{
		int sourceLength = source.length();
		
		List<FlatlyTypedWherefulToken<BasicClikeTokenType>> tokens = new ArrayList<FlatlyTypedWherefulToken<BasicClikeTokenType>>();
		
		int start = 0;
		
		LeState state = CODE;
		
		char c = 0;
		for (int i = 0; i < sourceLength; i++)
		{
			c = source.charAt(i);
			
			if (state == CODE)
			{
				if (c == '"')
				{
					if (i - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, i - start, source, BasicClikeTokenType.Code));
					start = i;
					
					state = STRINGLITERAL;
				}
				else if (c == '\'')
				{
					if (i - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, i - start, source, BasicClikeTokenType.Code));
					start = i;
					
					state = CHARLITERAL;
				}
				else if (c == '/')
				{
					state = CODE_SINGLESLASH;
				}
			}
			else if (state == CODE_SINGLESLASH)
			{
				if (c == '/')
				{
					if ((i-1) - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, (i-1) - start, source, BasicClikeTokenType.Code));
					start = i-1;
					
					state = SINGLELINECOMMENT;
				}
				else if (c == '*')
				{
					if ((i-1) - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, (i-1) - start, source, BasicClikeTokenType.Code));
					start = i-1;
					
					state = BLOCKCOMMENT;
				}
				else
				{
					state = CODE;
				}
			}
			
			else if (state == STRINGLITERAL)
			{
				if (c == '\\')
				{
					state = STRINGLITERAL_ESCAPECHAR;
				}
				else if (c == '"')
				{
					if ((i+1) - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, (i+1) - start, source, BasicClikeTokenType.StringLiteral));
					start = i+1;
					
					state = CODE;
				}
			}
			else if (state == STRINGLITERAL_ESCAPECHAR)
			{
				state = STRINGLITERAL;
			}
			
			else if (state == CHARLITERAL)
			{
				if (c == '\\')
				{
					state = CHARLITERAL_ESCAPECHAR;
				}
				else if (c == '\'')
				{
					if ((i+1) - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, (i+1) - start, source, BasicClikeTokenType.CharLiteral));
					start = i+1;
					
					state = CODE;
				}
			}
			else if (state == CHARLITERAL_ESCAPECHAR)
			{
				state = CHARLITERAL;
			}
			
			else if (state == SINGLELINECOMMENT)
			{
				if (c == '\n')
				{
					if ((i+1) - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, (i+1) - start, source, BasicClikeTokenType.CommentSingleLine));
					start = i+1;
					
					state = CODE;
				}
			}
			
			else if (state == BLOCKCOMMENT)
			{
				if (c == '*')
				{
					state = BLOCKCOMMENT_ASTERISK;
				}
			}
			else if (state == BLOCKCOMMENT_ASTERISK)
			{
				if (c == '/')
				{
					if ((i+1) - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, (i+1) - start, source, BasicClikeTokenType.CommentMultiLine));
					start = i+1;
					
					state = CODE;
				}
				else if (c != '*')
				{
					state = BLOCKCOMMENT;
				}
			}
			
			else
			{
				throw new UnexpectedHardcodedEnumValueException(state);
			}
		}
		
		
		//EOF tokenthing! :D
		{
			if (state == CODE || state == CODE_SINGLESLASH)
			{
				if (sourceLength - start != 0)
					tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, sourceLength - start, source, BasicClikeTokenType.Code));
			}
			else if (state == STRINGLITERAL || state == STRINGLITERAL_ESCAPECHAR)
			{
				if (sourceLength - start != 0)
					tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, sourceLength - start, source, BasicClikeTokenType.StringLiteral));
			}
			else if (state == SINGLELINECOMMENT)
			{
				if (sourceLength - start != 0)
					tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, sourceLength - start, source, BasicClikeTokenType.CommentSingleLine));
			}
			else if (state == BLOCKCOMMENT || state == BLOCKCOMMENT_ASTERISK)
			{
				if (sourceLength - start != 0)
					tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicClikeTokenType>(start, sourceLength - start, source, BasicClikeTokenType.CommentMultiLine));
			}
			
			else
			{
				throw new UnexpectedHardcodedEnumValueException(state);
			}
		}
		
		
		
		
		//Validate!
		assert TokenStreamTestingUtilities.validateContiguousAndSourceIsEqual(tokens, source);
		
		return tokens;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static String filterAwayNoncode(List<FlatlyTypedWherefulToken<BasicClikeTokenType>> parsed)
	{
		return TokenStreamUtilities.reconstituteFiltering(t -> t.getTokenType() == BasicClikeTokenType.Code, parsed);
	}
	
	public static String parseAndFilterAwayNoncode(String unparsed)
	{
		return filterAwayNoncode(parse(unparsed));
	}
	
	
	
	
	
	public static String filterAwayComments(List<FlatlyTypedWherefulToken<BasicClikeTokenType>> parsed)
	{
		return TokenStreamUtilities.reconstituteFiltering(t -> !t.getTokenType().isComment(), parsed);
	}
	
	public static String parseAndFilterAwayComments(String unparsed)
	{
		return filterAwayComments(parse(unparsed));
	}
}
