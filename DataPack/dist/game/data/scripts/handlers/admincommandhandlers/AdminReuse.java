package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.SkillCoolTime;

/**
 * Projeto PkElfo
 */

public class AdminReuse implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_reuse",
	};

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		if (command.startsWith("admin_reuse")){
			if(activeChar.getTarget() != null){
				for (L2Skill skill : ((L2Character) activeChar.getTarget()).getAllSkills())
				{
					((L2Character) activeChar.getTarget()).enableSkill(skill);
				}
				((L2PcInstance) activeChar.getTarget()).sendSkillList();
				((L2PcInstance) activeChar.getTarget()).sendPacket(new SkillCoolTime((L2PcInstance) activeChar.getTarget()));
			}
			else
			{
				for (L2Skill skill : activeChar.getAllSkills())
				{
					activeChar.enableSkill(skill);
				}
				activeChar.sendSkillList();
				activeChar.sendPacket(new SkillCoolTime(activeChar));
			}
	}
	return true;
}
      
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}