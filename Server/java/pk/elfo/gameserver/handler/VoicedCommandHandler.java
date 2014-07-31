package pk.elfo.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.masteriopack.rankpvpsystem.RPSConfig;
import pk.elfo.gameserver.masteriopack.rankpvpsystem.VoicedCommandHandlerPvpInfo;

public class VoicedCommandHandler implements IHandler<IVoicedCommandHandler, String>
{
	private final Map<String, IVoicedCommandHandler> _datatable;
	
	protected VoicedCommandHandler()
	{
		_datatable = new HashMap<>();
		if (RPSConfig.RANK_PVP_SYSTEM_ENABLED && RPSConfig.PVP_INFO_COMMAND_ENABLED && RPSConfig.RANK_PVP_SYSTEM_ENABLED && !RPSConfig.PVP_INFO_USER_COMMAND_ENABLED)
		{
			registerHandler(new VoicedCommandHandlerPvpInfo());
		}
	}
	
	@Override
	public void registerHandler(IVoicedCommandHandler handler)
	{
		String[] ids = handler.getVoicedCommandList();
		for (String id : ids)
		{
			_datatable.put(id, handler);
		}
	}
	
	@Override
	public synchronized void removeHandler(IVoicedCommandHandler handler)
	{
		String[] ids = handler.getVoicedCommandList();
		for (String id : ids)
		{
			_datatable.remove(id);
		}
	}
	
	@Override
	public IVoicedCommandHandler getHandler(String voicedCommand)
	{
		String command = voicedCommand;
		if (voicedCommand.contains(" "))
		{
			command = voicedCommand.substring(0, voicedCommand.indexOf(" "));
		}
		return _datatable.get(command);
	}
	
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	public static VoicedCommandHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final VoicedCommandHandler _instance = new VoicedCommandHandler();
	}
}