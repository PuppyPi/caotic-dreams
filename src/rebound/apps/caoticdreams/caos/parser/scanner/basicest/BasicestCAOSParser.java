package rebound.apps.caoticdreams.caos.parser.scanner.basicest;

import static rebound.apps.caoticdreams.caos.parser.scanner.basicest.BasicestCAOSParser.LeState.*;
import java.util.ArrayList;
import java.util.List;
import rebound.annotations.semantic.allowedoperations.ReadonlyValue;
import rebound.exceptions.UnexpectedHardcodedEnumValueException;
import rebound.io.util.UniversalNewlineReader;
import rebound.text.StringUtilities;
import rebound.text.lexing.apis.tokenstream.flat.FlatlyTypedWherefulToken;
import rebound.text.lexing.apis.tokenstream.flat.util.FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation;
import rebound.text.lexing.apis.tokenstream.util.TokenStreamTestingUtilities;
import rebound.text.lexing.apis.tokenstream.util.TokenStreamUtilities;

//TODO Test this with rebound.jagent.lib.caos2pray.scanner.CosParser!  :D

public class BasicestCAOSParser
{
	protected static enum LeState
	{
		CODE,
		
		
		SINGLELINECOMMENT,
		
		BLOCKCOMMENT,
		BLOCKCOMMENT_ASTERISK,
		
		
		CHARLITERAL,
		CHARLITERAL_ESCAPECHAR,
		
		STRINGLITERAL,
		STRINGLITERAL_ESCAPECHAR,
		
