package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExVoteSystemInfo;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.network.serverpackets.UserInfo;

/**
 * Projeto PkElfo
 */
 
public class GiveReco implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.GIVE_RECO
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		for (L2Object obj : targets)
		{
			if (obj.isPlayer())
			{
				L2PcInstance target = obj.getActingPlayer();
				int power = (int) skill.getPower();
				int reco = target.getRecomHave();
				
				if ((reco + power) >= 255)
				{
					power = 255 - reco;
				}
				
				if (power > 0)
				{
					target.setRecomHave(reco + power);
					
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_OBTAINED_S1_RECOMMENDATIONS);
					sm.addNumber(power);
					target.sendPacket(sm);
					target.sendPacket(new UserInfo(target));
					target.sendPacket(new ExVoteSystemInfo(target));
				}
				else
				{
					target.sendPacket(SystemMessageId.NOTHING_HAPPENED);
				}
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}