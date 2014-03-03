package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Attackable.RewardItem;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSweeper;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */
 
public class Sweep implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.SWEEP
	};
	private static final int maxSweepTime = 15000;
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		final L2PcInstance player = activeChar.getActingPlayer();
		
		RewardItem[] items = null;
		L2Attackable target;
		L2SkillSweeper sweep;
		SystemMessage sm;
		boolean canSweep;
		boolean isSweeping;
		for (L2Object tgt : targets)
		{
			if (!tgt.isL2Attackable())
			{
				continue;
			}
			target = (L2Attackable) tgt;
			canSweep = target.checkSpoilOwner(player, true);
			canSweep &= target.checkCorpseTime(player, maxSweepTime, true);
			canSweep &= player.getInventory().checkInventorySlotsAndWeight(target.getSpoilLootItems(), true, false);
			
			if (canSweep)
			{
				isSweeping = false;
				synchronized (target)
				{
					if (target.isSweepActive())
					{
						items = target.takeSweep();
						isSweeping = true;
					}
				}
				if (isSweeping)
				{
					if ((items == null) || (items.length == 0))
					{
						continue;
					}
					for (RewardItem ritem : items)
					{
						if (player.isInParty())
						{
							player.getParty().distributeItem(player, ritem, true, target);
						}
						else
						{
							player.addItem("Sweep", ritem.getItemId(), ritem.getCount(), player, true);
						}
					}
				}
			}
			target.endDecayTask();
			
			sweep = (L2SkillSweeper) skill;
			if (sweep.getAbsorbAbs() != -1)
			{
				int restored = 0;
				double absorb = 0;
				final StatusUpdate su = new StatusUpdate(activeChar);
				final int abs = sweep.getAbsorbAbs();
				if (sweep.isAbsorbHp())
				{
					absorb = ((activeChar.getCurrentHp() + abs) > activeChar.getMaxHp() ? activeChar.getMaxHp() : (activeChar.getCurrentHp() + abs));
					restored = (int) (absorb - activeChar.getCurrentHp());
					activeChar.setCurrentHp(absorb);
					
					su.addAttribute(StatusUpdate.CUR_HP, (int) absorb);
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED);
				}
				else
				{
					absorb = ((activeChar.getCurrentMp() + abs) > activeChar.getMaxMp() ? activeChar.getMaxMp() : (activeChar.getCurrentMp() + abs));
					restored = (int) (absorb - activeChar.getCurrentMp());
					activeChar.setCurrentMp(absorb);
					
					su.addAttribute(StatusUpdate.CUR_MP, (int) absorb);
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_MP_RESTORED);
				}
				activeChar.sendPacket(su);
				sm.addNumber(restored);
				activeChar.sendPacket(sm);
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}