package handlers.voicedcommandhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Projeto PkElfo
 */

public class Banking implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"bank",
		"withdraw",
		"deposit"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (command.equals("bank"))
		{
			activeChar.sendMessage(".deposit (" + Config.BANKING_SYSTEM_ADENA + " Adena = " + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar) / .withdraw (" + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar = " + Config.BANKING_SYSTEM_ADENA + " Adena)");
		}
		else if (command.equals("deposit") && activeChar.isVip())
		{
			if (activeChar.getInventory().getInventoryItemCount(57, 0) >= Config.BANKING_SYSTEM_ADENA)
			{
				if (!activeChar.reduceAdena("Goldbar", Config.BANKING_SYSTEM_ADENA, activeChar, false))
				{
					return false;
				}
				activeChar.getInventory().addItem("Goldbar", 3470, Config.BANKING_SYSTEM_GOLDBARS, activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendMessage("Obrigado, agora voce tem " + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar(s), e menos " + Config.BANKING_SYSTEM_ADENA + " de adena.");
			}
			else
			{
				activeChar.sendMessage("Voce nao tem Adena suficiente para converter em Goldbar(s), voce precisa de " + Config.BANKING_SYSTEM_ADENA + " Adena.");
			}
		}
		else if (command.equals("withdraw") && activeChar.isVip())
		{
			if (activeChar.getInventory().getInventoryItemCount(3470, 0) >= Config.BANKING_SYSTEM_GOLDBARS)
			{
				if (!activeChar.destroyItemByItemId("Adena", 3470, Config.BANKING_SYSTEM_GOLDBARS, activeChar, false))
				{
					return false;
				}
				activeChar.getInventory().addAdena("Adena", Config.BANKING_SYSTEM_ADENA, activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendMessage("Obrigado, agora voce tem " + Config.BANKING_SYSTEM_ADENA + " Adena, e menos " + Config.BANKING_SYSTEM_GOLDBARS + " de Goldbar(s).");
			}
			else
			{
				activeChar.sendMessage("Voce nao tem nenhum Goldbars para se transformar em " + Config.BANKING_SYSTEM_ADENA + " Adena.");
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}