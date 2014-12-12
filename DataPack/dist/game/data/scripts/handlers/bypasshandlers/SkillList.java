package handlers.bypasshandlers;

import java.util.List;
import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.SkillTreesData;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2NpcInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.base.ClassId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Projeto PkElfo
 */

public class SkillList implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"SkillList"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2NpcInstance))
		{
			return false;
		}
		
		if (Config.ALT_GAME_SKILL_LEARN)
		{
			try
			{
				String id = command.substring(9).trim();
				if (id.length() != 0)
				{
					L2NpcInstance.showSkillList(activeChar, (L2Npc) target, ClassId.getClassId(Integer.parseInt(id)));
				}
				else
				{
					boolean own_class = false;
					
					final List<ClassId> classesToTeach = ((L2NpcInstance) target).getClassesToTeach();
					for (ClassId cid : classesToTeach)
					{
						if (cid.equalsOrChildOf(activeChar.getClassId()))
						{
							own_class = true;
							break;
						}
					}
					
					String text = "<html><body><center>Aprendizagem de Skill:</center><br>";
					
					if (!own_class)
					{
						String charType = activeChar.getClassId().isMage() ? "fighter" : "mage";
						text += "Habilidades de cada classe sao as mais faceis de aprender.<br>" + "Habilidades de uma outra classe da sua raca sao um pouco mais dificil.<br>" + "Habilidades de outras classes e de outra raca sao extremamente dificeis.<br>" + "Mas a mais dificil de todas para aprender e a  " + charType + "skills!<br>";
					}
					
					// make a list of classes
					if (!classesToTeach.isEmpty())
					{
						int count = 0;
						ClassId classCheck = activeChar.getClassId();
						
						while ((count == 0) && (classCheck != null))
						{
							for (ClassId cid : classesToTeach)
							{
								if (cid.level() > classCheck.level())
								{
									continue;
								}
								
								if (SkillTreesData.getInstance().getAvailableSkills(activeChar, cid, false, false).isEmpty())
								{
									continue;
								}
								
								text += "<a action=\"bypass -h npc_%objectId%_SkillList " + cid.getId() + "\">Learn " + cid + "'s class Skills</a><br>\n";
								count++;
							}
							classCheck = classCheck.getParent();
						}
						classCheck = null;
					}
					else
					{
						text += "Nao ha Skills.<br>";
					}
					text += "</body></html>";
					
					NpcHtmlMessage html = new NpcHtmlMessage(((L2Npc) target).getObjectId());
					html.setHtml(text);
					html.replace("%objectId%", String.valueOf(((L2Npc) target).getObjectId()));
					activeChar.sendPacket(html);
					
					activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				}
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
			}
		}
		else
		{
			L2NpcInstance.showSkillList(activeChar, (L2Npc) target, activeChar.getClassId());
		}
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}