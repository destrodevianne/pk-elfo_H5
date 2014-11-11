package pk.elfo.gameserver.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.datatables.SummonItemsData;
import pk.elfo.gameserver.model.L2SummonItem;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2PetInstance;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.InventoryUpdate;
import pk.elfo.gameserver.network.serverpackets.MagicSkillLaunched;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

public final class Evolve
{
	public static final Logger _log = Logger.getLogger(Evolve.class.getName());
	
	public static final boolean doEvolve(L2PcInstance player, L2Npc npc, int itemIdtake, int itemIdgive, int petminlvl)
	{
		if ((itemIdtake == 0) || (itemIdgive == 0) || (petminlvl == 0))
		{
			return false;
		}
		
		if (!player.hasSummon() || !player.getSummon().isPet())
		{
			return false;
		}
		
		final L2PetInstance currentPet = (L2PetInstance) player.getSummon();
		if (currentPet.isAlikeDead())
		{
			Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to use death pet exploit!", Config.DEFAULT_PUNISH);
			return false;
		}
		
		L2ItemInstance item = null;
		long petexp = currentPet.getStat().getExp();
		String oldname = currentPet.getName();
		int oldX = currentPet.getX();
		int oldY = currentPet.getY();
		int oldZ = currentPet.getZ();
		
		L2SummonItem olditem = SummonItemsData.getInstance().getSummonItem(itemIdtake);
		
		if (olditem == null)
		{
			return false;
		}
		
		int oldnpcID = olditem.getNpcId();
		
		if ((currentPet.getStat().getLevel() < petminlvl) || (currentPet.getNpcId() != oldnpcID))
		{
			return false;
		}
		
		L2SummonItem sitem = SummonItemsData.getInstance().getSummonItem(itemIdgive);
		
		if (sitem == null)
		{
			return false;
		}
		
		int npcID = sitem.getNpcId();
		
		if (npcID == 0)
		{
			return false;
		}
		
		L2NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(npcID);
		
		currentPet.unSummon(player);

		currentPet.destroyControlItem(player, true);
		
		item = player.getInventory().addItem("Evolve", itemIdgive, 1, player, npc);

		L2PetInstance petSummon = L2PetInstance.spawnPet(npcTemplate, player, item);
		
		if (petSummon == null)
		{
			return false;
		}

		long _minimumexp = petSummon.getStat().getExpForLevel(petminlvl);
		if (petexp < _minimumexp)
		{
			petexp = _minimumexp;
		}
		
		petSummon.getStat().addExp(petexp);
		petSummon.setCurrentHp(petSummon.getMaxHp());
		petSummon.setCurrentMp(petSummon.getMaxMp());
		petSummon.setCurrentFed(petSummon.getMaxFed());
		petSummon.setTitle(player.getName());
		petSummon.setName(oldname);
		petSummon.setRunning();
		petSummon.store();
		
		player.setPet(petSummon);
		
		player.sendPacket(new MagicSkillUse(npc, 2046, 1, 1000, 600000));
		player.sendPacket(SystemMessageId.SUMMON_A_PET);
		// L2World.getInstance().storeObject(petSummon);
		petSummon.spawnMe(oldX, oldY, oldZ);
		petSummon.startFeed();
		item.setEnchantLevel(petSummon.getLevel());
		
		ThreadPoolManager.getInstance().scheduleGeneral(new EvolveFinalizer(player, petSummon), 900);
		
		if (petSummon.getCurrentFed() <= 0)
		{
			ThreadPoolManager.getInstance().scheduleGeneral(new EvolveFeedWait(player, petSummon), 60000);
		}
		else
		{
			petSummon.startFeed();
		}
		return true;
	}
	
