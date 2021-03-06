package handlers.admincommandhandlers;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.datatables.DoorTable;
import pk.elfo.gameserver.datatables.FakePcsTable;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.datatables.MultiSell;
import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.datatables.NpcWalkerRoutesData;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.datatables.TeleportLocationTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.QuestManager;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.olympiad.Olympiad;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import javolution.text.TextBuilder;

/**
 * This class handles following admin commands: - admin|admin1/admin2/admin3/admin4/admin5 = slots for the 5 starting admin menus - gmliston/gmlistoff = includes/excludes active character from /gmlist results - silence = toggles private messages acceptance mode - diet = toggles weight penalty mode -
 * tradeoff = toggles trade acceptance mode - reload = reloads specified component from multisell|skill|npc|htm|item - set/set_menu/set_mod = alters specified server setting - saveolymp = saves olympiad state manually - manualhero = cycles olympiad and calculate new heroes.
 * Projeto PkElfo
 */
public class AdminAdmin implements IAdminCommandHandler
{
	private static final Logger _log = Logger.getLogger(AdminAdmin.class.getName());
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_admin",
		"admin_admin1",
		"admin_admin2",
		"admin_admin3",
		"admin_admin4",
		"admin_admin5",
		"admin_admin6",
		"admin_admin7",
		"admin_admin8",
		"admin_gmliston",
		"admin_gmlistoff",
		"admin_silence",
		"admin_diet",
		"admin_tradeoff",
		"admin_reload",
		"admin_set",
		"admin_set_mod",
		"admin_saveolymp",
		"admin_manualhero",
		"admin_sethero",
		"admin_endolympiad",
		"admin_setconfig",
		"admin_config_server",
		"admin_gmon"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_admin"))
		{
			showMainPage(activeChar, command);
		}
		else if (command.equals("admin_config_server"))
		{
			showConfigPage(activeChar);
		}
		else if (command.startsWith("admin_gmliston"))
		{
			AdminTable.getInstance().showGm(activeChar);
			activeChar.sendMessage("Registrado na lista de GM");
			AdminHelpPage.showHelpPage(activeChar, "gm_menu.htm");
		}
		else if (command.startsWith("admin_gmlistoff"))
		{
			AdminTable.getInstance().hideGm(activeChar);
			activeChar.sendMessage("Removido da lista de GM");
			AdminHelpPage.showHelpPage(activeChar, "gm_menu.htm");
		}
		else if (command.startsWith("admin_silence"))
		{
			if (activeChar.isSilenceMode()) // already in message refusal mode
			{
				activeChar.setSilenceMode(false);
				activeChar.sendPacket(SystemMessageId.MESSAGE_ACCEPTANCE_MODE);
			}
			else
			{
				activeChar.setSilenceMode(true);
				activeChar.sendPacket(SystemMessageId.MESSAGE_REFUSAL_MODE);
			}
			AdminHelpPage.showHelpPage(activeChar, "gm_menu.htm");
		}
		else if (command.startsWith("admin_saveolymp"))
		{
			Olympiad.getInstance().saveOlympiadStatus();
			activeChar.sendMessage("Sistema de Olimpiadas salvo.");
		}
		else if (command.startsWith("admin_endolympiad"))
		{
			try
			{
				Olympiad.getInstance().manualSelectHeroes();
			}
			catch (Exception e)
			{
				_log.warning("Ocorreu um erro ao terminar a olimpiada: " + e);
			}
			activeChar.sendMessage("Herois formados.");
		}
		else if (command.startsWith("admin_manualhero") || command.startsWith("admin_sethero"))
		{
			if (activeChar.getTarget() == null)
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				return false;
			}
			
