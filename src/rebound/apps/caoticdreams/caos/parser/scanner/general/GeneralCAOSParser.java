package rebound.apps.caoticdreams.caos.parser.scanner.general;

import static rebound.bits.Unsigned.*;
import static rebound.testing.WidespreadTestingUtilities.*;
import static rebound.text.StringUtilities.*;
import static rebound.util.collections.CollectionUtilities.*;
import static rebound.util.objectutil.BasicObjectUtilities.*;
import java.util.ArrayList;
import java.util.List;
import rebound.apps.caoticdreams.caos.parser.scanner.basicest.BasicCaosTokenType;
import rebound.apps.caoticdreams.caos.parser.scanner.general.GeneralCAOSTokenMeaning.GeneralCAOSTokenMeaningDataless;
import rebound.apps.caoticdreams.caos.parser.scanner.general.GeneralCAOSTokenMeaning.GeneralCAOSTokenMeaningLiteral;
import rebound.apps.caoticdreams.caos.parser.scanner.general.GeneralCAOSTokenMeaning.GeneralCAOSTokenMeaningLiteral.GeneralCAOSTokenMeaningLiteralType;
import rebound.exceptions.ImpossibleException;
import rebound.exceptions.TextSyntaxException;
import rebound.text.UCS4CodePoint;
import rebound.text.lexing.apis.tokenstream.flat.FlatlyTypedWherefulToken;
import rebound.text.lexing.apis.tokenstream.flat.util.DecoratorFlatlyTypedWherefulToken;
import rebound.text.lexing.apis.tokenstream.flat.util.FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation;
import rebound.util.collections.CollectionUtilities;
import rebound.util.collections.Slice;
import rebound.util.collections.prim.PrimitiveCollections.ByteArrayList;
import rebound.util.collections.prim.PrimitiveCollections.ByteList;
import rebound.util.collections.prim.PrimitiveCollections.ImmutableByteArrayList;

public class GeneralCAOSParser
{
	public static List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> parse(Iterable<FlatlyTypedWherefulToken<BasicCaosTokenType>> basicallyParsed) throws TextSyntaxException
	{
		return CollectionUtilities.mapToListConcatenating(b -> parse1(b), basicallyParsed);
	}
	
	public static List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> parse1(FlatlyTypedWherefulToken<BasicCaosTokenType> basicallyParsed) throws TextSyntaxException
	{
		try
		{
			BasicCaosTokenType bt = basicallyParsed.getTokenType();
			
			if (bt == BasicCaosTokenType.CharLiteral)
				return listof(new DecoratorFlatlyTypedWherefulToken(basicallyParsed, new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralChar, decodeCharLiteral(basicallyParsed.getMaskedSourceSlice()))));
			
			else if (bt == BasicCaosTokenType.StringLiteral)
				return listof(new DecoratorFlatlyTypedWherefulToken(basicallyParsed, new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralString, decodeStringLiteral(basicallyParsed.getMaskedSourceSlice()))));
			
