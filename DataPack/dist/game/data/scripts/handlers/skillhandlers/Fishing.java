package handlers.skillhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.GeoData;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.instancemanager.ZoneManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.PcCondOverride;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.Inventory;
import pk.elfo.gameserver.model.items.L2Weapon;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.items.type.L2WeaponType;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.zone.L2ZoneType;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.model.zone.type.L2FishingZone;
import pk.elfo.gameserver.model.zone.type.L2WaterZone;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.InventoryUpdate;
import pk.elfo.gameserver.util.Util;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */
 
public class Fishing implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.FISHING
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		final L2PcInstance player = activeChar.getActingPlayer();
		
		/*
		 * If fishing is disabled, there isn't much point in doing anything else, unless you are GM. so this got moved up here, before anything else.
		 */
		if (!Config.ALLOWFISHING && !player.canOverrideCond(PcCondOverride.SKILL_CONDITIONS))
		{
			player.sendMessage("O servidor de pesca esta Offline");
			return;
		}
		if (player.isFishing())
		{
			if (player.getFishCombat() != null)
			{
				player.getFishCombat().doDie(false);
			}
			else
			{
				player.endFishing(false);
			}
			// Cancels fishing
			player.sendPacket(SystemMessageId.FISHING_ATTEMPT_CANCELLED);
			return;
		}
		L2Weapon weaponItem = player.getActiveWeaponItem();
		if (((weaponItem == null) || (weaponItem.getItemType() != L2WeaponType.FISHINGROD)))
		{
			// Fishing poles are not installed
			player.sendPacket(SystemMessageId.FISHING_POLE_NOT_EQUIPPED);
			return;
		}
		L2ItemInstance lure = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		if (lure == null)
		{
			// Bait not equiped.
			player.sendPacket(SystemMessageId.BAIT_ON_HOOK_BEFORE_FISHING);
			return;
		}
		player.setLure(lure);
		L2ItemInstance lure2 = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
		
		if ((lure2 == null) || (lure2.getCount() < 1)) // Not enough bait.
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_BAIT);
			return;
		}
		
		if (!player.isGM())
		{
			if (player.isInBoat())
			{
				// You can't fish while you are on boat
				player.sendPacket(SystemMessageId.CANNOT_FISH_ON_BOAT);
				return;
			}
			
			if (player.isInCraftMode() || player.isInStoreMode())
			{
				player.sendPacket(SystemMessageId.CANNOT_FISH_WHILE_USING_RECIPE_BOOK);
				return;
			}
			
			if (player.isInsideZone(ZoneId.WATER))
			{
				// You can't fish in water
				player.sendPacket(SystemMessageId.CANNOT_FISH_UNDER_WATER);
				return;
			}
			
			if (player.isInsideZone(ZoneId.PEACE))
			{
				// You can't fish here.
				player.sendPacket(SystemMessageId.CANNOT_FISH_HERE);
				return;
			}
		}
		
		/*
		 * If fishing is enabled, here is the code that was striped from startFishing() in L2PcInstance. Decide now where will the hook be cast...
		 */
		int rnd = Rnd.get(150) + 50;
		double angle = Util.convertHeadingToDegree(player.getHeading());
		double radian = Math.toRadians(angle);
		double sin = Math.sin(radian);
		double cos = Math.cos(radian);
		int x = player.getX() + (int) (cos * rnd);
		int y = player.getY() + (int) (sin * rnd);
		int z = player.getZ() + 50;
		/*
		 * ...and if the spot is in a fishing zone. If it is, it will then position the hook on the water surface. If not, you have to be GM to proceed past here... in that case, the hook will be positioned using the old Z lookup method.
		 */
		L2FishingZone aimingTo = null;
		L2WaterZone water = null;
		boolean canFish = false;
		for (L2ZoneType zone : ZoneManager.getInstance().getZones(x, y))
		{
			if (zone instanceof L2FishingZone)
			{
				aimingTo = (L2FishingZone) zone;
				continue;
			}
			if (zone instanceof L2WaterZone)
			{
				water = (L2WaterZone) zone;
			}
		}
		if (aimingTo != null)
		{
			// fishing zone found, we can fish here
			if (Config.GEODATA > 0)
			{
				// geodata enabled, checking if we can see end of the pole
				if (GeoData.getInstance().canSeeTarget(player.getX(), player.getY(), z, x, y, z))
				{
					// finding z level for hook
					if (water != null)
					{
						// water zone exist
						if (GeoData.getInstance().getHeight(x, y, z) < water.getWaterZ())
						{
							// water Z is higher than geo Z
							z = water.getWaterZ() + 10;
							canFish = true;
						}
					}
					else
					{
						// no water zone, using fishing zone
						if (GeoData.getInstance().getHeight(x, y, z) < aimingTo.getWaterZ())
						{
							// fishing Z is higher than geo Z
							z = aimingTo.getWaterZ() + 10;
							canFish = true;
						}
					}
				}
			}
			else
			{
				// geodata disabled
				// if water zone exist using it, if not - using fishing zone
				if (water != null)
				{
					z = water.getWaterZ() + 10;
				}
				else
				{
					z = aimingTo.getWaterZ() + 10;
				}
				canFish = true;
			}
		}
		if (!canFish)
		{
			// You can't fish here
			player.sendPacket(SystemMessageId.CANNOT_FISH_HERE);
			if (!player.isGM())
			{
				return;
			}
		}
		// Has enough bait, consume 1 and update inventory. Start fishing
		// follows.
		lure2 = player.getInventory().destroyItem("Consume", player.getInventory().getPaperdollObjectId(Inventory.PAPERDOLL_LHAND), 1, player, null);
		InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(lure2);
		player.sendPacket(iu);
		// If everything else checks out, actually cast the hook and start
		// fishing... :P
		player.startFishing(x, y, z);
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}