		BYTESTRINGLITERAL,
		BYTESTRINGLITERAL_CHARLITERAL,
		BYTESTRINGLITERAL_CHARLITERAL_ESCAPECHAR,
	}
	
	
	
	
	/**
	 * Note: this only parses Universal Newlines text (a la Python; ie, Mac OS Classic (CR), Windows/ASCII (CR LF), and UNIX Newlines (LF) all become UNIX Newlines (LF))
	 * You can use {@link UniversalNewlineReader} and {@link StringUtilities#universalNewlines(String)} to do the conversion ahead of time :3
	 * (But if you don't, single-line comments might not parse correctly!)
	 * 
	 * And don't worry, explicit escapes of \r\n in the code won't be affected by passing it through {@link StringUtilities#universalNewlines(String)}.
	 */
	@ReadonlyValue
	public static List<FlatlyTypedWherefulToken<BasicCaosTokenType>> parse(String source)
	{
		int sourceLength = source.length();
		
		List<FlatlyTypedWherefulToken<BasicCaosTokenType>> tokens = new ArrayList<FlatlyTypedWherefulToken<BasicCaosTokenType>>();
		
		int start = 0;
		
		LeState state = CODE;
		
		int blockCommentDepth = 0;  //0 = in a block comment, the outermost one  (this variable isn't used unless state = BLOCKCOMMENT)
		
		char c = 0;
		for (int i = 0; i < sourceLength; i++)
		{
			c = source.charAt(i);
			
			
			if (state == CODE)
			{
				boolean doubleQuote = c == '"';
				boolean singleQuote = c == '\'';
				boolean openBracket = c == '[';
				boolean asterisk = c == '*';
				
				if (doubleQuote || singleQuote || openBracket || asterisk)
				{
					//Complete/commit the current region :3
					if (i - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicCaosTokenType>(start, i - start, source, BasicCaosTokenType.Code));
					
					//And start a new one :3
					start = i;
					
					if (doubleQuote)
						state = STRINGLITERAL;
					else if (singleQuote)
						state = CHARLITERAL;
					else if (openBracket)
						state = BYTESTRINGLITERAL;
					else if (asterisk)
						state = SINGLELINECOMMENT;
					else
						throw new AssertionError();
				}
				else
				{
					//Stay in "code" state :3
				}
			}
			
			
			else if (state == SINGLELINECOMMENT)
			{
				if (c == '<')
				{
					state = BLOCKCOMMENT;
					blockCommentDepth = 0;
				}
				else if (c == '\n')  //See https://github.com/PuppyPi/caotic-dreams/issues/7
				{
					//Complete/commit the current region :3
					if (i - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicCaosTokenType>(start, i - start, source, BasicCaosTokenType.CommentSingleLine));
					
					//And start a new one :3
					start = i;
					state = CODE;
				}
				else
				{
					//Stay in single-line comment state :3
				}
			}
			
			else if (state == BLOCKCOMMENT)
			{
				if (c == '*')
				{
					state = BLOCKCOMMENT_ASTERISK;
				}
				else
				{
					//Stay in state
				}
			}
			else if (state == BLOCKCOMMENT_ASTERISK)
			{
				if (c == '>')
				{
					//Closer!
					
					if (blockCommentDepth == 0)
					{
						//The final closing!
						if ((i+1) - start != 0)
							tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicCaosTokenType>(start, (i+1) - start, source, BasicCaosTokenType.CommentMultiLine));
						start = i+1;
						
						state = CODE;
					}
					else
					{
						blockCommentDepth--;
					}
				}
				else if (c == '<')
				{
					blockCommentDepth++;
				}
				else
				{
					state = BLOCKCOMMENT;
				}
			}
			
			
			
			
			
			
			
			else if (state == BYTESTRINGLITERAL)
			{
				if (c == '\'')
				{
					/*
					 * This is allowed according to https://creatures.wiki/CAOS_language_spec#Basic_token_types
					 * 		"You can use any integer as the values (so binary/characters are valid)"
					 */
					state = BYTESTRINGLITERAL_CHARLITERAL;
				}
				else if (c == ']')
				{
					if ((i+1) - start != 0)
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicCaosTokenType>(start, (i+1) - start, source, BasicCaosTokenType.ByteStringLiteral));
					start = i+1;
					
					state = CODE;
				}
				else
				{
					//Stay in state
				}
			}
			else if (state == BYTESTRINGLITERAL_CHARLITERAL)
			{
				if (c == '\\')
				{
					state = BYTESTRINGLITERAL_CHARLITERAL_ESCAPECHAR;
				}
				else if (c == '\'')
				{
					state = BYTESTRINGLITERAL;
				}
			}
			else if (state == BYTESTRINGLITERAL_CHARLITERAL_ESCAPECHAR)
			{
				state = BYTESTRINGLITERAL_CHARLITERAL;
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
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicCaosTokenType>(start, (i+1) - start, source, BasicCaosTokenType.StringLiteral));
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
						tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicCaosTokenType>(start, (i+1) - start, source, BasicCaosTokenType.CharLiteral));
					start = i+1;
					
					state = CODE;
				}
			}
			else if (state == CHARLITERAL_ESCAPECHAR)
			{
				state = CHARLITERAL;
			}
			
			
			
			else
			{
				throw new UnexpectedHardcodedEnumValueException(state);
			}
		}
		
		
		
		
		
		//EOF tokenthing! :D
		{
			final BasicCaosTokenType type;
			
			if (state == CODE)
				type = BasicCaosTokenType.Code;
			
			else if (state == STRINGLITERAL || state == STRINGLITERAL_ESCAPECHAR)
				type = BasicCaosTokenType.StringLiteral;
			
			else if (state == BYTESTRINGLITERAL || state == BYTESTRINGLITERAL_CHARLITERAL || state == BYTESTRINGLITERAL_CHARLITERAL_ESCAPECHAR)
				type = BasicCaosTokenType.ByteStringLiteral;
			
			else if (state == CHARLITERAL || state == CHARLITERAL_ESCAPECHAR)
				type = BasicCaosTokenType.CharLiteral;
			
			else if (state == SINGLELINECOMMENT)
				type = BasicCaosTokenType.CommentSingleLine;
			
			else if (state == BLOCKCOMMENT || state == BLOCKCOMMENT_ASTERISK)
				type = BasicCaosTokenType.CommentMultiLine;
			
			else
				throw new UnexpectedHardcodedEnumValueException(state);
			
			
			if (sourceLength - start != 0)
				tokens.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<BasicCaosTokenType>(start, sourceLength - start, source, type));
		}
		
		
		
		
		//Validate!
		assert TokenStreamTestingUtilities.validateContiguousAndSourceIsEqual(tokens, source);
		
		return tokens;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static String filterAwayNoncode(List<FlatlyTypedWherefulToken<BasicCaosTokenType>> parsed)
	{
		return TokenStreamUtilities.reconstituteFiltering(t -> t.getTokenType() == BasicCaosTokenType.Code, parsed);
	}
	
	public static String parseAndFilterAwayNoncode(String unparsed)
	{
		return filterAwayNoncode(parse(unparsed));
	}
	
	
	
	
	
	public static String filterAwayComments(List<FlatlyTypedWherefulToken<BasicCaosTokenType>> parsed)
	{
		return TokenStreamUtilities.reconstituteFiltering(t -> !t.getTokenType().isComment(), parsed);
	}
	
	public static String parseAndFilterAwayComments(String unparsed)
	{
		return filterAwayComments(parse(unparsed));
	}
}
