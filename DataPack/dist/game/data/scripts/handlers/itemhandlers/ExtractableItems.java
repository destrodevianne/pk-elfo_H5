package handlers.itemhandlers;

import java.util.List;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.L2ExtractableProduct;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.L2EtcItem;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */
 
public class ExtractableItems implements IItemHandler
{
	private static Logger _log = Logger.getLogger(ItemTable.class.getName());
	
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		final L2EtcItem etcitem = (L2EtcItem) item.getItem();
		final List<L2ExtractableProduct> exitem = etcitem.getExtractableItems();
		if (exitem == null)
		{
			_log.info("Nao ha dados extraiveis definidos para " + etcitem);
			return false;
		}
		
		// destroy item
		if (!activeChar.destroyItem("Extract", item.getObjectId(), 1, activeChar, true))
		{
			return false;
		}
		
		boolean created = false;
		// calculate extraction
		int min;
		int max;
		int createitemAmount;
		for (L2ExtractableProduct expi : exitem)
		{
			if (Rnd.get(100000) <= expi.getChance())
			{
				min = (int) (expi.getMin() * Config.RATE_EXTRACTABLE);
				max = (int) (expi.getMax() * Config.RATE_EXTRACTABLE);
				createitemAmount = (max == min) ? min : (Rnd.get((max - min) + 1) + min);
				// activeChar.addItem("Extract", expi.getId(), createitemAmount, activeChar, true);
    			if ((createitemAmount > 1) && !ItemTable.getInstance().getTemplate(expi.getId()).isStackable())
    				{
						while (createitemAmount > 0)
						{
							activeChar.addItem("Extract", expi.getId(), 1, activeChar, true);
							createitemAmount--;
						}
					}
					else
					{
						activeChar.addItem("Extract", expi.getId(), createitemAmount, activeChar, true);
				}
				created = true;
			}
		}
		
		if (!created)
		{
			activeChar.sendPacket(SystemMessageId.NOTHING_INSIDE_THAT);
		}
		return true;
	}
}