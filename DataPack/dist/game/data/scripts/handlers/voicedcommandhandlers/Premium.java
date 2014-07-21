package handlers.voicedcommandhandlers;

import java.util.Date;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.datatables.PremiumTable;
import pk.elfo.gameserver.handler.BypassHandler;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.instancemanager.ExpirableServicesManager;
import pk.elfo.gameserver.instancemanager.ExpirableServicesManager.ServiceType;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.ItemHolder;
import pk.elfo.gameserver.network.DialogId;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ConfirmDlg;
import pk.elfo.gameserver.util.Util;

/**
 * PkElfo
 */

public class Premium implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"mypremium",
		"addpremium"
	};
	
	/**
	 * 
	 * @see pk.elfo.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String, pk.elfo.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
	 */
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("mypremium"))
   	{		
		  if (activeChar.hasPremium())
		  {
		    if (ExpirableServicesManager.getInstance().isServiceUnlimited(ServiceType.PREMIUM, activeChar))
		    {
		    	activeChar.sendMessage("Voce tem servico premium ilimitado");
				}
				else if (ExpirableServicesManager.getInstance().isServiceTemporary(ServiceType.PREMIUM, activeChar))
				{
					activeChar.sendMessage("Seu servico premium estara ativo ate o logout");
				}
				else
				{
					activeChar.sendMessage("O Servico Premium ficara ativo ate " + Util.formatDate(new Date(ExpirableServicesManager.getInstance().getExpirationDate(ServiceType.PREMIUM, activeChar)), "dd.MM.yyyy HH:mm"));
				}
		  }
		  else
		  {
		    activeChar.sendMessage("O Servico premium nao esta ativo");
		  }
    }
		
		else if (command.startsWith("addpremium"))
		{
			if (!Config.PREMIUM_ALLOW_VOICED)
			{
				activeChar.sendMessage("O comando esta proibido");
			}
			else
			{
				IBypassHandler handler = BypassHandler.getInstance().getHandler("premiumShowList");
				handler.useBypass("premiumShowList", activeChar, null);
			}
		} 	
		return true;
	}
	
	/**
	 * 
	 * @see pk.elfo.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}