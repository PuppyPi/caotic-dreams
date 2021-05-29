package rebound.apps.caoticdreams.caos.parser.tree;

import static java.util.Collections.*;
import static java.util.Objects.*;
import static rebound.bits.BitfieldSafeCasts.*;
import static rebound.testing.WidespreadTestingUtilities.*;
import static rebound.text.StringUtilities.*;
import static rebound.util.BasicExceptionUtilities.*;
import static rebound.util.collections.BasicCollectionUtilities.*;
import static rebound.util.collections.CollectionUtilities.*;
import static rebound.util.objectutil.BasicObjectUtilities.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import rebound.apps.caoticdreams.caos.parser.scanner.basicest.BasicCaosTokenType;
import rebound.apps.caoticdreams.caos.parser.scanner.basicest.BasicestCAOSParser;
import rebound.apps.caoticdreams.caos.parser.scanner.general.GeneralCAOSParser;
import rebound.apps.caoticdreams.caos.parser.scanner.general.GeneralCAOSTokenMeaning;
import rebound.apps.caoticdreams.caos.parser.scanner.general.GeneralCAOSTokenMeaning.GeneralCAOSTokenMeaningDataless;
import rebound.apps.caoticdreams.caos.parser.scanner.general.GeneralCAOSTokenMeaning.GeneralCAOSTokenMeaningLiteral;
import rebound.apps.caoticdreams.caos.parser.tree.ast.CAOSToplevel;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSExpression;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSFunctionInvocation;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSLiteral;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSSpecialFunctionInvocation;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSSpecialFunctionInvocation.CAOSSpecialFunction;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSCodeBlock;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSCommandInvocation;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSDoifBranch;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSDoifBranch.CAOSSingleConditionalBranch;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSLoop;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSSpecialCommandInvocation;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSSpecialCommandInvocation.CAOSSpecialCommand;
import rebound.apps.caoticdreams.caos.parser.tree.ast.statements.CAOSStatement;
import rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel.CAOSEventScript;
import rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel.CAOSFunctionDefinition;
import rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel.CAOSToplevelScript;
import rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel.CAOSToplevelScriptParameterless;
import rebound.apps.caoticdreams.caos.parser.tree.ast.toplevel.CAOSToplevelScriptParameterless.CAOSParameterlessScriptType;
import rebound.exceptions.ImpossibleException;
import rebound.exceptions.TextSyntaxException;
import rebound.text.lexing.apis.tokenstream.flat.FlatlyTypedWherefulToken;
import rebound.util.collections.PairOrdered;
import rebound.util.container.ContainerInterfaces.ObjectContainer;
import rebound.util.container.SimpleContainers.SimpleObjectContainer;

public class CAOSGrammarParser
{
	public static CAOSToplevel parseFully(String source, CAOSParsingContext parsingContext) throws TextSyntaxException
	{
		List<FlatlyTypedWherefulToken<BasicCaosTokenType>> basicallyScanParsed = BasicestCAOSParser.parse(source);
		List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> fullyScanParsed = GeneralCAOSParser.parse(basicallyScanParsed);
		return parse(fullyScanParsed, parsingContext);
	}
	
	
	public static CAOSToplevel parse(List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> input, CAOSParsingContext parsingContext) throws TextSyntaxException
	{
		CAOSGrammarParser r = new CAOSGrammarParser(input, parsingContext);
		return r.parseAll();
	}
	
	
	
	
	
	
	
	
	protected CAOSParsingContext parsingContext;
	protected final List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> input;
	protected int cursor = 0;
	
	// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/57
	// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/58
	protected final List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> installScriptBuffer = new ArrayList<>();
	
	protected List<ToplevelDirectiveBlockType> toplevelModeStack = new ArrayList<>();
	
	protected static enum ToplevelDirectiveBlockType
	{
		InstIfCompiled,
		RequireCompilation,
	}
	
	
	
	
	protected CAOSGrammarParser(List<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> input, CAOSParsingContext parsingContext)
	{
		this.parsingContext = requireNonNull(parsingContext);
		this.input = requireNonNull(input);
	}
	
	
	
