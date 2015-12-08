package pk.elfo.gameserver.handler;

import javolution.util.FastMap;

/**
 * PkElfo
 */

public class AIOItemHandler
{
	private static FastMap<String, IAIOItemHandler> _aioItemHandlers;
	
	public AIOItemHandler()
	{
		if (_aioItemHandlers == null)
		{
			_aioItemHandlers = new FastMap<>();
		}
	}
	
	public static AIOItemHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void registerHandler(IAIOItemHandler handler)
	{
		String handlerBypass = handler.getBypass();
		_aioItemHandlers.put(handlerBypass, handler);
	}
	
	public IAIOItemHandler getAIOHandler(String bypass)
	{
		return _aioItemHandlers.get(bypass);
	}
	
	public int size()
	{
		return _aioItemHandlers.size();
	}
	
	private static final class SingletonHolder
	{
		static final AIOItemHandler _instance = new AIOItemHandler();
	}
}