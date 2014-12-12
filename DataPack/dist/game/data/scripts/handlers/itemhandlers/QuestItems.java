package handlers.itemhandlers;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.L2Item;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class QuestItems implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceuse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		L2PcInstance player = playable.getActingPlayer();
		
		if (!player.destroyItem("Item Handler - QuestItems", item, player, true))
		{
			return false;
		}
		
		L2Item itm = item.getItem();
		for (Quest quest : itm.getQuestEvents())
		{
			QuestState state = player.getQuestState(quest.getName());
			if ((state == null) || !state.isStarted())
			{
				continue;
			}
			
			quest.notifyItemUse(itm, player);
		}
		return true;
	}
}