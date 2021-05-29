package rebound.apps.caoticdreams.caos.evaluation.interpreter.util;

import static rebound.util.collections.CollectionUtilities.*;
import java.util.List;
import javax.annotation.Nullable;
import rebound.annotations.semantic.simpledata.Nonempty;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.LiveCaosBranch;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.LiveCaosExpression;
import rebound.apps.caoticdreams.caos.evaluation.interpreter.LiveCaosStatement;

public class CaosInterpreterHelpers
{
	public static class ConditionBlock
	{
		public final LiveCaosExpression condition;
		public final LiveCaosStatement bodyStart;
		public final int startInSourceCode;
		
		public ConditionBlock(LiveCaosExpression condition, LiveCaosStatement bodyStart, int startInSourceCode)
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
	public static LiveCaosStatement makeDoif(@Nonempty List<ConditionBlock> blocks, @Nullable LiveCaosStatement elseStart, int elseStartInSourceCode, int endiEndInSourceCode)
	{
		requireNonEmpty(blocks);
		
		LiveCaosBranch firstBranch = null;
		LiveCaosBranch lastBranch = null;
		
		int n = blocks.size();
		for (int i = 0; i < n; i++)
		{
			ConditionBlock block = blocks.get(i);
			
			LiveCaosExpression condition = block.condition;
			LiveCaosStatement bodyStart = block.bodyStart;
			int startInSourceCode = block.startInSourceCode;
			
			int endInSourceCode;
			{
				if (i == n - 1)
					endInSourceCode = elseStart == null ? endiEndInSourceCode : elseStartInSourceCode;
				else
					endInSourceCode = blocks.get(i+1).startInSourceCode;
			}
			
			LiveCaosBranch thisBranch = new LiveCaosBranch(startInSourceCode, endInSourceCode - startInSourceCode);
			
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
