package pk.elfo.gameserver.model.skills.l2skills;

import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.entity.TvTRoundEvent;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.SystemMessageId;

public class L2SkillMount extends L2Skill
{
	private final int _itemId;
	
	public L2SkillMount(StatsSet set)
	{
		super(set);
		_itemId = set.getInteger("itemId", 0);
	}
	
	@Override
	public void useSkill(L2Character caster, L2Object[] targets)
	{
		if (!caster.isPlayer())
		{
			return;
		}
		
		if (!TvTEvent.onItemSummon(caster.getObjectId()))
		{
			return;
		}
		
		if (!TvTRoundEvent.onItemSummon(caster.getObjectId()))
		{
			return;
		}
		
		L2PcInstance activePlayer = caster.getActingPlayer();
		
		if (!activePlayer.getFloodProtectors().getItemPetSummon().tryPerformAction("mount"))
		{
			return;
		}
		
		// Dismount Action
		if (getNpcId() == 0)
		{
			activePlayer.dismount();
			return;
		}
		
		if (activePlayer.isSitting())
		{
			activePlayer.sendPacket(SystemMessageId.CANT_MOVE_SITTING);
			return;
		}
		
		if (activePlayer.inObserverMode())
		{
			return;
		}
		
		if (activePlayer.isInOlympiadMode())
		{
			activePlayer.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return;
		}
		
		if (activePlayer.hasSummon() || activePlayer.isMounted())
		{
			activePlayer.sendPacket(SystemMessageId.YOU_ALREADY_HAVE_A_PET);
			return;
		}
		
		if (activePlayer.isAttackingNow())
		{
			activePlayer.sendPacket(SystemMessageId.YOU_CANNOT_SUMMON_IN_COMBAT);
			return;
		}
		
		if (activePlayer.isCursedWeaponEquipped())
		{
			return;
		}
		
		activePlayer.mount(getNpcId(), _itemId, false);
	}
}