			final L2PcInstance target = activeChar.getTarget().isPlayer() ? activeChar.getTarget().getActingPlayer() : activeChar;
			target.setHero(!target.isHero());
			target.broadcastUserInfo();
		}
		else if (command.startsWith("admin_diet"))
		{
			try
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				if (st.nextToken().equalsIgnoreCase("on"))
				{
					activeChar.setDietMode(true);
					activeChar.sendMessage("Modo dieta on");
				}
				else if (st.nextToken().equalsIgnoreCase("off"))
				{
					activeChar.setDietMode(false);
					activeChar.sendMessage("Modo dieta off");
				}
			}
			catch (Exception ex)
			{
				if (activeChar.getDietMode())
				{
					activeChar.setDietMode(false);
					activeChar.sendMessage("Modo dieta off");
				}
				else
				{
					activeChar.setDietMode(true);
					activeChar.sendMessage("Modo dieta on");
				}
			}
			finally
			{
				activeChar.refreshOverloaded();
			}
			AdminHelpPage.showHelpPage(activeChar, "gm_menu.htm");
		}
		else if (command.startsWith("admin_tradeoff"))
		{
			try
			{
				String mode = command.substring(15);
				if (mode.equalsIgnoreCase("on"))
				{
					activeChar.setTradeRefusal(true);
					activeChar.sendMessage("Recusa de trade habilitado");
				}
				else if (mode.equalsIgnoreCase("off"))
				{
					activeChar.setTradeRefusal(false);
					activeChar.sendMessage("Recusa de trade desabilitado");
				}
			}
			catch (Exception ex)
			{
				if (activeChar.getTradeRefusal())
				{
					activeChar.setTradeRefusal(false);
					activeChar.sendMessage("Recusa de trade desabilitado");
				}
				else
				{
					activeChar.setTradeRefusal(true);
					activeChar.sendMessage("Recusa de trade habilitado");
				}
			}
			AdminHelpPage.showHelpPage(activeChar, "gm_menu.htm");
		}
		else if (command.startsWith("admin_reload"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			if (!st.hasMoreTokens())
			{
				activeChar.sendMessage("Voce precisa especificar um tipo de relogamento!");
				activeChar.sendMessage("Use: //reload <multisell|teleport|skill|npc|htm|item|config|npcwalkers|access|quests>");
				return false;
			}
			
			final String type = st.nextToken();
			try
			{
				if (type.equals("multisell"))
				{
					MultiSell.getInstance().reload();
					activeChar.sendMessage("Todas as Multisells foram relogadas");
				}
				else if (type.startsWith("teleport"))
				{
					TeleportLocationTable.getInstance().reloadAll();
					activeChar.sendMessage("Localizacoes de teleportes foram relogadas");
				}
				else if (type.startsWith("skill"))
				{
					SkillTable.getInstance().reload();
					activeChar.sendMessage("Todas as Skills have foram relogadas");
				}
				else if (type.startsWith("npcId"))
				{
					Integer npcId = Integer.parseInt(st.nextToken());
					if (npcId != null)
					{
						NpcTable.getInstance().reloadNpc(npcId);
						for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(npcId))
						{
							if (spawn != null)
							{
								spawn.respawnNpc(spawn.getLastSpawn());
							}
						}
						activeChar.sendMessage("NPC " + npcId + " foi relogado");
					}
				}
				else if (type.equals("npc"))
				{
					NpcTable.getInstance().reloadAllNpc();
					QuestManager.getInstance().reloadAllQuests();
					activeChar.sendMessage("Todos os NPCs foram relogadas");
				}
				else if (type.startsWith("htm"))
				{
					HtmCache.getInstance().reload();
					activeChar.sendMessage("Cache[HTML]: " + HtmCache.getInstance().getMemoryUsage() + " megabytes em " + HtmCache.getInstance().getLoadedFiles() + " arquivos lidos");
				}
				else if (type.startsWith("item"))
				{
					ItemTable.getInstance().reload();
					activeChar.sendMessage("Modelos de Items foram relogados");
				}
				else if (type.startsWith("config"))
				{
					Config.load();
					activeChar.sendMessage("Todas as Definicoes de Configuracoes foram relogadas");
				}
				else if (type.startsWith("npcwalkers"))
				{
					NpcWalkerRoutesData.getInstance().load();
					activeChar.sendMessage("NPC Walker Routes foram relogados");
				}
				else if (type.startsWith("access"))
				{
					AdminTable.getInstance().load();
					activeChar.sendMessage("Direitos de Acesso foram relogados");
				}
				else if (type.startsWith("quests"))
				{
					QuestManager.getInstance().reloadAllQuests();
					activeChar.sendMessage("Todas as Quests foram relogados.");
				}
				else if (type.startsWith("door"))
				{
					DoorTable.getInstance().load();
					activeChar.sendMessage("Todas as Portas e Portoes foram relogados");
				}
                else if (type.startsWith("fakenpc"))
                {
                    FakePcsTable.getInstance().reloadData();
                    activeChar.sendMessage("Todos os Fake NPC foram relogados");
                }
				activeChar.sendMessage("AVISO: Existem varios problemas conhecidos em relacao a este recurso. Recarregando os dados do servidor, isto e FORTEMENTE NAO RECOMENDADO para os servidores ativos, apenas para o desenvolvimento de ambientes.");
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Ocorreu um erro ao recarregar " + type + " !");
				activeChar.sendMessage("Use: //reload <multisell|teleport|skill|npc|htm|item|config|npcwalkers|access|quests>");
				_log.log(Level.WARNING, "Ocorreu um erro ao recarregar " + type + ": " + e, e);
			}
		}
		else if (command.startsWith("admin_setconfig"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			try
			{
				String pName = st.nextToken();
				String pValue = st.nextToken();
				if (Config.setParameterValue(pName, pValue))
				{
					activeChar.sendMessage("Parametro de configuracao " + pName + " definido para " + pValue);
				}
				else
				{
					activeChar.sendMessage("Parametro invalido!");
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Use: //setconfig <parameter> <value>");
			}
			finally
			{
				showConfigPage(activeChar);
			}
		}
		else if (command.startsWith("admin_set"))
		{
			StringTokenizer st = new StringTokenizer(command);
			String[] cmd = st.nextToken().split("_");
			try
			{
				String[] parameter = st.nextToken().split("=");
				String pName = parameter[0].trim();
				String pValue = parameter[1].trim();
				if (Config.setParameterValue(pName, pValue))
				{
					activeChar.sendMessage("parametro " + pName + " com sucesso para definir " + pValue);
				}
				else
				{
					activeChar.sendMessage("Parametro invalido!");
				}
			}
			catch (Exception e)
			{
				if (cmd.length == 2)
				{
					activeChar.sendMessage("Use: //set parameter=value");
				}
			}
			finally
			{
				if (cmd.length == 3)
				{
					if (cmd[2].equalsIgnoreCase("mod"))
					{
						AdminHelpPage.showHelpPage(activeChar, "mods_menu.htm");
					}
				}
			}
		}
		else if (command.startsWith("admin_gmon"))
		{
			// nothing
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showMainPage(L2PcInstance activeChar, String command)
	{
		int mode = 0;
		String filename = null;
		try
		{
			mode = Integer.parseInt(command.substring(11));
		}
		catch (Exception e)
		{
		}
		switch (mode)
		{
			case 1:
				filename = "main";
				break;
			case 2:
				filename = "game";
				break;
			case 3:
				filename = "effects";
				break;
			case 4:
				filename = "server";
				break;
			case 5:
				filename = "mods";
				break;
			case 6:
				filename = "char";
				break;
			case 7:
				filename = "gm";
				break;
			case 8:
				filename = "old";
				break;
			default:
				if (Config.GM_ADMIN_MENU_STYLE.equals("modern"))
				{
					filename = "main";
				}
				else
				{
					filename = "classic";
				}
				break;
		}
		AdminHelpPage.showHelpPage(activeChar, filename + "_menu.htm");
	}
	
	public void showConfigPage(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		TextBuilder replyMSG = new TextBuilder("<html><title>L2J PkElfo :: Configuracao</title><body>");
		replyMSG.append("<center><table width=270><tr><td width=60><button value=\"Principal\" action=\"bypass -h admin_admin\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td width=150>Painel de Config do Server</td><td width=60><button value=\"Back\" action=\"bypass -h admin_admin4\" width=60 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></center><br>");
		replyMSG.append("<center><table width=260><tr><td width=140></td><td width=40></td><td width=40></td></tr>");
		replyMSG.append("<tr><td><font color=\"00AA00\">Drop:</font></td><td></td><td></td></tr>");
		replyMSG.append("<tr><td><font color=\"LEVEL\">Rate EXP</font> = " + Config.RATE_XP + "</td><td><edit var=\"param1\" width=40 height=15></td><td><button value=\"Set\" action=\"bypass -h admin_setconfig RateXp $param1\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		replyMSG.append("<tr><td><font color=\"LEVEL\">Rate SP</font> = " + Config.RATE_SP + "</td><td><edit var=\"param2\" width=40 height=15></td><td><button value=\"Set\" action=\"bypass -h admin_setconfig RateSp $param2\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		replyMSG.append("<tr><td><font color=\"LEVEL\">Rate Drop Spoil</font> = " + Config.RATE_DROP_SPOIL + "</td><td><edit var=\"param4\" width=40 height=15></td><td><button value=\"Set\" action=\"bypass -h admin_setconfig RateDropSpoil $param4\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		replyMSG.append("<tr><td width=140></td><td width=40></td><td width=40></td></tr>");
		replyMSG.append("<tr><td><font color=\"00AA00\">Enchant:</font></td><td></td><td></td></tr>");
		replyMSG.append("<tr><td><font color=\"LEVEL\">Encantar Elemento de pedra</font> = " + Config.ENCHANT_CHANCE_ELEMENT_STONE + "</td><td><edit var=\"param8\" width=40 height=15></td><td><button value=\"Set\" action=\"bypass -h admin_setconfig EnchantChanceElementStone $param8\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		replyMSG.append("<tr><td><font color=\"LEVEL\">Encantar Elemento de cristal</font> = " + Config.ENCHANT_CHANCE_ELEMENT_CRYSTAL + "</td><td><edit var=\"param9\" width=40 height=15></td><td><button value=\"Set\" action=\"bypass -h admin_setconfig EnchantChanceElementCrystal $param9\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		replyMSG.append("<tr><td><font color=\"LEVEL\">Encantar Elemento de Joia</font> = " + Config.ENCHANT_CHANCE_ELEMENT_JEWEL + "</td><td><edit var=\"param10\" width=40 height=15></td><td><button value=\"Set\" action=\"bypass -h admin_setconfig EnchantChanceElementJewel $param10\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		replyMSG.append("<tr><td><font color=\"LEVEL\">Encantar Elemento de Energia</font> = " + Config.ENCHANT_CHANCE_ELEMENT_ENERGY + "</td><td><edit var=\"param11\" width=40 height=15></td><td><button value=\"Set\" action=\"bypass -h admin_setconfig EnchantChanceElementEnergy $param11\" width=40 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
		replyMSG.append("</table></body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
}