			else if (bt == BasicCaosTokenType.ByteStringLiteral)
				return listof(new DecoratorFlatlyTypedWherefulToken(basicallyParsed, new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralByteString, decodeByteStringLiteral(basicallyParsed.getMaskedSourceSlice()))));
			
			else if (bt == BasicCaosTokenType.CommentSingleLine || bt == BasicCaosTokenType.CommentMultiLine)
				return listof(new DecoratorFlatlyTypedWherefulToken(basicallyParsed, GeneralCAOSTokenMeaningDataless.Comment));
			
			else
				return finishScanningCode(basicallyParsed.getMaskedSourceSlice());
		}
		catch (TextSyntaxException | NumberFormatException exc)
		{
			//Todo give error positions in actual UCS-4 codepoints not UTF-16 like Java somewhat-archaically uses ^^'
			throw TextSyntaxException.inst("Error at region in source starting at UTF-16 codepoint index (zero-based) "+basicallyParsed.getStartingCharacterIndexInSource()+" and is "+basicallyParsed.getLengthOfMaskedSource()+" UTF-16 codepoints long.", exc);
		}
	}
	
	
	
	public static UCS4CodePoint decodeCharLiteral(Slice<CharSequence> source) throws TextSyntaxException
	{
		CharSequence u = source.getUnderlying();
		int l = source.getLength();
		int o = source.getOffset();
		
		if (l < 3)
			throw TextSyntaxException.inst("Character literal too small!");
		
		//4 = Escape backslash or UCS4 with two UTF-16 codepoints in between the apostrophes :3
		//5 = Escape backslash *and* UCS4!
		
		if (l > 5)
			throw TextSyntaxException.inst("Character literal too large!");
		
		if (u.charAt(o+0) != '\'')
			throw TextSyntaxException.inst("Character literal doesn't start with \"'\"!  Wait how did this get through the first parser stage!? XD");
		
		if (u.charAt(o+l-1) != '\'')
			throw TextSyntaxException.inst("Character literal doesn't end with \"'\"!  Wait how did this get through the first parser stage!? XD");
		
		if (l == 3)
			return new UCS4CodePoint((int)u.charAt(o+1));
		else
		{
			if (u.charAt(o+1) == '\\')
			{
				CharSequence d = descape(u.subSequence(o+2, o+l-1));
				
				if (d.length() == 1)
					return new UCS4CodePoint((int)d.charAt(0));
				else if (lengthInCodePoints(d) == 1)
					return new UCS4CodePoint(codePointAt(d, 0));
				else
					throw TextSyntaxException.inst("Character literal too large!");
			}
			else
			{
				if (lengthInCodePoints(u.subSequence(o+1, o+l-1)) == 1)
					return new UCS4CodePoint(codePointAt(u, o+1));
				else
					throw TextSyntaxException.inst("Character literal too large!");
			}
		}
	}
	
	
	public static String decodeStringLiteral(Slice<CharSequence> source) throws TextSyntaxException
	{
		CharSequence u = source.getUnderlying();
		int l = source.getLength();
		int o = source.getOffset();
		
		if (l < 2)
			throw TextSyntaxException.inst("String literal too small!");
		
		if (u.charAt(o+0) != '"')
			throw TextSyntaxException.inst("String literal doesn't start with '\"'!  Wait how did this get through the first parser stage!? XD");
		
		if (u.charAt(o+l-1) != '"')
			throw TextSyntaxException.inst("String literal doesn't end with '\"'!  Wait how did this get through the first parser stage!? XD");
		
		return descape(u.subSequence(o+1, o+l-1)).toString();
	}
	
	
	public static ImmutableByteArrayList decodeByteStringLiteral(Slice<CharSequence> source) throws TextSyntaxException, NumberFormatException
	{
		// https://creatures.wiki/CAOS_language_spec#Basic_token_types
		
		CharSequence u = source.getUnderlying();
		int l = source.getLength();
		int o = source.getOffset();
		
		if (l < 2)
			throw TextSyntaxException.inst("Byte string literal too small!");
		
		if (u.charAt(o+0) != '[')
			throw TextSyntaxException.inst("Byte string literal doesn't start with '['!  Wait how did this get through the first parser stage!? XD");
		
		if (u.charAt(o+l-1) != ']')
			throw TextSyntaxException.inst("Byte string literal doesn't end with ']'!  Wait how did this get through the first parser stage!? XD");
		
		String[] tokens = splitwhitespace(u.subSequence(o+1, o+l-1).toString());
		
		ByteList bytes = new ByteArrayList();
		
		for (String t : tokens)
		{
			if (t.isEmpty())
				throw new ImpossibleException();
			
			long value;
			
			if (t.charAt(0) == '\'')
				value = upcast(decodeCharLiteral(new Slice<>(t, 0, t.length())).getCodeUnit());  //UCS4 is really uint32_t's so upcasting is proper in case it could be >= 2^31 someday somehow (probably not important xD )
			
			else if (forAll(c -> (c >= '0' && c <= '9') || c == '-', t))
				value = Long.parseLong(t);
			
			else if (t.length() >= 2 && t.charAt(0) == '%' && forAll(c -> c == '0' || c == '1', t.substring(1)))
				value = Long.parseLong(t.substring(1), 2);
			
			else if (t.length() >= 3 && t.charAt(0) == '-' && t.charAt(1) == '%' && forAll(c -> c == '0' || c == '1', t.substring(2)))
				value = -Long.parseLong(t.substring(1), 2);
			
			else
				throw TextSyntaxException.inst("Byte string literal contains invalid integer literal: "+repr(t));
			
			if (value < 0)
				throw TextSyntaxException.inst("Byte string literal contains negative integer literal: "+value);
			else if (value > 255)
				throw TextSyntaxException.inst("Byte string literal contains out of bounds (too high) integer literal: "+value+"  (bytes must be 0 to 255 inclusive)");
			else
				bytes.add((byte)value);
		}
		
		return ImmutableByteArrayList.newLIVEOrCopying(bytes);
	}
	
	
	
	
	
	public static List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> finishScanningCode(Slice<CharSequence> source) throws TextSyntaxException, NumberFormatException
	{
		CharSequence u = source.getUnderlying();
		int l = source.getLength();
		int o = source.getOffset();
		
		List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> out = new ArrayList<>();
		
		int start = 0;
		
		final int StateWhitespace = 0;
		final int StateOperatorToken = 1;
		final int StateOtherCodeToken = 2;
		int state = 0;
		
		int e = o + l;
		for (int i = o; i < e; i++)
		{
			boolean first = i == o;
			char c = u.charAt(i);
			
			boolean operatorChar = c == '<' || c == '>' || c == '=';  //These never appear in any CAOS command/function/etc.  :3
			
			if (Character.isWhitespace(c))
			{
				if (first)
				{
					state = StateWhitespace;
					//start = 0;
				}
				else
				{
					if (state == StateWhitespace)
					{
						//Then stay in XD :3
					}
					else
					{
						out.addAll(parseCodeToken(new Slice<>(u, start, i)));
						
						state = operatorChar ? StateOperatorToken : StateOtherCodeToken;
						start = i;
					}
				}
			}
			else
			{
				int thisState = operatorChar ? StateOperatorToken : StateOtherCodeToken;
				
				if (first)
				{
					state = thisState;
					//start = 0;
				}
				else
				{
					if (state == thisState)
					{
						//Then stay in XD :3
					}
					else if (state == StateWhitespace)
					{
						out.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<GeneralCAOSTokenMeaning>(start, i - start, u, GeneralCAOSTokenMeaningDataless.Whitespace));
						
						state = thisState;
						start = i;
					}
					else
					{
						out.addAll(parseCodeToken(new Slice<>(u, start, i)));
						
						state = thisState;
						start = i;
					}
				}
			}
		}
		
		if (l > 0)
		{
			int i = e;
			
			if (state == StateWhitespace)
			{
				out.add(new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<GeneralCAOSTokenMeaning>(start, i - start, u, GeneralCAOSTokenMeaningDataless.Whitespace));
			}
			else
			{
				out.addAll(parseCodeToken(new Slice<>(u, start, i)));
			}
		}
		
		return out;
	}
	
	protected static List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> parseCodeToken(Slice<CharSequence> source)
	{
		/*
		 * This function exists solely because of this:
		 * 		https://creatures.wiki/CAOS_language_spec
		 * 			However, there is no need for whitespace directly after a string, bytestring or decimal.
		 * 			This may be a bug in CEE. It's present in openc2e too for compatibility reasons.
		 * 
		 * See
		 * 		https://github.com/Creatures-Developer-Network/c2e-quirks/issues/47
		 * 		https://github.com/Creatures-Developer-Network/c2e-quirks/issues/49
		 * 		https://github.com/PuppyPi/caotic-dreams/issues/8
		 * 
		 * X'D
		 * 
		 * String and bytestring is already handled in other places.
		 * This handles the case of decimal (which is complicated because floating points can contain alphabetic chars not just numeric, eg: 1.318e6 )
		 */
		
		asrt(source.getLength() > 0);
		
		CharSequence u = source.getUnderlying();
		int l = source.getLength();
		int o = source.getOffset();
		
		int start = 0;
		char first = u.charAt(o+start);
		
		if (first == '-' || first == '%')
		{
			start++;
			first = u.charAt(o+start);
		}
		
		if ((first >= '0' && first <= '9') || first == '.')
		{
			boolean inPossibleSignificand = false;
			boolean inSignificand = false;
			
			int nextThingStart = l;
			
			for (int i = start + 1; i < l; i++)
			{
				char c = u.charAt(o+i);
				
				if ((c >= '0' && c <= '9') || c == '.')
				{
					//Continue on :3
					
					if (inPossibleSignificand)
					{
						inPossibleSignificand = false;
						inSignificand = true;
					}
				}
				else if (c == 'd' || c == 'D' || c == 'e' || c == 'E')
				{
					if (inPossibleSignificand)
					{
						//Nope, back up!
						nextThingStart = i-1;
						break;
					}
					else if (inSignificand)
					{
						nextThingStart = i;
						break;
					}
					
					inPossibleSignificand = true;
				}
				else
				{
					nextThingStart = i;
					break;
				}
			}
			
			asrt(nextThingStart != 0);
			
			if (nextThingStart != l)
			{
				return listof(parseCodeToken1(source.subsliceFromBeginning(nextThingStart)), parseCodeToken1(source.subsliceToEnd(nextThingStart)));
			}
		}
		
		return listof(parseCodeToken1(source));
	}
	
	protected static FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> parseCodeToken1(Slice<CharSequence> source)
	{
		//Todo better parsing impl. using CharacterLists and sublist views ^^'
		//Todo but then better better parsing impl. using IntegerLists for UCS-4!  blah I'll just use Java's normal way for now x'D
		
		String text = source.getUnderlying().subSequence(source.getOffset(), source.getOffset() + source.getLength()).toString();
		
		try
		{
			return new FlatlyTypedTokenForOnlyOriginalWithLazyMaskedComputation<GeneralCAOSTokenMeaning>(source.getOffset(), source.getLength(), source.getUnderlying(), parseCodeTokenMeaning(text));
		}
		catch (TextSyntaxException | NumberFormatException exc)
		{
			throw TextSyntaxException.inst("Starting at character "+source.getOffset(), exc);
		}
	}
	
	
	protected static GeneralCAOSTokenMeaning parseCodeTokenMeaning(String s) throws TextSyntaxException, NumberFormatException
	{
		s = s.toLowerCase();
		
		if (eq(s, "<") || eq(s, "lt"))
			return GeneralCAOSTokenMeaningDataless.OperatorLT;
		
		else if (eq(s, ">") || eq(s, "gt"))
			return GeneralCAOSTokenMeaningDataless.OperatorGT;
		
		else if (eq(s, "<=") || eq(s, "le"))
			return GeneralCAOSTokenMeaningDataless.OperatorLE;
		
		else if (eq(s, ">=") || eq(s, "ge"))
			return GeneralCAOSTokenMeaningDataless.OperatorGE;
		
		else if (eq(s, "=") || eq(s, "eq"))
			return GeneralCAOSTokenMeaningDataless.OperatorEQ;
		
		else if (eq(s, "<>") || eq(s, "ne"))
			return GeneralCAOSTokenMeaningDataless.OperatorNE;
		
		else if (eq(s, "and"))
			return GeneralCAOSTokenMeaningDataless.OperatorAND;
		
		else if (eq(s, "or"))
			return GeneralCAOSTokenMeaningDataless.OperatorOR;
		
		else if (eq(s, "and!"))  //DreamCaos 8>
			return GeneralCAOSTokenMeaningDataless.OperatorANDShortCircuiting;
		
		else if (eq(s, "or!"))  //DreamCaos 8>
			return GeneralCAOSTokenMeaningDataless.OperatorORShortCircuiting;
		
		
		else if (forAll(c -> (c >= '0' && c <= '9') || c == '-', s))
			return new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralIntegerBase10, Long.parseLong(s));
		
		
		else if (s.length() >= 2 && s.charAt(0) == '%' && forAll(c -> c == '0' || c == '1', s.substring(1)))
			return new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralIntegerBase2, Long.parseLong(s.substring(1), 2));
		
		else if (s.length() >= 3 && s.charAt(0) == '-' && s.charAt(1) == '%' && forAll(c -> c == '0' || c == '1', s.substring(2)))
			return new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralIntegerBase2, -Long.parseLong(s.substring(2), 2));
		
		
		else if (eq(s, "-inf"))
			return new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralFloat, Double.NEGATIVE_INFINITY);
		else if (eq(s, "inf"))
			return new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralFloat, Double.POSITIVE_INFINITY);
		else if (eq(s, "nan"))
			return new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralFloat, Double.NaN);
		
		else if (isFloatLiteral(s))
			return new GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType.LiteralFloat, Double.parseDouble(s));
		
		else
			return GeneralCAOSTokenMeaningDataless.CodeToken;
	}
	
	public static boolean isFloatLiteral(String s)
	{
		// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/49
		// https://github.com/PuppyPi/caotic-dreams/issues/8
		
		if (s.isEmpty())
			return false;
		else
		{
			char f = s.charAt(0);
			
			if ((f >= '0' && f <= '9') || f == '-' || f == '.')
			{
				return forAll(c -> (c >= '0' && c <= '9') || c == '-' || c == '.' || c == 'e', s);
			}
			else
			{
				return false;
			}
		}
	}
	
	
	
	
	
	
	public static CharSequence descape(CharSequence s) throws TextSyntaxException
	{
		if (forAny(c -> c == '\\', s))
		{
			StringBuilder b = new StringBuilder();
			
			boolean inEscape = false;
			
			int n = s.length();
			for (int i = 0; i < n; i++)
			{
				char c = s.charAt(i);
				
				if (inEscape)
				{
					// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/50
					if (c == '\\' || c == '"' || c == '\'')
						b.append(c);
					else if (c == 'n')
						b.append('\n');
					else if (c == 'r')
						b.append('\r');
					else if (c == 't')
						b.append('\t');
					else
						throw TextSyntaxException.inst("Unrecognized escape sequence: \\"+c);
				}
				else
				{
					if (c == '\\')
						inEscape = true;
					else
						b.append(c);
				}
			}
			
			if (inEscape)
				throw TextSyntaxException.inst("Syntax error: premature EOF during string/char escape!");
			
			return b;
		}
		else
		{
			return s;
		}
	}
}
