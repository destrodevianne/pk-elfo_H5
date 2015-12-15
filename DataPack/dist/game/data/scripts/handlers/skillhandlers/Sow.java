package handlers.skillhandlers;

import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.ManorData;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.quest.Quest.QuestSound;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */
 
public class Sow implements ISkillHandler
{
	private static Logger _log = Logger.getLogger(Sow.class.getName());
	
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.SOW
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		final L2Object[] targetList = skill.getTargetList(activeChar);
		if ((targetList == null) || (targetList.length == 0))
		{
			return;
		}
		
		if (Config.DEBUG)
		{
			_log.info("Casting sow");
		}
		
		L2MonsterInstance target;
		
		for (L2Object tgt : targetList)
		{
			if (!tgt.isMonster())
			{
				continue;
			}
			
			target = (L2MonsterInstance) tgt;
			if (target.isDead() || target.isSeeded() || (target.getSeederId() != activeChar.getObjectId()))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				continue;
			}
			
			final int seedId = target.getSeedType();
			if (seedId == 0)
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				continue;
			}
			
			// Consuming used seed
			if (!activeChar.destroyItemByItemId("Consume", seedId, 1, target, false))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			SystemMessage sm;
			if (calcSuccess(activeChar, target, seedId))
			{
				activeChar.sendPacket(QuestSound.ITEMSOUND_QUEST_ITEMGET.getPacket());
				target.setSeeded(activeChar.getActingPlayer());
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_SEED_WAS_SUCCESSFULLY_SOWN);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_SEED_WAS_NOT_SOWN);
			}
			
			if (activeChar.getParty() == null)
			{
				activeChar.sendPacket(sm);
			}
			else
			{
				activeChar.getParty().broadcastPacket(sm);
			}
			
			// TODO: Mob should not aggro on player, this way doesn't work really nice
			target.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		}
	}
	
	private boolean calcSuccess(L2Character activeChar, L2Character target, int seedId)
	{
		// TODO: check all the chances
		int basicSuccess = (ManorData.getInstance().isAlternative(seedId) ? 20 : 90);
		final int minlevelSeed = ManorData.getInstance().getSeedMinLevel(seedId);
		final int maxlevelSeed = ManorData.getInstance().getSeedMaxLevel(seedId);
		final int levelPlayer = activeChar.getLevel(); // Attacker Level
		final int levelTarget = target.getLevel(); // target Level
		
		// seed level
		if (levelTarget < minlevelSeed)
		{
			basicSuccess -= 5 * (minlevelSeed - levelTarget);
		}
		if (levelTarget > maxlevelSeed)
		{
			basicSuccess -= 5 * (levelTarget - maxlevelSeed);
		}
		
		// 5% decrease in chance if player level
		// is more than +/- 5 levels to _target's_ level
		int diff = (levelPlayer - levelTarget);
		if (diff < 0)
		{
			diff = -diff;
		}
		if (diff > 5)
		{
			basicSuccess -= 5 * (diff - 5);
		}
		
		// chance can't be less than 1%
		if (basicSuccess < 1)
		{
			basicSuccess = 1;
		}
		return Rnd.nextInt(99) < basicSuccess;
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}