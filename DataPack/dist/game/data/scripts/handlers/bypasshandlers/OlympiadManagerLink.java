package handlers.bypasshandlers;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.MultiSell;
import pk.elfo.gameserver.datatables.NpcBufferTable;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.instance.L2OlympiadManagerInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.olympiad.CompetitionType;
import pk.elfo.gameserver.model.olympiad.Olympiad;
import pk.elfo.gameserver.model.olympiad.OlympiadManager;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExHeroList;
import pk.elfo.gameserver.network.serverpackets.InventoryUpdate;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.util.L2FastList;

/**
 * Projeto PkElfo
 */

public class OlympiadManagerLink implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"olympiaddesc",
		"olympiadnoble",
		"olybuff",
		"olympiad"
	};
	
	private static final String FEWER_THAN = "Fewer than " + String.valueOf(Config.ALT_OLY_REG_DISPLAY);
	private static final String MORE_THAN = "More than " + String.valueOf(Config.ALT_OLY_REG_DISPLAY);
	private static final int GATE_PASS = Config.ALT_OLY_COMP_RITEM;
	
	@Override
	public final boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2OlympiadManagerInstance))
		{
			return false;
		}
		
		try
		{
			if (command.toLowerCase().startsWith("olympiaddesc"))
			{
				int val = Integer.parseInt(command.substring(13, 14));
				String suffix = command.substring(14);
				((L2OlympiadManagerInstance) target).showChatWindow(activeChar, val, suffix);
			}
			else if (command.toLowerCase().startsWith("olympiadnoble"))
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(target.getObjectId());
				if (activeChar.isCursedWeaponEquipped())
				{
					html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_cursed_weapon.htm");
					activeChar.sendPacket(html);
					return false;
				}
				if (activeChar.getClassIndex() != 0)
				{
					html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_sub.htm");
					html.replace("%objectId%", String.valueOf(target.getObjectId()));
					activeChar.sendPacket(html);
					return false;
				}
				if (!activeChar.isNoble() || (activeChar.getClassId().level() < 3))
				{
					html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_thirdclass.htm");
					html.replace("%objectId%", String.valueOf(target.getObjectId()));
					activeChar.sendPacket(html);
					return false;
				}
				
				int passes;
				int val = Integer.parseInt(command.substring(14));
				switch (val)
				{
					case 0: // H5 match selection
						if (!OlympiadManager.getInstance().isRegistered(activeChar))
						{
							final int olympiad_round = 0; // TODO : implement me
							final int olympiad_week = 0; // TODO: implement me
							final int olympiad_participant = 0; // TODO: implement me
							
							html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_desc2a.htm");
							html.replace("%objectId%", String.valueOf(target.getObjectId()));
							html.replace("%olympiad_round%", String.valueOf(olympiad_round));
							html.replace("%olympiad_week%", String.valueOf(olympiad_week));
							html.replace("%olympiad_participant%", String.valueOf(olympiad_participant));
							activeChar.sendPacket(html);
						}
						else
						{
							html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_unregister.htm");
							html.replace("%objectId%", String.valueOf(target.getObjectId()));
							activeChar.sendPacket(html);
						}
						break;
					case 1: // unregister
						OlympiadManager.getInstance().unRegisterNoble(activeChar);
						break;
					case 2: // show waiting list | TODO: cleanup (not used anymore)
						final int nonClassed = OlympiadManager.getInstance().getRegisteredNonClassBased().size();
						final int teams = OlympiadManager.getInstance().getRegisteredTeamsBased().size();
						final Collection<List<Integer>> allClassed = OlympiadManager.getInstance().getRegisteredClassBased().values();
						int classed = 0;
						if (!allClassed.isEmpty())
						{
							for (List<Integer> cls : allClassed)
							{
								if (cls != null)
								{
									classed += cls.size();
								}
							}
						}
						html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_registered.htm");
						if (Config.ALT_OLY_REG_DISPLAY > 0)
						{
							html.replace("%listClassed%", classed < Config.ALT_OLY_REG_DISPLAY ? FEWER_THAN : MORE_THAN);
							html.replace("%listNonClassedTeam%", teams < Config.ALT_OLY_REG_DISPLAY ? FEWER_THAN : MORE_THAN);
							html.replace("%listNonClassed%", nonClassed < Config.ALT_OLY_REG_DISPLAY ? FEWER_THAN : MORE_THAN);
						}
						else
						{
							html.replace("%listClassed%", String.valueOf(classed));
							html.replace("%listNonClassedTeam%", String.valueOf(teams));
							html.replace("%listNonClassed%", String.valueOf(nonClassed));
						}
						html.replace("%objectId%", String.valueOf(target.getObjectId()));
						activeChar.sendPacket(html);
						break;
					case 3: // There are %points% Grand Olympiad points granted for this event. | TODO: cleanup (not used anymore)
						int points = Olympiad.getInstance().getNoblePoints(activeChar.getObjectId());
						html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_points1.htm");
						html.replace("%points%", String.valueOf(points));
						html.replace("%objectId%", String.valueOf(target.getObjectId()));
						activeChar.sendPacket(html);
						break;
					case 4: // register non classed
						OlympiadManager.getInstance().registerNoble(activeChar, CompetitionType.NON_CLASSED);
						break;
					case 5: // register classed
						OlympiadManager.getInstance().registerNoble(activeChar, CompetitionType.CLASSED);
						break;
					case 6: // request tokens reward
						passes = Olympiad.getInstance().getNoblessePasses(activeChar, false);
						if (passes > 0)
						{
							html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_settle.htm");
							html.replace("%objectId%", String.valueOf(target.getObjectId()));
							activeChar.sendPacket(html);
						}
						else
						{
							html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_nopoints2.htm");
							html.replace("%objectId%", String.valueOf(target.getObjectId()));
							activeChar.sendPacket(html);
						}
						break;
					case 7: // Equipment Rewards
						MultiSell.getInstance().separateAndSend(102, activeChar, (L2Npc) target, false);
						break;
					case 8: // Misc. Rewards
						MultiSell.getInstance().separateAndSend(103, activeChar, (L2Npc) target, false);
						break;
					case 9: // Your Grand Olympiad Score from the previous period is %points% point(s) | TODO: cleanup (not used anymore)
						int point = Olympiad.getInstance().getLastNobleOlympiadPoints(activeChar.getObjectId());
						html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "noble_points2.htm");
						html.replace("%points%", String.valueOf(point));
						html.replace("%objectId%", String.valueOf(target.getObjectId()));
						activeChar.sendPacket(html);
						break;
					case 10: // give tokens to player
						passes = Olympiad.getInstance().getNoblessePasses(activeChar, true);
						if (passes > 0)
						{
							L2ItemInstance item = activeChar.getInventory().addItem("Olympiad", GATE_PASS, passes, activeChar, target);
							
							InventoryUpdate iu = new InventoryUpdate();
							iu.addModifiedItem(item);
							activeChar.sendPacket(iu);
							
							final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
							sm.addItemNumber(passes);
							sm.addItemName(item);
							activeChar.sendPacket(sm);
						}
						break;
					case 11: // register team
						OlympiadManager.getInstance().registerNoble(activeChar, CompetitionType.TEAMS);
						break;
					default:
						_log.warning("Sistema de olimpiada: Nao conseguiram enviar o pacote para o pedido " + val);
						break;
				}
			}
			else if (command.toLowerCase().startsWith("olybuff"))
			{
				if (activeChar.olyBuff <= 0)
				{
					return false;
				}
				
				NpcHtmlMessage html = new NpcHtmlMessage(target.getObjectId());
				String[] params = command.split(" ");
				
				if (params[1] == null)
				{
					_log.warning("Olympiad Buffer Aviso: npcId = " + ((L2Npc) target).getNpcId() + " nao ha buffGroup definido no bypass para o buff selecionado.");
					return false;
				}
				int buffGroup = Integer.parseInt(params[1]);
				
				int[] npcBuffGroupInfo = NpcBufferTable.getInstance().getSkillInfo(((L2Npc) target).getNpcId(), buffGroup);
				
				if (npcBuffGroupInfo == null)
				{
					_log.warning("Olympiad Buffer Aviso: npcId = " + ((L2Npc) target).getNpcId() + " Localizacao: " + target.getX() + ", " + target.getY() + ", " + target.getZ() + " Jogador: " + activeChar.getName() + " tentou usar skill de grupo (" + buffGroup + ") nao atribuido ao NPC Buffer!");
					return false;
				}
				
				int skillId = npcBuffGroupInfo[0];
				int skillLevel = npcBuffGroupInfo[1];
				
				L2Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
				
				target.setTarget(activeChar);
				
				if (activeChar.olyBuff > 0)
				{
					if (skill != null)
					{
						activeChar.olyBuff--;
						target.broadcastPacket(new MagicSkillUse(target, activeChar, skill.getId(), skill.getLevel(), 0, 0));
						skill.getEffects(activeChar, activeChar);
						L2Summon summon = activeChar.getSummon();
						if (summon != null)
						{
							target.broadcastPacket(new MagicSkillUse(target, summon, skill.getId(), skill.getLevel(), 0, 0));
							skill.getEffects(summon, summon);
						}
					}
				}
				
				if (activeChar.olyBuff > 0)
				{
					html.setFile(activeChar.getHtmlPrefix(), activeChar.olyBuff == 5 ? Olympiad.OLYMPIAD_HTML_PATH + "olympiad_buffs.htm" : Olympiad.OLYMPIAD_HTML_PATH + "olympiad_5buffs.htm");
					html.replace("%objectId%", String.valueOf(target.getObjectId()));
					activeChar.sendPacket(html);
				}
				else
				{
					html.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "olympiad_nobuffs.htm");
					html.replace("%objectId%", String.valueOf(target.getObjectId()));
					activeChar.sendPacket(html);
					target.decayMe();
				}
			}
			else if (command.toLowerCase().startsWith("olympiad"))
			{
				int val = Integer.parseInt(command.substring(9, 10));
				
				NpcHtmlMessage reply = new NpcHtmlMessage(target.getObjectId());
				
				switch (val)
				{
					case 2: // show rank for a specific class
						// for example >> Olympiad 1_88
						int classId = Integer.parseInt(command.substring(11));
						if (((classId >= 88) && (classId <= 118)) || ((classId >= 131) && (classId <= 134)) || (classId == 136))
						{
							L2FastList<String> names = Olympiad.getInstance().getClassLeaderBoard(classId);
							reply.setFile(activeChar.getHtmlPrefix(), Olympiad.OLYMPIAD_HTML_PATH + "olympiad_ranking.htm");
							
							int index = 1;
							for (String name : names)
							{
								reply.replace("%place" + index + "%", String.valueOf(index));
								reply.replace("%rank" + index + "%", name);
								index++;
								if (index > 10)
								{
									break;
								}
							}
							for (; index <= 10; index++)
							{
								reply.replace("%place" + index + "%", "");
								reply.replace("%rank" + index + "%", "");
							}
							
							reply.replace("%objectId%", String.valueOf(target.getObjectId()));
							activeChar.sendPacket(reply);
						}
						break;
					case 4: // hero list
						activeChar.sendPacket(new ExHeroList());
						break;
					default:
						_log.warning("Sistema de Olimpiada: Nao conseguiram enviar o pacote para o pedido " + val);
						break;
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		
		return true;
	}
	
	@Override
	public final String[] getBypassList()
	{
		return COMMANDS;
	}
}