	public static final boolean doRestore(L2PcInstance player, L2Npc npc, int itemIdtake, int itemIdgive, int petminlvl)
	{
		if ((itemIdtake == 0) || (itemIdgive == 0) || (petminlvl == 0))
		{
			return false;
		}
		
		L2ItemInstance item = player.getInventory().getItemByItemId(itemIdtake);
		if (item == null)
		{
			return false;
		}
		
		int oldpetlvl = item.getEnchantLevel();
		if (oldpetlvl < petminlvl)
		{
			oldpetlvl = petminlvl;
		}
		
		L2SummonItem oldItem = SummonItemsData.getInstance().getSummonItem(itemIdtake);
		if (oldItem == null)
		{
			return false;
		}
		
		L2SummonItem sItem = SummonItemsData.getInstance().getSummonItem(itemIdgive);
		if (sItem == null)
		{
			return false;
		}
		
		int npcId = sItem.getNpcId();
		if (npcId == 0)
		{
			return false;
		}
		
		L2NpcTemplate npcTemplate = NpcTable.getInstance().getTemplate(npcId);

		L2ItemInstance removedItem = player.getInventory().destroyItem("PetRestore", item, player, npc);
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISAPPEARED);
		sm.addItemName(removedItem);
		player.sendPacket(sm);

		L2ItemInstance addedItem = player.getInventory().addItem("PetRestore", itemIdgive, 1, player, npc);

		L2PetInstance petSummon = L2PetInstance.spawnPet(npcTemplate, player, addedItem);
		if (petSummon == null)
		{
			return false;
		}
		
		long _maxexp = petSummon.getStat().getExpForLevel(oldpetlvl);
		
		petSummon.getStat().addExp(_maxexp);
		petSummon.setCurrentHp(petSummon.getMaxHp());
		petSummon.setCurrentMp(petSummon.getMaxMp());
		petSummon.setCurrentFed(petSummon.getMaxFed());
		petSummon.setTitle(player.getName());
		petSummon.setRunning();
		petSummon.store();
		
		player.setPet(petSummon);
		
		player.sendPacket(new MagicSkillUse(npc, 2046, 1, 1000, 600000));
		player.sendPacket(SystemMessageId.SUMMON_A_PET);
		// L2World.getInstance().storeObject(petSummon);
		petSummon.spawnMe(player.getX(), player.getY(), player.getZ());
		petSummon.startFeed();
		addedItem.setEnchantLevel(petSummon.getLevel());

		InventoryUpdate iu = new InventoryUpdate();
		iu.addRemovedItem(removedItem);
		player.sendPacket(iu);
		
		StatusUpdate su = new StatusUpdate(player);
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
		
		player.broadcastUserInfo();
		
		L2World world = L2World.getInstance();
		world.removeObject(removedItem);
		
		ThreadPoolManager.getInstance().scheduleGeneral(new EvolveFinalizer(player, petSummon), 900);
		
		if (petSummon.getCurrentFed() <= 0)
		{
			ThreadPoolManager.getInstance().scheduleGeneral(new EvolveFeedWait(player, petSummon), 60000);
		}
		else
		{
			petSummon.startFeed();
		}
		
		// pet control item no longer exists, delete the pet from the db
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM pets WHERE item_obj_id=?"))
		{
			ps.setInt(1, removedItem.getObjectId());
			ps.execute();
		}
		catch (Exception e)
		{
		}
		return true;
	}
	
	static final class EvolveFeedWait implements Runnable
	{
		private final L2PcInstance _activeChar;
		private final L2PetInstance _petSummon;
		
		EvolveFeedWait(L2PcInstance activeChar, L2PetInstance petSummon)
		{
			_activeChar = activeChar;
			_petSummon = petSummon;
		}
		
		@Override
		public void run()
		{
			try
			{
				if (_petSummon.getCurrentFed() <= 0)
				{
					_petSummon.unSummon(_activeChar);
				}
				else
				{
					_petSummon.startFeed();
				}
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "", e);
			}
		}
	}
	
	static final class EvolveFinalizer implements Runnable
	{
		private final L2PcInstance _activeChar;
		private final L2PetInstance _petSummon;
		
		EvolveFinalizer(L2PcInstance activeChar, L2PetInstance petSummon)
		{
			_activeChar = activeChar;
			_petSummon = petSummon;
		}
		
		@Override
		public void run()
		{
			try
			{
				_activeChar.sendPacket(new MagicSkillLaunched(_activeChar, 2046, 1));
				_petSummon.setFollowStatus(true);
				_petSummon.setShowSummonAnimation(false);
			}
			catch (Throwable e)
			{
				_log.log(Level.WARNING, "", e);
			}
		}
	}
}