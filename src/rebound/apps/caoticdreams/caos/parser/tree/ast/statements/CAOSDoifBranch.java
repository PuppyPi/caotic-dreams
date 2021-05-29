package rebound.apps.caoticdreams.caos.parser.tree.ast.statements;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.annotations.semantic.simpledata.Emptyable;
import rebound.annotations.semantic.simpledata.NonnullElements;
import rebound.apps.caoticdreams.caos.parser.tree.ast.CAOSSyntacticStructureRememberingLocationInformation;
import rebound.apps.caoticdreams.caos.parser.tree.ast.expressions.CAOSExpression;

public class CAOSDoifBranch
extends CAOSStatement
{
	protected final @Nonnull CAOSSingleConditionalBranch doif;
	protected final @NonnullElements @Nonnull @Emptyable List<CAOSSingleConditionalBranch> elifs;
	protected final @Nullable CAOSCodeBlock elseBlock;
	
	
	
	public static class CAOSSingleConditionalBranch
	extends CAOSSyntacticStructureRememberingLocationInformation
	{
		protected final @Nonnull CAOSExpression condition;
		protected final CAOSCodeBlock ifTrueBlock;
		
		
		
		public CAOSSingleConditionalBranch(CAOSExpression condition, CAOSCodeBlock ifTrueBlock)
		{
			this.condition = condition;
			this.ifTrueBlock = ifTrueBlock;
		}
		
		public CAOSExpression getCondition()
		{
			return condition;
		}
		
		public CAOSCodeBlock getIfTrueBlock()
		{
			return ifTrueBlock;
		}
	}
	
	
	
	
	
	
	
	public CAOSDoifBranch(CAOSSingleConditionalBranch doif, List<CAOSSingleConditionalBranch> elifs, CAOSCodeBlock elseBlock)
	{
		this.doif = doif;
		this.elifs = elifs;
		this.elseBlock = elseBlock;
	}
	
	public CAOSSingleConditionalBranch getDoif()
	{
		return doif;
	}
	
	public List<CAOSSingleConditionalBranch> getElifs()
	{
		return elifs;
	}
	
	public CAOSCodeBlock getElseBlock()
	{
		return elseBlock;
	}
}
