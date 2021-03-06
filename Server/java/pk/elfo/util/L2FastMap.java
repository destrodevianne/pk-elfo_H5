package pk.elfo.util;

import java.util.Map;

import pk.elfo.gameserver.model.interfaces.IL2EntryProcedure;
import pk.elfo.gameserver.model.interfaces.IL2Procedure;
import javolution.util.FastMap;

/**
 * A custom version of FastMap with extension for iterating without using temporary collection<br>
 * It's provide synchronization lock when iterating if needed<br>
 * @version 1.0.1 (2008-02-07)<br>
 *          Changes:<br>
 *          1.0.0 - Initial version.<br>
 *          1.0.1 - Made forEachP() final.<br>
 * @version 1.0.2 (2012-08-19)<br>
 *          1.0.2 - Using IL2Procedure instead of I2ForEachKey/Value<br>
 * @param <K>
 * @param <V>
 */
 
public class L2FastMap<K, V> extends FastMap<K, V>
{
	private static final long serialVersionUID = 8503855490858805336L;
	
	public L2FastMap()
	{
		this(false);
	}
	
	public L2FastMap(Map<? extends K, ? extends V> map)
	{
		this(map, false);
	}
	
	public L2FastMap(int initialCapacity)
	{
		this(initialCapacity, false);
	}
	
	public L2FastMap(boolean shared)
	{
		super();
		if (shared)
		{
			shared();
		}
	}
	
	public L2FastMap(Map<? extends K, ? extends V> map, boolean shared)
	{
		super(map);
		if (shared)
		{
			shared();
		}
	}
	
	public L2FastMap(int initialCapacity, boolean shared)
	{
		super(initialCapacity);
		if (shared)
		{
			shared();
		}
	}
	
	/**
	 * Public method that iterate entire collection.<br>
	 * <br>
	 * @param proc - a class method that must be executed on every element of collection.<br>
	 * @return - returns true if entire collection is iterated, false if it`s been interrupted by<br>
	 *         check method (IL2EntryProcedure.execute())<br>
	 */
	public boolean executeForEachEntry(IL2EntryProcedure<K, V> proc)
	{
		for (Map.Entry<K, V> e : entrySet())
		{
			if (!proc.execute(e.getKey(), e.getValue()))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean executeForEachKey(IL2Procedure<K> proc)
	{
		for (K k : keySet())
		{
			if (!proc.execute(k))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean executeForEachValue(IL2Procedure<V> proc)
	{
		for (V v : values())
		{
			if (!proc.execute(v))
			{
				return false;
			}
		}
		return true;
	}
}