	protected CAOSToplevel parseAll()
	{
		//Scan for local functions!
		{
			PairOrdered<Map<String, Integer>, Map<String, Integer>> r = scanEntiretyForLocalFunctionDefinitions();
			
			Map<String, Integer> commandDeclarations = r.getA();
			Map<String, Integer> functionDeclarations = r.getB();
			
			final CAOSParsingContext underlyingParsingContext = parsingContext;
			
			parsingContext = new CAOSParsingContext()
			{
				@Override
				public Predicate<String> isLoopOpener(CAOSCommandInvocation loopOpenerCandidate)
				{
					return underlyingParsingContext.isLoopOpener(loopOpenerCandidate);
				}
				
				@Override
				public Integer getArityForCommand(String commandName)
				{
					Integer a = commandDeclarations.get(commandName.toLowerCase());
					return a != null ? a : underlyingParsingContext.getArityForFunction(commandName);
				}
				
				@Override
				public Integer getArityForFunction(String functionName)
				{
					Integer a = functionDeclarations.get(functionName.toLowerCase());
					return a != null ? a : underlyingParsingContext.getArityForFunction(functionName);
				}
			};
		}
		
		
		
		
		//corrupted by the above scanning!
		{
			cursor = 0;
			toplevelModeStack.clear();
		}
		
		
		
		
		//Main parsing! :D
		{
			List<CAOSToplevelScript> scripts = new ArrayList<>();
			
			while (true)
			{
				CAOSToplevelScript s;
				try
				{
					s = parseScriptOrNullIfEOF();
				}
				catch (TextSyntaxException exc)
				{
					FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = input.get(cursor == 0 ? 0 : cursor - 1);
					int start = t.getStartingCharacterIndexInSource();
					int length = t.getLengthOfMaskedSource();
					
					throw TextSyntaxException.inst("At UTF-16 codepoint "+start+" (zero-based indexes), going for "+length+" codepoints.", exc);
				}
				
				if (s == null)
					break;
				else
					scripts.add(s);
			}
			
			@Nullable CAOSToplevelScriptParameterless installScript;
			{
				if (installScriptBuffer.isEmpty())
				{
					installScript = null;
				}
				else
				{
					CAOSGrammarParser p = new CAOSGrammarParser(installScriptBuffer, parsingContext);
					
					PairOrdered<CAOSCodeBlock, Map<String, CAOSCodeBlock>> b = p.parseScriptBody(false);
					
					if (b.getA().getStatements().isEmpty() && b.getB().isEmpty())  //eg, it was just comments and such X3
						installScript = null;
					else
						installScript = new CAOSToplevelScriptParameterless(b.getA(), b.getB(), CAOSParameterlessScriptType.Install);
				}
			}
			
			return new CAOSToplevel(installScript == null ? scripts : concatenateListsOPC(listof(installScript), scripts));
		}
	}
	
	
	
	/**
	 * + corrupts {@link #cursor} and {@link #toplevelModeStack}, so call this before they're used!
	 * + keys will be lowercased
	 * @return (commands, functions)
	 */
	protected PairOrdered<Map<String, Integer>, Map<String, Integer>> scanEntiretyForLocalFunctionDefinitions()
	{
		// https://github.com/PuppyPi/caotic-dreams/issues/10
		
		Map<String, Integer> commandDeclarations = null;
		Map<String, Integer> functionDeclarations = null;
		
		int n = input.size();
		
		for (int i = 0; i < n; i++)
		{
			FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = input.get(i);
			requireNonNull(t);
			
			GeneralCAOSTokenMeaning tt = t.getTokenType();
			
			if (tt == GeneralCAOSTokenMeaningDataless.CodeToken)
			{
				String s = t.getMaskedSource().toLowerCase();
				
				boolean func = s.startsWith("func");
				boolean cmdc = !func && s.startsWith("cmdc");
				
				if (func || cmdc)
				{
					int arity;
					{
						try
						{
							arity = Integer.parseInt(t.getMaskedSource().substring(4));
						}
						catch (NumberFormatException exc)
						{
							throw TextSyntaxException.inst("Function definition arity must be an integer!  Eg, FUNC0 or FUNC1 or FUNC5 or FUNC12 or etc.", exc);
						}
						
						if (arity < 0)
							throw TextSyntaxException.inst("Function definition arity must be a positive integer!  Eg, FUNC0 or FUNC1 or FUNC5 or FUNC12 or etc.");
					}	
					
					
					cursor = i;
					String name = readLabel(false).toLowerCase();
					
					Map<String, Integer> m;
					{
						if (func)
						{
							if (functionDeclarations == null)
								functionDeclarations = new HashMap<>();
							m = functionDeclarations;
						}
						else
						{
							if (commandDeclarations == null)
								commandDeclarations = new HashMap<>();
							m = commandDeclarations;
						}
					}
					
					if (m.containsKey(name))
						throw TextSyntaxException.inst("Duplicate local functions encountered!: "+repr(name));
					else
						m.put(name, arity);
				}
			}
		}
		
		return pair(commandDeclarations, functionDeclarations);
	}
	
	
	
	
	protected @Nullable CAOSToplevelScript parseScriptOrNullIfEOF()
	{
		while (true)
		{
			FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = read1();
			
			if (t == null)
				return null;
			
			GeneralCAOSTokenMeaning tt = t.getTokenType();
			
			Directive d = parseDirective(t);
			
			if (d != null)
			{
				processDirective(t, d);
				installScriptBuffer.add(t);  //we've got to do this since we parse the install script in a second pass!
			}
			else if (tt == GeneralCAOSTokenMeaningDataless.Whitespace)
			{
				//continue on X3
			}
			else if (tt == GeneralCAOSTokenMeaningDataless.Comment)
			{
				//continue on, directive-encoding ones were already handled X3
			}
			else if (tt == GeneralCAOSTokenMeaningDataless.CodeToken)
			{
				String s = t.getMaskedSource().toLowerCase();
				
				if (eq(s, "scrp"))
					return parseEventScriptAfterStart();
				
				else if (s.startsWith("func"))
					return parseFunctionDefinitionAfterStart(s.substring(4), false, true);
				
				else if (s.startsWith("fung"))
					return parseFunctionDefinitionAfterStart(s.substring(4), true, true);
				
				else if (s.startsWith("cmdc"))
					return parseFunctionDefinitionAfterStart(s.substring(4), false, false);
				
				else if (s.startsWith("cmdg"))
					return parseFunctionDefinitionAfterStart(s.substring(4), true, false);
				
				else if (eq(s, "iscr"))  //also the default X3
					soakUpInstallScriptCodeAfterISCR();
				
				else if (eq(s, "rscr"))
				{
					// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/10
					// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/60
					PairOrdered<CAOSCodeBlock, Map<String, CAOSCodeBlock>> b = parseScriptBody(false);
					return new CAOSToplevelScriptParameterless(b.getA(), b.getB(), CAOSParameterlessScriptType.Remove);
				}
				
				else if (eq(s, "endm"))
				{
					//throw TextSyntaxException.inst("ENDM not permitted without a matching SCRP, ISCR, RSCR, FUNC, or FUNG");
					
					//It isssssss now!  So just ignore it :3
					// https://github.com/PuppyPi/caotic-dreams/issues/11
				}
				
				else
				{
					installScriptBuffer.add(t);
				}
			}
			else
			{
				installScriptBuffer.add(t);
			}
		}
	}
	
