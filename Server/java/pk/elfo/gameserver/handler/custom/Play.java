package pk.elfo.gameserver.handler.custom;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.PlaySound;

/**
 * Projeto PkElfo
 */

public class Play implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = 
	{
		"play1", "play2", "play3", "play4", "play5"
	};
 
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("play1"))
		{
			PlaySound _snd = new PlaySound(1, "musica1", 0, 0, 0, 0, 0);
			activeChar.sendPacket(_snd);
			activeChar.sendMessage("Interprete 1 - Audio 1");
			activeChar.broadcastUserInfo();
		}
		else if (command.equalsIgnoreCase("play2"))
		{
			PlaySound _snd = new PlaySound(1, "musica2", 0, 0, 0, 0, 0);
			activeChar.sendPacket(_snd);
			activeChar.sendMessage("Interprete 2 - Audio 2");
			activeChar.broadcastUserInfo();
		}
		else if (command.equalsIgnoreCase("play3"))
		{
			PlaySound _snd = new PlaySound(1, "musica3", 0, 0, 0, 0, 0);
			activeChar.sendPacket(_snd);
			activeChar.sendMessage("Interprete 3 - Audio 3");
			activeChar.broadcastUserInfo();
		}
		else if (command.equalsIgnoreCase("play4"))
		{
			PlaySound _snd = new PlaySound(1, "musica4", 0, 0, 0, 0, 0);
			activeChar.sendPacket(_snd);
			activeChar.sendMessage("Interprete 4 - Audio 4");
			activeChar.broadcastUserInfo();
		}
		else if (command.equalsIgnoreCase("play5"))
		{
			PlaySound _snd = new PlaySound(1, "musica5", 0, 0, 0, 0, 0);
			activeChar.sendPacket(_snd);
			activeChar.sendMessage("Interprete 5 - Audio 5");
			activeChar.broadcastUserInfo();
		}
		return false;
	}
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}