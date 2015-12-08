package pk.elfo.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.model.punishment.PunishmentType;

public class PunishmentHandler implements IHandler<IPunishmentHandler, PunishmentType>
{
	private final Map<PunishmentType, IPunishmentHandler> _handlers = new HashMap<>();
	
	protected PunishmentHandler()
	{
	}
	
	@Override
	public void registerHandler(IPunishmentHandler handler)
	{
		_handlers.put(handler.getType(), handler);
	}
	
	@Override
	public synchronized void removeHandler(IPunishmentHandler handler)
	{
		_handlers.remove(handler.getType());
	}
	
	@Override
	public IPunishmentHandler getHandler(PunishmentType val)
	{
		return _handlers.get(val);
	}
	
	@Override
	public int size()
	{
		return _handlers.size();
	}
	
	public static PunishmentHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final PunishmentHandler _instance = new PunishmentHandler();
	}
}