package rebound.apps.caoticdreams.caos.library.content;

import static rebound.util.collections.CollectionUtilities.*;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rebound.exceptions.NoSuchMappingReturnPath;
import rebound.util.collections.CollectionUtilities;

public class CDApiUtilities
{
	public static @Nullable Object getNamedVariable(@Nonnull Map<Object, Object> namedVariableBank, @Nullable Object name)
	{
		try
		{
			return getrp(namedVariableBank, name);
		}
		catch (NoSuchMappingReturnPath rp)
		{
			return 0;
		}
	}
	
	public static void setNamedVariable(@Nonnull Map<Object, Object> namedVariableBank, @Nullable Object name, @Nullable Object value)
	{
		namedVariableBank.put(name, value);
	}
	
	
	
	public static @Nullable Object getIndexedVariable(@Nonnull List<Object> namedVariableBank, @Nonnegative int index)
	{
		if (index >= namedVariableBank.size())
			return 0;
		else
			return namedVariableBank.get(index);
	}
	
	public static void setIndexedVariable(@Nonnull List<Object> namedVariableBank, @Nonnegative int index, @Nullable Object value)
	{
		CollectionUtilities.setInListGrowingIfNecessary(namedVariableBank, index, value, 0);
	}
}
