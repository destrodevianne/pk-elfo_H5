package handlers.admincommandhandlers;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;

/**
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */
public class AdminTest implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_stats",
		"admin_skill_test",
		"admin_known"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_stats"))
		{
			for (String line : ThreadPoolManager.getInstance().getStats())
			{
				activeChar.sendMessage(line);
			}
		}
		else if (command.startsWith("admin_skill_test"))
		{
			try
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				int id = Integer.parseInt(st.nextToken());
				if (command.startsWith("admin_skill_test"))
				{
					adminTestSkill(activeChar, id, true);
				}
				else
				{
					adminTestSkill(activeChar, id, false);
				}
			}
			catch (NumberFormatException e)
			{
				activeChar.sendMessage("Command format is //skill_test <ID>");
			}
			catch (NoSuchElementException nsee)
			{
				activeChar.sendMessage("Command format is //skill_test <ID>");
			}
		}
		else if (command.equals("admin_known on"))
		{
			Config.CHECK_KNOWN = true;
		}
		else if (command.equals("admin_known off"))
		{
			Config.CHECK_KNOWN = false;
		}
		return true;
	}
	
	/**
	 * @param activeChar
	 * @param id
	 * @param msu
	 */
	private void adminTestSkill(L2PcInstance activeChar, int id, boolean msu)
	{
		L2Character caster;
		L2Object target = activeChar.getTarget();
		if (!(target instanceof L2Character))
		{
			caster = activeChar;
		}
		else
		{
			caster = (L2Character) target;
		}
		
		L2Skill _skill = SkillTable.getInstance().getInfo(id, 1);
		if (_skill != null)
		{
			caster.setTarget(activeChar);
			if (msu)
			{
				caster.broadcastPacket(new MagicSkillUse(caster, activeChar, id, 1, _skill.getHitTime(), _skill.getReuseDelay()));
			}
			else
			{
				caster.doCast(_skill);
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
