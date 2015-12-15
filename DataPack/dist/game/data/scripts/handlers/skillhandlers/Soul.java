package handlers.skillhandlers;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class Soul implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.CHARGESOUL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer() || activeChar.isAlikeDead())
		{
			return;
		}
		
		L2PcInstance player = activeChar.getActingPlayer();
		
		int level = player.getSkillLevel(467);
		if (level > 0)
		{
			L2Skill soulmastery = SkillTable.getInstance().getInfo(467, level);
			
			if (soulmastery != null)
			{
				if (player.getSouls() < soulmastery.getNumSouls())
				{
					int count = 0;
					
					if ((player.getSouls() + skill.getNumSouls()) <= soulmastery.getNumSouls())
					{
						count = skill.getNumSouls();
					}
					else
					{
						count = soulmastery.getNumSouls() - player.getSouls();
					}
					
					player.increaseSouls(count);
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SOUL_CANNOT_BE_INCREASED_ANYMORE);
					player.sendPacket(sm);
					return;
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