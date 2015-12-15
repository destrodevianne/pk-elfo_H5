package handlers.skillhandlers;

import pk.elfo.gameserver.ai.CtrlEvent;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.stats.Formulas;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class Spoil implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.SPOIL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		if (targets == null)
		{
			return;
		}
		
		for (L2Object tgt : targets)
		{
			if (!tgt.isMonster())
			{
				continue;
			}
			
			L2MonsterInstance target = (L2MonsterInstance) tgt;
			
			if (target.isSpoil())
			{
				activeChar.sendPacket(SystemMessageId.ALREADY_SPOILED);
				continue;
			}
			
			// SPOIL SYSTEM by Lbaldi
			boolean spoil = false;
			if (target.isDead() == false)
			{
				spoil = Formulas.calcMagicSuccess(activeChar, (L2Character) tgt, skill);
				
				if (spoil)
				{
					target.setSpoil(true);
					target.setIsSpoiledBy(activeChar.getObjectId());
					activeChar.sendPacket(SystemMessageId.SPOIL_SUCCESS);
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_RESISTED_YOUR_S2);
					sm.addCharName(target);
					sm.addSkillName(skill);
					activeChar.sendPacket(sm);
				}
				target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, activeChar);
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}