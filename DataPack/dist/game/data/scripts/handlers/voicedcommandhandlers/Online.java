package handlers.voicedcommandhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class Online implements IVoicedCommandHandler
{
    private static String[] _voicedCommands =
    {
        "online"
    };

    @Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
		if(Config.CMD_FAKE)
		{
			activeChar.sendMessage("=======<Jogadores Online>======");
			activeChar.sendMessage("Jogadores online: " + (Config.FAKE_PLAYERS + L2World.getInstance().getAllPlayers().size()));
			activeChar.sendMessage("===============================");
		}
		else
		{
			activeChar.sendMessage("=======<Jogadores Online>======");
			activeChar.sendMessage("Jogadores online: " + L2World.getInstance().getAllPlayers().size());
			activeChar.sendMessage("===============================");
		}
        return true;
    }

    @Override
	public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
}