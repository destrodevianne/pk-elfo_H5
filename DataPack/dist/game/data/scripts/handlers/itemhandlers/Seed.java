package handlers.itemhandlers;

import pk.elfo.gameserver.datatables.ManorData;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.instancemanager.CastleManorManager;
import pk.elfo.gameserver.instancemanager.MapRegionManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2ChestInstance;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;

/**
 * PkElfo
 */
 
public class Seed implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		if (CastleManorManager.getInstance().isDisabled())
		{
			return false;
		}
		
		final L2Object tgt = playable.getTarget();
		if (!(tgt instanceof L2Npc))
		{
			playable.sendPacket(SystemMessageId.INCORRECT_TARGET);
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		if (!(tgt instanceof L2MonsterInstance) || (tgt instanceof L2ChestInstance) || ((L2Character) tgt).isRaid())
		{
			playable.sendPacket(SystemMessageId.THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING);
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		final L2MonsterInstance target = (L2MonsterInstance) tgt;
		if (target.isDead())
		{
			playable.sendPacket(SystemMessageId.INCORRECT_TARGET);
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if (target.isSeeded())
		{
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		final int seedId = item.getItemId();
		if (!areaValid(seedId, MapRegionManager.getInstance().getAreaCastle(playable)))
		{
			playable.sendPacket(SystemMessageId.THIS_SEED_MAY_NOT_BE_SOWN_HERE);
			return false;
		}
		
		target.setSeeded(seedId, (L2PcInstance) playable);
		final SkillHolder[] skills = item.getItem().getSkills();
		final L2PcInstance activeChar = playable.getActingPlayer();
		if (skills != null)
		{
			for (SkillHolder sk : skills)
			{
				activeChar.useMagic(sk.getSkill(), false, false);
			}
		}
		return true;
	}
	
	/**
	 * @param seedId
	 * @param castleId
	 * @return
	 */
	private boolean areaValid(int seedId, int castleId)
	{
		return (ManorData.getInstance().getCastleIdForSeed(seedId) == castleId);
	}
}