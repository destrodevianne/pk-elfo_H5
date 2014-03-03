package handlers.itemhandlers;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.instancemanager.TerritoryWarManager;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * PkElfo
 */
 
public class Disguise implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		L2PcInstance activeChar = playable.getActingPlayer();
		
		int regId = TerritoryWarManager.getInstance().getRegisteredTerritoryId(activeChar);
		if ((regId > 0) && (regId == (item.getItemId() - 13596)))
		{
			if ((activeChar.getClan() != null) && (activeChar.getClan().getCastleId() > 0))
			{
				activeChar.sendPacket(SystemMessageId.TERRITORY_OWNING_CLAN_CANNOT_USE_DISGUISE_SCROLL);
				return false;
			}
			TerritoryWarManager.getInstance().addDisguisedPlayer(activeChar.getObjectId());
			activeChar.broadcastUserInfo();
			playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
			return true;
		}
		else if (regId > 0)
		{
			activeChar.sendPacket(SystemMessageId.THE_DISGUISE_SCROLL_MEANT_FOR_DIFFERENT_TERRITORY);
			return false;
		}
		else
		{
			activeChar.sendPacket(SystemMessageId.TERRITORY_WAR_SCROLL_CAN_NOT_USED_NOW);
			return false;
		}
	}
}