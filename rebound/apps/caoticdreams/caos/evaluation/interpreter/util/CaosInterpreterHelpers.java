package rebound.apps.caoticdreams.caos.evaluation.interpreter.util;

import static rebound.util.collections.CollectionUtilities.*;
import java.util.List;
import javax.annotation.Nullable;
import rebound.annotations.semantic.simpledata.Nonempty;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.CaosBranch;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.CaosExpression;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.CaosStatement;

public class CaosInterpreterHelpers
{
	public static class ConditionBlock
	{
		public final CaosExpression condition;
		public final CaosStatement bodyStart;
		public final int startInSourceCode;
		
		public ConditionBlock(CaosExpression condition, CaosStatement bodyStart, int startInSourceCode)
		{
			this.condition = condition;
			this.bodyStart = bodyStart;
			this.startInSourceCode = startInSourceCode;
		}
	}
	
	
	
	/**
	 * Note that you still need to set all the last Next's in each block to the statement that comes after ENDI if appropriate!  (inappropriate would be if they preceded a GSUB, for example X3 )
	 * 
	 * @param elseStart  null if there is no ELSE block
	 * @param elseStartInSourceCode  the position of the first char in "ELSE" or whatever you want if elseStart == null
	 * @param endiEndInSourceCode  the position of the first character after the "I" in "ENDI"  :3
	 */
	public static CaosStatement makeDoif(@Nonempty List<ConditionBlock> blocks, @Nullable CaosStatement elseStart, int elseStartInSourceCode, int endiEndInSourceCode)
	{
		requireNonEmpty(blocks);
		
		CaosBranch firstBranch = null;
		CaosBranch lastBranch = null;
		
		int n = blocks.size();
		for (int i = 0; i < n; i++)
		{
			ConditionBlock block = blocks.get(i);
			
			CaosExpression condition = block.condition;
			CaosStatement bodyStart = block.bodyStart;
			int startInSourceCode = block.startInSourceCode;
			
			int endInSourceCode;
			{
				if (i == n - 1)
					endInSourceCode = elseStart == null ? endiEndInSourceCode : elseStartInSourceCode;
				else
					endInSourceCode = blocks.get(i+1).startInSourceCode;
			}
			
			CaosBranch thisBranch = new CaosBranch(startInSourceCode, endInSourceCode - startInSourceCode);
			
			thisBranch.setCondition(condition);
			thisBranch.setNextIfTrue(bodyStart);
			
			if (i == 0)
			{
				firstBranch = thisBranch;
			}
			else
			{
				lastBranch.setNextIfFalse(thisBranch);
			}
			
			lastBranch = thisBranch;
		}
		
		if (elseStart != null)
		{
			lastBranch.setNextIfFalse(elseStart);
		}
		
		return firstBranch;
	}
	
	
	
	
	//TODO Loopssssss! :D
}
