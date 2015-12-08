package pk.elfo.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.masteriopack.rankpvpsystem.RPSConfig;
import pk.elfo.gameserver.masteriopack.rankpvpsystem.UserCommandHandlerPvpInfo;

public class UserCommandHandler implements IHandler<IUserCommandHandler, Integer>
{
	private final Map<Integer, IUserCommandHandler> _datatable;
	
	protected UserCommandHandler()
	{
		_datatable = new HashMap<>();
		if (RPSConfig.RANK_PVP_SYSTEM_ENABLED && RPSConfig.PVP_INFO_USER_COMMAND_ENABLED && RPSConfig.PVP_INFO_COMMAND_ENABLED)
		{
			registerHandler(new UserCommandHandlerPvpInfo());
		}
	}
	
	@Override
	public void registerHandler(IUserCommandHandler handler)
	{
		int[] ids = handler.getUserCommandList();
		for (int id : ids)
		{
			_datatable.put(id, handler);
		}
	}
	
	@Override
	public synchronized void removeHandler(IUserCommandHandler handler)
	{
		int[] ids = handler.getUserCommandList();
		for (int id : ids)
		{
			_datatable.remove(id);
		}
	}
	
	@Override
	public IUserCommandHandler getHandler(Integer userCommand)
	{
		return _datatable.get(userCommand);
	}
	
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	public static UserCommandHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final UserCommandHandler _instance = new UserCommandHandler();
	}
}