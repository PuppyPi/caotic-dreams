package rebound.apps.caoticdreams.caos.parser.scanner.general;

import rebound.text.UCS4CodePoint;
import rebound.util.collections.prim.PrimitiveCollections.ImmutableByteArrayList;

public interface GeneralCAOSTokenMeaning
{
	public static enum GeneralCAOSTokenMeaningDataless
	implements GeneralCAOSTokenMeaning
	{
		/**
		 * A word of code (usually but in this system, not necessarily! four letters), and can definitely contain at least underscores, colons, and plusses!
		 * 
		 * Functions (eg, CARR)
		 * Procedures aka Commands (eg, DROP)
		 * Subtoken (eg, NEW: and COMP)
		 * Flow control (eg, DOIF, ENUM, SUBR, SCRP, ENDM, etc.)
		 * Local variables (vaXX)
		 * TARG agent variables (ovXX)
		 * OWNR agent variables (mvXX)
		 */
		CodeToken,
		
		Comment,
		Whitespace,
		
		//The only infix things in CAOS XD
		OperatorEQ,
		OperatorNE,
		OperatorLT,
		OperatorLE,
		OperatorGT,
		OperatorGE,
		OperatorAND,
		OperatorOR,
		OperatorANDShortCircuiting,
		OperatorORShortCircuiting,
		;
		
		
		public boolean isOperator()
		{
			return this != CodeToken && this != Comment && this != Whitespace;
		}
	}
	
	
	
	
	
	public static class GeneralCAOSTokenMeaningLiteral
	implements GeneralCAOSTokenMeaning
	{
		public static enum GeneralCAOSTokenMeaningLiteralType
		{
			/**
			 * {@link GeneralCAOSTokenMeaningLiteral#getLiteralValue()} is a {@link String} :3
			 */
			LiteralString,
			
			
			/**
			 * {@link GeneralCAOSTokenMeaningLiteral#getLiteralValue()} is a {@link Long} :3
			 */
			LiteralChar,
			
			
			/**
			 * {@link GeneralCAOSTokenMeaningLiteral#getLiteralValue()} is a {@link Long} :3
			 */
			LiteralIntegerBase10,
			
			
			/**
			 * {@link GeneralCAOSTokenMeaningLiteral#getLiteralValue()} is a {@link Long} :3
			 */
			LiteralIntegerBase2,
			
			
			/**
			 * {@link GeneralCAOSTokenMeaningLiteral#getLiteralValue()} is a {@link Double} :3
			 */
			LiteralFloat,
			
			
			/**
			 * {@link GeneralCAOSTokenMeaningLiteral#getLiteralValue()} is a {@link ImmutableByteArrayList}, which is just like {@link String} (is immutable and caches its {@link Object#hashCode() hashcode}) but for bytes not chars :3
			 */
			LiteralByteString,
		}
		
		
		
		
		protected final GeneralCAOSTokenMeaningLiteralType literalType;
		protected final Object literalValue;
		
		public GeneralCAOSTokenMeaningLiteral(GeneralCAOSTokenMeaningLiteralType literalType, Object literalValue)
		{
			this.literalType = literalType;
			this.literalValue = literalValue;
		}
		
		public GeneralCAOSTokenMeaningLiteralType getLiteralType()
		{
			return literalType;
		}
		
		public Object getLiteralValue()
		{
			return literalValue;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((literalType == null) ? 0 : literalType.hashCode());
			result = prime * result + ((literalValue == null) ? 0 : literalValue.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GeneralCAOSTokenMeaningLiteral other = (GeneralCAOSTokenMeaningLiteral) obj;
			if (literalType != other.literalType)
				return false;
			if (literalValue == null)
			{
				if (other.literalValue != null)
					return false;
			}
			else if (!literalValue.equals(other.literalValue))
				return false;
			return true;
		}
		
		@Override
		public String toString()
		{
			return "GeneralCAOSTokenMeaningLiteral [literalType=" + literalType + ", literalValue=" + literalValue + "]";
		}
	}
}
