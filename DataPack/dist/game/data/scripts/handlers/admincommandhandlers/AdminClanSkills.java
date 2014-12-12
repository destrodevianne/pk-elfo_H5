package handlers.admincommandhandlers;
 
import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;

/**
 * Projeto PkElfo
 */
   
public class AdminClanSkills implements IAdminCommandHandler
{
	public static final String[] ADMIN_COMMANDS = { "admin_clanskills" };
 
	@Override
	public boolean useAdminCommand (String command,L2PcInstance activeChar)
	{
		if (command.startsWith("admin_clanskills"))
		{
			L2PcInstance target = (L2PcInstance) activeChar.getTarget();
			ClanTable.getInstance().getClan(target.getClanId());
			L2Skill skilll = L2Skill.valueOf(370, 3);
			L2Skill skill2 = L2Skill.valueOf(373, 3);
			L2Skill skill3 = L2Skill.valueOf(379, 3);
			L2Skill skill4 = L2Skill.valueOf(391, 1);
			L2Skill skill5 = L2Skill.valueOf(371, 3);
			L2Skill skill6 = L2Skill.valueOf(374, 3);
			L2Skill skill7 = L2Skill.valueOf(379, 3);
			L2Skill skill8 = L2Skill.valueOf(376, 3);
			L2Skill skill9 = L2Skill.valueOf(377, 3);
			L2Skill skill10 = L2Skill.valueOf(383, 3);
			L2Skill skill11 = L2Skill.valueOf(380, 3);
			L2Skill skill12 = L2Skill.valueOf(382, 3);
			L2Skill skill13 = L2Skill.valueOf(384, 3);
			L2Skill skill14 = L2Skill.valueOf(385, 3);
			L2Skill skill15 = L2Skill.valueOf(386, 3);
			L2Skill skill16 = L2Skill.valueOf(387, 3);
			L2Skill skill17 = L2Skill.valueOf(388, 3);
			L2Skill skill18 = L2Skill.valueOf(390, 3);
			L2Skill skill19 = L2Skill.valueOf(372, 3);
			L2Skill skill20 = L2Skill.valueOf(375, 3);
			L2Skill skill21 = L2Skill.valueOf(378, 3);
			L2Skill skill22 = L2Skill.valueOf(381, 3);
			L2Skill skill23 = L2Skill.valueOf(389, 3);
			target.getClan().addSkill(skilll);
			target.getClan().addSkill(skill2);
			target.getClan().addSkill(skill3);
			target.getClan().addSkill(skill4);
			target.getClan().addSkill(skill5);
			target.getClan().addSkill(skill6);
			target.getClan().addSkill(skill7);
			target.getClan().addSkill(skill8);
			target.getClan().addSkill(skill9);
			target.getClan().addSkill(skill10);
			target.getClan().addSkill(skill11);
			target.getClan().addSkill(skill12);
			target.getClan().addSkill(skill13);
			target.getClan().addSkill(skill14);
			target.getClan().addSkill(skill15);
			target.getClan().addSkill(skill16);
			target.getClan().addSkill(skill17);
			target.getClan().addSkill(skill18);
			target.getClan().addSkill(skill19);
			target.getClan().addSkill(skill20);
			target.getClan().addSkill(skill21);
			target.getClan().addSkill(skill22);
			target.getClan().addSkill(skill23);
		}
		else if (!activeChar.isClanLeader())
		{
			activeChar.sendMessage("O que voce esta fazendo? Ele nao e um lider de clan. A acao falhou!");
			return false;
		}
		else
		{
			activeChar.sendMessage("Clan nao encontrado");
		}
		return true;
		}
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}