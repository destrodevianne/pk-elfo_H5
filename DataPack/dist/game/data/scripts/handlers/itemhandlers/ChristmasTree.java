package handlers.itemhandlers;

import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * PkElfo
 */
 
public class ChristmasTree implements IItemHandler
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
		L2NpcTemplate template1 = null;
		
		switch (item.getItemId())
		{
			case 5560:
				template1 = NpcTable.getInstance().getTemplate(13006);
				break;
			case 5561:
				template1 = NpcTable.getInstance().getTemplate(13007);
				break;
		}
		
		if (template1 == null)
		{
			return false;
		}
		
		L2Object target = activeChar.getTarget();
		if (target == null)
		{
			target = activeChar;
		}
		
		try
		{
			L2Spawn spawn = new L2Spawn(template1);
			spawn.setLocx(target.getX());
			spawn.setLocy(target.getY());
			spawn.setLocz(target.getZ());
			spawn.setInstanceId(activeChar.getInstanceId());
			L2Npc npc = spawn.spawnOne(false);
			npc.setSummoner(activeChar);
			activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);
			activeChar.sendMessage("Criado " + template1.getName() + " em x: " + spawn.getLocx() + " y: " + spawn.getLocy() + " z: " + spawn.getLocz());
			return true;
		}
		catch (Exception e)
		{
			activeChar.sendPacket(SystemMessageId.TARGET_CANT_FOUND);
			return false;
		}
	}
}