	protected void processDirective(FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t, Directive d)
	{
		if (d == Directive.InstIfCompiledStart)
			toplevelModeStack.add(ToplevelDirectiveBlockType.InstIfCompiled);
		else if (d == Directive.RequireCompilationStart)
			toplevelModeStack.add(ToplevelDirectiveBlockType.RequireCompilation);
		else
		{
			ToplevelDirectiveBlockType m;
			
			if (d == Directive.InstIfCompiledStart)
				m = ToplevelDirectiveBlockType.InstIfCompiled;
			else if (d == Directive.RequireCompilationStart)
				m = ToplevelDirectiveBlockType.RequireCompilation;
			else
				throw newUnexpectedHardcodedEnumValueExceptionOrNullPointerException(d);
			
			if (toplevelModeStack.isEmpty())
				throw TextSyntaxException.inst("Ending "+t.getMaskedSource()+" encountered without any opener!");
			else if (toplevelModeStack.get(toplevelModeStack.size()-1) != m)
				throw TextSyntaxException.inst("Ending "+t.getMaskedSource()+" encountered without a properly-nested matching opener!");
		}
	}
	
	protected CAOSEventScript parseEventScriptAfterStart()
	{
		int family = readIntegerLiteral();
		int genus = readIntegerLiteral();
		int species = readIntegerLiteral();
		int event = readIntegerLiteral();
		
		PairOrdered<CAOSCodeBlock, Map<String, CAOSCodeBlock>> b = parseScriptBody(true);
		
		return new CAOSEventScript(b.getA(), b.getB(), family, genus, species, event);
	}
	
	
	protected CAOSFunctionDefinition parseFunctionDefinitionAfterStart(String openerArity, boolean global, boolean hasReturnValue)
	{
		// https://github.com/PuppyPi/caotic-dreams/issues/10
		
		int arity;
		{
			try
			{
				arity = Integer.parseInt(openerArity);
			}
			catch (NumberFormatException exc)
			{
				throw TextSyntaxException.inst("Function definition arity must be an integer!  Eg, FUNC0 or FUNC1 or FUNC5 or FUNC12 or etc.", exc);
			}
			
			if (arity < 0)
				throw TextSyntaxException.inst("Function definition arity must be a positive integer!  Eg, FUNC0 or FUNC1 or FUNC5 or FUNC12 or etc.");
		}	
		
		String name = readLabel(global);
		
		PairOrdered<CAOSCodeBlock, Map<String, CAOSCodeBlock>> b = parseScriptBody(true);
		
		return new CAOSFunctionDefinition(b.getA(), b.getB(), name, arity, global, hasReturnValue);
	}
	
	
	
