package pk.elfo.util;

import java.util.ArrayList;
import java.util.Collection;

import pk.elfo.gameserver.model.interfaces.IL2Procedure;

/**
 * A custom version of ArrayList: Extension for iterating without using temporary collection<br>
 * Note that this implementation is not synchronized. If multiple threads access a array list concurrently, and at least one of the threads modifies the list structurally, it must be synchronized externally. This is typically accomplished by synchronizing on some object that naturally encapsulates
 * the list. If no such object exists, the list should be "wrapped" using the {@link L2FastList}. This is best done at creation time, to prevent accidental unsynchronized access.
 * @param <T>
 */
public class L2ArrayList<T> extends ArrayList<T>
{
	private static final long serialVersionUID = 8354641653178203420L;
	
	public L2ArrayList()
	{
		super();
	}
	
	public L2ArrayList(Collection<? extends T> c)
	{
		super(c);
	}
	
	public L2ArrayList(int initialCapacity)
	{
		super(initialCapacity);
	}
	
	/**
	 * Public method that iterate entire collection.<br>
	 * <br>
	 * @param proc - a class method that must be executed on every element of collection.<br>
	 * @return - returns true if entire collection is iterated, false if it`s been interrupted by<br>
	 *         check method (IL2Procedure.execute(T))<br>
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
