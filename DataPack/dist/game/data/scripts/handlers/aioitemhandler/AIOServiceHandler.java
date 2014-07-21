package handlers.aioitemhandler;

import java.util.List;
import java.util.logging.Logger;

import pk.elfo.gameserver.datatables.HennaData;
import pk.elfo.gameserver.handler.IAIOItemHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.L2Henna;
import pk.elfo.gameserver.network.serverpackets.ExShowVariationCancelWindow;
import pk.elfo.gameserver.network.serverpackets.ExShowVariationMakeWindow;
import pk.elfo.gameserver.network.serverpackets.HennaEquipList;
import pk.elfo.gameserver.network.serverpackets.HennaRemoveList;

/**
 * PkElfo
 */

public class AIOServiceHandler implements IAIOItemHandler
{
	private static final Logger _log = Logger.getLogger(AIOServiceHandler.class.getName());
	private static final String BYPASS = "services";
	
	@Override
	public String getBypass()
	{
		return BYPASS;
	}
	
	@Override
	public void onBypassUse(L2PcInstance player, String command)
	{
		String[] subCommands = command.split(" ");
		if (subCommands.length < 2)
		{
			_log.warning("AIOServiceHandler: Wrong bypass: " + command);
		}
		
		String actualCmd = subCommands[0];
		String secondCmd = subCommands[1];
		
		if ((secondCmd == null) || secondCmd.isEmpty())
		{
			_log.severe("Wrong sub-bypass for the AIO Item at: " + command);
			return;
		}
		
		/*
		 * Augment
		 */
		if (actualCmd.equalsIgnoreCase("augment"))
		{
			/*
			 * Add an augmentation
			 */
			if (secondCmd.equalsIgnoreCase("add"))
			{
				player.sendPacket(ExShowVariationMakeWindow.STATIC_PACKET);
			}
			/*
			 * Remove an agumentation
			 */
			else if (secondCmd.equalsIgnoreCase("erase"))
			{
				player.sendPacket(ExShowVariationCancelWindow.STATIC_PACKET);
			}
			/*
			 * Wrong bypass
			 */
			else
			{
				_log.severe("Wrong tag for Aioitem_services_augment_: " + secondCmd);
				return;
			}
		}
		/*
		 * Henna draw & erase
		 */
		else if (actualCmd.equalsIgnoreCase("henna"))
		{
			/*
			 * Draw a symbol
			 */
			if (secondCmd.equalsIgnoreCase("add"))
			{
				List<L2Henna> tato = HennaData.getInstance().getHennaList(player.getClassId());
				player.sendPacket(new HennaEquipList(player, tato));
			}
			/*
			 * Erase a symbol
			 */
			else if (secondCmd.equalsIgnoreCase("erase"))
			{
				boolean hasHennas = false;
				
				for (int i = 1; i <= 3; i++)
				{
					L2Henna henna = player.getHenna(i);
					
					if (henna != null)
					{
						hasHennas = true;
					}
				}
				
				if (hasHennas)
				{
					player.sendPacket(new HennaRemoveList(player));
				}
			}
			/*
			 * Wrong bypass
			 */
			else
			{
				_log.severe("Wrong tag for Aioitem_henna_ : " + secondCmd);
				return;
			}
		}
		/*
		 * Wrong bypass
		 */
		else
		{
			_log.severe("Wrong bypass for the AIOItem services tag: " + command);
			return;
		}
	}
}