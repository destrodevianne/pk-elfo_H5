package handlers.itemhandlers;

import pk.elfo.gameserver.datatables.DoorTable;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;

/**
 * PkElfo
 */
 
public class PaganKeys implements IItemHandler
{
	public static final int INTERACTION_DISTANCE = 100;
	
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final int itemId = item.getItemId();
		final L2PcInstance activeChar = (L2PcInstance) playable;
		final L2Object target = activeChar.getTarget();
		
		if (!(target instanceof L2DoorInstance))
		{
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		L2DoorInstance door = (L2DoorInstance) target;
		
		if (!(activeChar.isInsideRadius(door, INTERACTION_DISTANCE, false, false)))
		{
			activeChar.sendMessage("Muito longe.");
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		if ((activeChar.getAbnormalEffect() > 0) || activeChar.isInCombat())
		{
			activeChar.sendMessage("Voce nao pode usar a chave agora.");
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if (!playable.destroyItem("Consume", item.getObjectId(), 1, null, false))
		{
			return false;
		}
		
		// TODO: Unhardcode these!
		switch (itemId)
		{
			case 9698:
				if (door.getDoorId() == 24220020)
				{
					if (activeChar.getInstanceId() != door.getInstanceId())
					{
						final L2DoorInstance instanceDoor = InstanceManager.getInstance().getInstance(activeChar.getInstanceId()).getDoor(door.getDoorId());
						if (instanceDoor != null)
						{
							instanceDoor.openMe();
						}
					}
					else
					{
						door.openMe();
					}
				}
				else
				{
					activeChar.sendMessage("Porta incorreta.");
				}
				break;
			case 9699:
				if (door.getDoorId() == 24220022)
				{
					if (activeChar.getInstanceId() != door.getInstanceId())
					{
						final L2DoorInstance instanceDoor = InstanceManager.getInstance().getInstance(activeChar.getInstanceId()).getDoor(door.getDoorId());
						if (instanceDoor != null)
						{
							instanceDoor.openMe();
						}
					}
					else
					{
						door.openMe();
					}
				}
				else
				{
					activeChar.sendMessage("Porta incorreta.");
				}
				break;
			case 8056:
				if ((door.getDoorId() == 23150004) || (door.getDoorId() == 23150003))
				{
					DoorTable.getInstance().getDoor(23150003).openMe();
					DoorTable.getInstance().getDoor(23150004).openMe();
				}
				else
				{
					activeChar.sendMessage("Porta incorreta.");
				}
				break;
		}
		return true;
	}
}