	protected void soakUpInstallScriptCodeAfterISCR()
	{
		// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/57
		// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/58
		
		while (true)
		{
			FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = read1();
			
			if (t == null)
				break;
			
			Directive d = parseDirective(t);
			
			if (d != null)
			{
				processDirective(t, d);
				installScriptBuffer.add(t);  //we've got to do this since we parse the install script in a second pass!
			}
			else
			{
				if (t.getTokenType() == GeneralCAOSTokenMeaningDataless.CodeToken)
				{
					String s = t.getMaskedSource().toLowerCase();
					
					// https://github.com/Creatures-Developer-Network/c2e-quirks/issues/59
					if (eq(s, "scrp") || s.startsWith("rscr") || s.startsWith("func") || s.startsWith("fung") || s.startsWith("cmdc") || s.startsWith("cmdg"))
					{
						unread1();
						break;
					}
					
					else if (eq(s, "iscr"))  //also the default X3
						throw TextSyntaxException.inst("ISCR not permitted inside another ISCR");
					
					else if (eq(s, "endm"))
						break;
				}
				
				installScriptBuffer.add(t);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	protected PairOrdered<CAOSCodeBlock, Map<String, CAOSCodeBlock>> parseScriptBody(boolean endmRequired)
	{
		ObjectContainer<String> currentSubr = new SimpleObjectContainer<>(null);
		ObjectContainer<String> nextSubr = new SimpleObjectContainer<>();
		
		CAOSCodeBlock firstBlock = null;
		Map<String, CAOSCodeBlock> subroutines = null;
		
		while (true)
		{
			CAOSCodeBlock block = parseUntil(true, t ->
			{
				if (t == null)
				{
					if (endmRequired)
					{
						throw TextSyntaxException.inst("Premature EOF, expecting ENDM");
					}
					else
					{
						nextSubr.set(null);
						return true;
					}
				}
				else
				{
					GeneralCAOSTokenMeaning tt = t.getTokenType();
					
					if (tt == GeneralCAOSTokenMeaningDataless.CodeToken)
					{
						String s = t.getMaskedSource().toLowerCase();
						
						if (eq(s, "endm"))
						{
							nextSubr.set(null);
							return true;
						}
						else if (eq(s, "subr"))
						{
							String name = readLabel(true);
							nextSubr.set(name);
							return true;
						}
					}
				}
				
				return false;
			});
			
			
			String l = currentSubr.get();
			
			if (l == null)
			{
				asrt(firstBlock == null);
				firstBlock = block;
			}
			else
			{
				if (subroutines != null && subroutines.containsKey(l))
					throw TextSyntaxException.inst("Duplicate SUBRoutines: "+repr(l));
				
				if (subroutines == null)
					subroutines = new HashMap<>();
				
				subroutines.put(l, block);
			}
			
			
			String n = nextSubr.get();
			
			if (n == null)
				break;
			else
				currentSubr.set(n);
		}
		
		
		asrt(firstBlock != null);
		
		return pair(firstBlock, subroutines == null ? emptyMap() : subroutines);
	}
	
	
	
	
	
	protected CAOSCodeBlock parseUntil(boolean bringInToplevelModeStack, Predicate<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> stop)
	{
		List<CAOSStatement> statements = new ArrayList<>();
		
		
		if (bringInToplevelModeStack)
		{
			for (ToplevelDirectiveBlockType m : toplevelModeStack)
			{
				if (m == ToplevelDirectiveBlockType.InstIfCompiled)
					statements.add(new CAOSSpecialCommandInvocation(CAOSSpecialCommand.InstIfCompiledStart, listof()));  //no location info since it's not really there XD'
				else if (m == ToplevelDirectiveBlockType.RequireCompilation)
					statements.add(new CAOSSpecialCommandInvocation(CAOSSpecialCommand.RequireCompilationStart, listof()));  //no location info since it's not really there XD'
				else
					throw newUnexpectedHardcodedEnumValueExceptionOrNullPointerException(m);
			}
		}
		
		
		while (true)
		{
			FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = read1();
			
			Directive d = t == null ? null : parseDirective(t);
			
			if (d != null)
				processDirective(t, d);
			
			if (stop.test(t))
				break;
			else
			{
				asrt(t != null);  //the stop checker should have dealt with this!
				
				if (d != null)
				{
					CAOSSpecialCommand c;
					
					if (d == Directive.InstIfCompiledStart)
						c = CAOSSpecialCommand.InstIfCompiledStart;
					else if (d == Directive.InstIfCompiledEnd)
						c = CAOSSpecialCommand.InstIfCompiledEnd;
					else if (d == Directive.RequireCompilationStart)
						c = CAOSSpecialCommand.RequireCompilationStart;
					else if (d == Directive.RequireCompilationEnd)
						c = CAOSSpecialCommand.RequireCompilationEnd;
					else
						throw newUnexpectedHardcodedEnumValueExceptionOrNullPointerException(d);
					
					CAOSSpecialCommandInvocation sc = new CAOSSpecialCommandInvocation(c, emptyList());
					sc.setStartingPointInSource(t.getStartingCharacterIndexInSource());
					sc.setLengthInSource(t.getLengthOfMaskedSource());
					statements.add(sc);
				}
				else
				{
					GeneralCAOSTokenMeaning tt = t.getTokenType();
					
					if (tt == GeneralCAOSTokenMeaningDataless.CodeToken)
					{
						String s = t.getMaskedSource().toLowerCase();
						
						if (eq(s, "doif"))
							statements.add(parseDoif(stop));
						
						else if (s.startsWith("ccmd"))
							statements.add(parseGlobalCommandInvocationAfterStart(s.substring(4), true, t));
						
						else if (s.startsWith("dcmd"))
							statements.add(parseGlobalCommandInvocationAfterStart(s.substring(4), false, t));
						
						else if (eq(s, "pcmd"))
							statements.add(parseDynamicArgGlobalCommandInvocationAfterStart(s.substring(4), true, t));
						
						else if (eq(s, "acmd"))
							statements.add(parseDynamicArgGlobalCommandInvocationAfterStart(s.substring(4), false, t));
						
						else if (eq(s, "gsub"))
							statements.add(parseGoSubroutineAfterStart(t));
						
						else
						{
							CAOSCommandInvocation normalCommand = parseCommand(t);
							
							Predicate<String> c = parsingContext.isLoopOpener(normalCommand);
							
							if (c != null)
							{
								statements.add(parseLoop(normalCommand, c, stop));
							}
							else
							{
								statements.add(normalCommand);
							}
						}
					}
					else if (tt == GeneralCAOSTokenMeaningDataless.Comment || tt == GeneralCAOSTokenMeaningDataless.Whitespace)
					{
						//Skip :3
					}
					else
					{
						throw TextSyntaxException.inst("Expected command or pseudocommand, instead got: "+t.getMaskedSource());
					}
				}
			}
		}
		
		
		return new CAOSCodeBlock(statements);
	}
	
	
	protected PairOrdered<CAOSCodeBlock, FlatlyTypedWherefulToken> parseUntilCommandlikeToken(Predicate<String> isEndingCommandName, Predicate<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> parentStop)
	{
		ObjectContainer<FlatlyTypedWherefulToken> endingCommandToken_C = new SimpleObjectContainer<>(null);
		
		CAOSCodeBlock body = parseUntil(false, t ->
		{
			if (parentStop.test(t))
				throw TextSyntaxException.inst("Premature end of script, expected end of loop!");
			
			if (t.getTokenType() == GeneralCAOSTokenMeaningDataless.CodeToken && isEndingCommandName.test(t.getMaskedSource()))
			{
				endingCommandToken_C.set(t);
				return true;
			}
			
			return false;
		});
		
		
		FlatlyTypedWherefulToken endingCommandToken = endingCommandToken_C.get();
		asrt(endingCommandToken != null);
		
		return pair(body, endingCommandToken);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	protected CAOSDoifBranch parseDoif(Predicate<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> parentStop)
	{
		PairOrdered<CAOSSingleConditionalBranch, AfterDoifType> first = parseDoifElif(parentStop);
		
		CAOSSingleConditionalBranch doif = first.getA();
		
		List<CAOSSingleConditionalBranch> elifs;
		final AfterDoifType after;
		{
			AfterDoifType af0 = first.getB();
			
			if (af0 == AfterDoifType.Elif)
			{
				elifs = new ArrayList<>();
				
				while (true)
				{
					PairOrdered<CAOSSingleConditionalBranch, AfterDoifType> elif = parseDoifElif(parentStop);
					
					elifs.add(elif.getA());
					
					AfterDoifType afN = elif.getB();
					
					if (afN == AfterDoifType.Elif)
					{
						//Continue on :3
					}
					else
					{
						after = afN;
						break;
					}
				}
			}
			else
			{
				elifs = emptyList();
				after = af0;
			}
		}
		
		
		
		CAOSCodeBlock elseBlock;
		{
			if (after == AfterDoifType.Else)
			{
				PairOrdered<CAOSCodeBlock, FlatlyTypedWherefulToken> r = parseUntilCommandlikeToken(n -> equalsIgnoreCase(n, "endi"), parentStop);
				elseBlock = requireNonNull(r.getA());
			}
			else if (after == AfterDoifType.Endi)
			{
				elseBlock = null;
			}
			else
			{
				throw newUnexpectedHardcodedEnumValueExceptionOrNullPointerException(after);
			}
		}
		
		
		
		return new CAOSDoifBranch(doif, elifs, elseBlock);
	}
	
	
	
	
	protected PairOrdered<CAOSSingleConditionalBranch, AfterDoifType> parseDoifElif(Predicate<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> parentStop)
	{
		CAOSExpression condition = parseExpression();
		
		PairOrdered<CAOSCodeBlock, FlatlyTypedWherefulToken> r = parseUntilCommandlikeToken(n -> equalsIgnoreCase(n, "elif") || equalsIgnoreCase(n, "else") || equalsIgnoreCase(n, "endi"), parentStop);
		
		CAOSCodeBlock body = r.getA();
		
		FlatlyTypedWherefulToken endingCommandToken = r.getB();
		
		AfterDoifType after;
		{
			String n = endingCommandToken.getMaskedSource().toLowerCase();
			
			if (eq(n, "elif"))
				after = AfterDoifType.Elif;
			
			else if (eq(n, "else"))
				after = AfterDoifType.Else;
			
			else if (eq(n, "endi"))
				after = AfterDoifType.Endi;
			
			else
				throw new AssertionError();
		}
		
		return pair(new CAOSSingleConditionalBranch(condition, body), after);
	}
	
	
	protected static enum AfterDoifType
	{
		Elif,
		Else,
		Endi,
	}
	
	
	
	protected CAOSLoop parseLoop(CAOSCommandInvocation start, Predicate<String> isEndingCommandName, Predicate<FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning>> parentStop)
	{
		PairOrdered<CAOSCodeBlock, FlatlyTypedWherefulToken> r = parseUntilCommandlikeToken(isEndingCommandName, parentStop);
		CAOSCodeBlock body = r.getA();
		FlatlyTypedWherefulToken endingCommandToken = r.getB();
		
		CAOSCommandInvocation end = parseCommand(endingCommandToken);
		
		return new CAOSLoop(start, end, body);
	}
	
	
	
	
	
	
	
	
	
	
	protected CAOSSpecialCommandInvocation parseGlobalCommandInvocationAfterStart(String openerArity, boolean usesLabel, FlatlyTypedWherefulToken token)
	{
		// https://github.com/PuppyPi/caotic-dreams/issues/10
		
		int arity;
		{
			try
			{
				arity = Integer.parseInt(openerArity);
			}
			catch (NumberFormatException exc)
			{
				throw TextSyntaxException.inst("Global command invocation arity must be an integer!  Eg, CCMD0 or CCMD1 or CCMD5 or CCMD12 or etc.", exc);
			}
			
			if (arity < 0)
				throw TextSyntaxException.inst("Global command invocation arity must be a positive integer!  Eg, CCMD0 or CCMD1 or CCMD5 or CCMD12 or etc.");
		}	
		
		CAOSExpression name;
		{
			if (usesLabel)
				name = new CAOSLiteral(requireNonNull(readLabel(true)));
			else
				name = parseExpression();
		}
		
		List<CAOSExpression> parameters = arity == 0 ? emptyList() : new ArrayList<>(arity);
		
		for (int i = 0; i < arity; i++)
			parameters.add(parseExpression());
		
		CAOSSpecialCommandInvocation command = new CAOSSpecialCommandInvocation(CAOSSpecialCommand.CCMDorDCMD, concatenateListsOPC(listof(name), parameters));
		command.setStartingPointInSource(token.getStartingCharacterIndexInSource());
		command.setLengthInSource(token.getLengthOfMaskedSource());
		
		return command;
	}
	
	protected CAOSSpecialCommandInvocation parseDynamicArgGlobalCommandInvocationAfterStart(String openerArity, boolean usesLabel, FlatlyTypedWherefulToken token)
	{
		// https://github.com/PuppyPi/caotic-dreams/issues/10
		
		CAOSExpression name;
		{
			if (usesLabel)
				name = new CAOSLiteral(requireNonNull(readLabel(true)));
			else
				name = parseExpression();
		}
		
		CAOSExpression args = parseExpression();
		
		CAOSSpecialCommandInvocation command = new CAOSSpecialCommandInvocation(CAOSSpecialCommand.PCMDorACMD, listof(name, args));
		command.setStartingPointInSource(token.getStartingCharacterIndexInSource());
		command.setLengthInSource(token.getLengthOfMaskedSource());
		
		return command;
	}
	
	protected CAOSSpecialCommandInvocation parseGoSubroutineAfterStart(FlatlyTypedWherefulToken token)
	{
		// https://github.com/PuppyPi/caotic-dreams/issues/10
		
		CAOSExpression name = new CAOSLiteral(requireNonNull(readLabel(true)));
		
		CAOSSpecialCommandInvocation command = new CAOSSpecialCommandInvocation(CAOSSpecialCommand.GSUB, listof(name));
		command.setStartingPointInSource(token.getStartingCharacterIndexInSource());
		command.setLengthInSource(token.getLengthOfMaskedSource());
		
		return command;
	}
	
	
	
	
	
	
	protected CAOSCommandInvocation parseCommand(FlatlyTypedWherefulToken token)
	{
		String name = token.getMaskedSource();
		
		Integer arity = parsingContext.getArityForCommand(name);
		
		if (arity == null)
			throw TextSyntaxException.inst("Unrecognized command "+name.toUpperCase());
		
		List<CAOSExpression> parameters = arity == 0 ? emptyList() : new ArrayList<>(arity);
		
		for (int i = 0; i < arity; i++)
			parameters.add(parseExpression());
		
		CAOSCommandInvocation command = new CAOSCommandInvocation(name, parameters);
		command.setStartingPointInSource(token.getStartingCharacterIndexInSource());
		command.setLengthInSource(token.getLengthOfMaskedSource());
		
		return command;
	}
	
	
	
	
	
	
	
	
	
	protected CAOSExpression parseExpression()
	{
		CAOSExpression lastExpression = null;
		GeneralCAOSTokenMeaningDataless infixOperator = null;
		FlatlyTypedWherefulToken infixOperatorToken = null;
		
		
		while (true)
		{
			boolean first = lastExpression == null;
			
			FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = read1();
			
			if (t == null)
				throw TextSyntaxException.inst("Premature EOF in the middle of an expression!");
			
			if (parseDirective(t) != null)
			{
				if (infixOperator == null && !first)
				{
					unread1();
					break;
				}
				else
				{
					throw TextSyntaxException.inst("Commands (and directives) can't be inside expressions!");
				}
			}
			else
			{
				GeneralCAOSTokenMeaning tt = t.getTokenType();
				
				if (tt == GeneralCAOSTokenMeaningDataless.CodeToken || tt instanceof GeneralCAOSTokenMeaningLiteral)
				{
					if (infixOperator == null && !first)
					{
						unread1();
						break;
					}
					else
					{
						CAOSExpression expr;
						{
							if (tt == GeneralCAOSTokenMeaningDataless.CodeToken)
							{
								String s = t.getMaskedSource().toLowerCase();
								
								if (s.startsWith("cfun"))
									expr = parseGlobalFunctionInvocationAfterStart(s.substring(4), true, t);
								
								else if (s.startsWith("dfun"))
									expr = parseGlobalFunctionInvocationAfterStart(s.substring(4), false, t);
								
								else if (eq(s, "pfun"))
									expr = parseDynamicArgGlobalFunctionInvocationAfterStart(s.substring(4), true, t);
								
								else if (eq(s, "afun"))
									expr = parseDynamicArgGlobalFunctionInvocationAfterStart(s.substring(4), false, t);
								
								else
									expr = parseFunction(t);
							}
							else
							{
								expr = new CAOSLiteral(((GeneralCAOSTokenMeaningLiteral)tt).getLiteralValue());
							}
						}
						
						expr.setStartingPointInSource(t.getStartingCharacterIndexInSource());
						expr.setLengthInSource(t.getLengthOfMaskedSource());
						
						if (first)
						{
							asrt(infixOperator == null);
							lastExpression = expr;
						}
						else
						{
							asrt(infixOperator != null);
							lastExpression = produceInfixFunctionInvocation(lastExpression, infixOperator, expr, infixOperatorToken);
							infixOperator = null;
							infixOperatorToken = null;
						}
					}
				}
				
				else if (tt == GeneralCAOSTokenMeaningDataless.Comment || tt == GeneralCAOSTokenMeaningDataless.Whitespace)
				{
					//Skip since it's not a directive :3
				}
				
				else if (tt instanceof GeneralCAOSTokenMeaningDataless && ((GeneralCAOSTokenMeaningDataless)tt).isOperator())
				{
					if (first)
					{
						asrt(infixOperator == null);
						throw TextSyntaxException.inst("Infix operator was encountered with nothing before it!: "+tt);
					}
					else if (infixOperator == null)
					{
						infixOperator = (GeneralCAOSTokenMeaningDataless) tt;
						infixOperatorToken = t;
					}
					else
					{
						throw TextSyntaxException.inst("Two infix operators were juxtaposed: "+infixOperator+" and "+tt);
					}
				}
				
				else
				{
					throw newClassCastExceptionOrNullPointerException(tt);
				}
			}
		}
		
		
		asrt(lastExpression != null);
		
		return lastExpression;
	}
	
	protected CAOSExpression produceInfixFunctionInvocation(CAOSExpression firstParameter, GeneralCAOSTokenMeaningDataless operator, CAOSExpression secondParameter, FlatlyTypedWherefulToken operatorToken)
	{
		CAOSSpecialFunction function;
		{
			if (operator == GeneralCAOSTokenMeaningDataless.OperatorEQ)
				function = CAOSSpecialFunction.EQ;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorNE)
				function = CAOSSpecialFunction.NE;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorLT)
				function = CAOSSpecialFunction.LT;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorLE)
				function = CAOSSpecialFunction.LE;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorGT)
				function = CAOSSpecialFunction.GT;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorGE)
				function = CAOSSpecialFunction.GE;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorAND)
				function = CAOSSpecialFunction.AND;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorOR)
				function = CAOSSpecialFunction.OR;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorANDShortCircuiting)
				function = CAOSSpecialFunction.ANDShortCircuiting;
			
			else if (operator == GeneralCAOSTokenMeaningDataless.OperatorORShortCircuiting)
				function = CAOSSpecialFunction.ORShortCircuiting;
			
			else
				throw TextSyntaxException.inst();
		}
		
		CAOSSpecialFunctionInvocation f = new CAOSSpecialFunctionInvocation(function, listof(firstParameter, secondParameter));
		f.setStartingPointInSource(operatorToken.getStartingCharacterIndexInSource());
		f.setLengthInSource(operatorToken.getLengthOfMaskedSource());
		return f;
	}
	
	
	
	
	
	protected CAOSSpecialFunctionInvocation parseGlobalFunctionInvocationAfterStart(String openerArity, boolean usesLabel, FlatlyTypedWherefulToken token)
	{
		// https://github.com/PuppyPi/caotic-dreams/issues/10
		
		int arity;
		{
			try
			{
				arity = Integer.parseInt(openerArity);
			}
			catch (NumberFormatException exc)
			{
				throw TextSyntaxException.inst("Global function invocation arity must be an integer!  Eg, CCMD0 or CCMD1 or CCMD5 or CCMD12 or etc.", exc);
			}
			
			if (arity < 0)
				throw TextSyntaxException.inst("Global function invocation arity must be a positive integer!  Eg, CCMD0 or CCMD1 or CCMD5 or CCMD12 or etc.");
		}	
		
		CAOSExpression name;
		{
			if (usesLabel)
				name = new CAOSLiteral(requireNonNull(readLabel(true)));
			else
				name = parseExpression();
		}
		
		List<CAOSExpression> parameters = arity == 0 ? emptyList() : new ArrayList<>(arity);
		
		for (int i = 0; i < arity; i++)
			parameters.add(parseExpression());
		
		CAOSSpecialFunctionInvocation function = new CAOSSpecialFunctionInvocation(CAOSSpecialFunction.CFUNorDFUN, concatenateListsOPC(listof(name), parameters));
		function.setStartingPointInSource(token.getStartingCharacterIndexInSource());
		function.setLengthInSource(token.getLengthOfMaskedSource());
		
		return function;
	}
	
	protected CAOSSpecialFunctionInvocation parseDynamicArgGlobalFunctionInvocationAfterStart(String openerArity, boolean usesLabel, FlatlyTypedWherefulToken token)
	{
		// https://github.com/PuppyPi/caotic-dreams/issues/10
		
		CAOSExpression name;
		{
			if (usesLabel)
				name = new CAOSLiteral(requireNonNull(readLabel(true)));
			else
				name = parseExpression();
		}
		
		CAOSExpression args = parseExpression();
		
		CAOSSpecialFunctionInvocation function = new CAOSSpecialFunctionInvocation(CAOSSpecialFunction.PFUNorAFUN, listof(name, args));
		function.setStartingPointInSource(token.getStartingCharacterIndexInSource());
		function.setLengthInSource(token.getLengthOfMaskedSource());
		
		return function;
	}
	
	protected CAOSFunctionInvocation parseFunction(FlatlyTypedWherefulToken token)
	{
		String name = token.getMaskedSource();
		
		Integer arity = parsingContext.getArityForFunction(name);
		
		if (arity == null)
			throw TextSyntaxException.inst("Unrecognized function "+name.toUpperCase());
		
		List<CAOSExpression> parameters = arity == 0 ? emptyList() : new ArrayList<>(arity);
		
		for (int i = 0; i < arity; i++)
			parameters.add(parseExpression());
		
		CAOSFunctionInvocation function = new CAOSFunctionInvocation(name, parameters);
		function.setStartingPointInSource(token.getStartingCharacterIndexInSource());
		function.setLengthInSource(token.getLengthOfMaskedSource());
		
		return function;
	}
	
	
	
	
	
	
	
	protected static enum Directive
	{
		InstIfCompiledStart,  //in comment tokens
		InstIfCompiledEnd,  //in comment tokens
		RequireCompilationStart,  //separate syntactical structure like a nullary command
		RequireCompilationEnd,  //separate syntactical structure like a nullary command
	}
	
	protected static @Nullable Directive parseDirective(FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t)
	{
		if (t != null)
		{
			GeneralCAOSTokenMeaning tt = t.getTokenType();
			
			if (tt == GeneralCAOSTokenMeaningDataless.CodeToken)
			{
				String s = t.getMaskedSource().toLowerCase();
				
				if (eq(s, "comp"))
					return Directive.RequireCompilationStart;
				else if (eq(s, "endc"))
					return Directive.RequireCompilationEnd;
			}
			else if (tt == GeneralCAOSTokenMeaningDataless.Comment)
			{
				String s = t.getMaskedSource();
				
				if (s.length() > 3)
				{
					if (s.startsWith("*!"))
					{
						s = s.substring(2).trim().toLowerCase();
						
						if (eq(s, "inst?"))
							return Directive.InstIfCompiledStart;
						else if (eq(s, "slow?"))
							return Directive.InstIfCompiledEnd;
					}
				}
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	protected int readIntegerLiteral()
	{
		FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = read1();
		
		if (t == null)
			throw TextSyntaxException.inst("Premature EOF: expected integer");
		
		GeneralCAOSTokenMeaning tt = t.getTokenType();
		
		if (tt instanceof GeneralCAOSTokenMeaningLiteral)
		{
			Object v = ((GeneralCAOSTokenMeaningLiteral) tt).getLiteralValue();
			
			if (v instanceof Long)
				return safeCastS64toS32((Long)v);
			else if (v instanceof Integer)
				throw new ImpossibleException();
			else
				throw TextSyntaxException.inst("Expected integer, got "+t.getMaskedSource());
		}
		else
		{
			throw TextSyntaxException.inst("Expected integer, got "+t.getMaskedSource());
		}
	}
	
	protected String readLabel(boolean allowStringLiterals)
	{
		FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> t = read1();
		
		if (t == null)
			throw TextSyntaxException.inst("Premature EOF: expected "+(allowStringLiterals ? "normal label or double-quoted label" : "label"));
		
		GeneralCAOSTokenMeaning tt = t.getTokenType();
		
		if (tt == GeneralCAOSTokenMeaningDataless.CodeToken)
		{
			return t.getMaskedSource();
		}
		else if (tt instanceof GeneralCAOSTokenMeaningLiteral && allowStringLiterals)
		{
			Object v = ((GeneralCAOSTokenMeaningLiteral) tt).getLiteralValue();
			
			if (v instanceof String)
				return (String)v;
			else if (v instanceof CharSequence)
				throw new ImpossibleException();
			else
				throw TextSyntaxException.inst("Expected normal label or double-quoted label, got "+t.getMaskedSource());
		}
		else
		{
			throw TextSyntaxException.inst("Expected "+(allowStringLiterals ? "normal label or double-quoted label" : "label")+", got "+t.getMaskedSource());
		}
	}
	
	
	
	
	
	
	protected @Nullable FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> read1()
	{
		if (cursor < input.size())
		{
			FlatlyTypedWherefulToken<GeneralCAOSTokenMeaning> r = input.get(cursor);
			cursor++;
			return r;
		}
		else
		{
			return null;  //EOF
		}
	}
	
	protected void unread1()
	{
		if (cursor == 0)
			throw new IllegalStateException();
		cursor--;
	}
}
