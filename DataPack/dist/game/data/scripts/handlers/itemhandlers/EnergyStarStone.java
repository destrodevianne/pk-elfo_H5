package handlers.itemhandlers;

import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2AirShipInstance;
import pk.elfo.gameserver.model.actor.instance.L2ControllableAirShipInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class EnergyStarStone extends ItemSkills
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final L2AirShipInstance ship = playable.getActingPlayer().getAirShip();
		if ((ship == null) || !(ship instanceof L2ControllableAirShipInstance) || (ship.getFuel() >= ship.getMaxFuel()))
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addItemName(item);
			playable.sendPacket(sm);
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		return super.useItem(playable, item, forceUse);
	}
}