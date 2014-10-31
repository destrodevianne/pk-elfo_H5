package pk.elfo.util;

import java.util.Collection;

import pk.elfo.gameserver.model.interfaces.IL2Procedure;
import javolution.util.FastList;

/**
 * A custom version of FastList with extension for iterating without using temporary collection<br>
 * It's provide synchronization lock when iterating if needed<br>
 * <br>
 * @version 1.0.1 (2008-02-07)<br>
 *          1.0.0 - Initial version.<br>
 *          1.0.1 - Made forEachP() final.<br>
 * @version 1.0.2 (20012-08-19)<br>
 *          1.0.2 - Using IL2Procedure instead of IForEach.
 * @param <T>
 */
 
public class L2FastList<T> extends FastList<T>
{
	private static final long serialVersionUID = 8354641653178203420L;
	
	public L2FastList()
	{
		this(false);
	}
	
	public L2FastList(int initialCapacity)
	{
		this(initialCapacity, false);
	}
	
	public L2FastList(Collection<? extends T> c)
	{
		this(c, false);
	}
	
	public L2FastList(boolean shared)
	{
		super();
		if (shared)
		{
			shared();
		}
	}
	
	public L2FastList(int initialCapacity, boolean shared)
	{
		super(initialCapacity);
		if (shared)
		{
			shared();
		}
	}
	
	public L2FastList(Collection<? extends T> c, boolean shared)
	{
		super(c);
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
	 * check method (IL2Procedure.execute(T))<br>
	 */
	public boolean executeForEach(IL2Procedure<T> proc)
	{
		for (T e : this)
		{
			if (!proc.execute(e))
			{
				return false;
			}
		}
		return true;
	}
}