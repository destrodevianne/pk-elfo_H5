package handlers.bypasshandlers;

import java.text.DecimalFormat;

import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.L2DropCategory;
import pk.elfo.gameserver.model.L2DropData;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.L2Item;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;

/**
 * Projeto PkElfo
 */

public class DropInfo implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"drop",
		"spoil",
		"info",
		"quest"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		try
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			L2Object targetmob = activeChar.getTarget();
			L2Npc npc = (L2Npc) targetmob;
			String droptext = "";
			
			if (command.startsWith("drop"))
			{
				try
				{
					html.setFile(activeChar.getHtmlPrefix(), "data/html/custom/mobdrop.htm");
					
					if (!(npc.isChampion() || npc.isMinion() || npc.getInstanceType() == InstanceType.L2GrandBossInstance || npc.isRaid() || npc.isRaidMinion() || npc.isMob()))
						return false;	
					if (npc.getTemplate().getDropData().isEmpty())
					{
						droptext = "WARNING: This Npc has no Drops!";
						html.replace("%drops%", droptext);
						activeChar.sendPacket(html);
						return false;
					}
					String champ = "";
					String imgsg = "<img src=\"l2ui.squaregray\" width=\"274\" height=\"1\">";
					String ta_op = "<table bgcolor=333333 cellspacing=2 cellpadding=1><tr><td height=38 fixwidth=36><img src=\"";
					String ta_op2 = "\" height=32 width=32></td><td fixwidth=234><table VALIGN=top valing = top width=234 cellpadding=0 cellspacing=0><tr>";

					final StringBuilder droptext1 = StringUtil.startAppend(9000, champ + "<br>" + imgsg);
					for (L2DropCategory cat : npc.getTemplate().getDropData())
					{
						for (L2DropData drop : cat.getAllDrops())
						{
							final L2Item item = ItemTable.getInstance().getTemplate(drop.getItemId());
							if (item == null)
								continue;
							if (cat.isSweep())
								continue;
							if (drop.isQuestDrop())
								continue;
					


							String smind = null, drops = null;
							String name = item.getName();
							double chance = (drop.getChance()/10000);
							

							if (item.getCrystalType() == 0)
							{
								smind = "<img src=\"L2UI_CH3.joypad_shortcut\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 1)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 2)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 3)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 4)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 5)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 6)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_80\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 7)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_84\" width=16 height=16>";
							}
		
							if (chance <= 0.001)
							{
								DecimalFormat df = new DecimalFormat("#.####");
								drops = df.format(chance);
							}
							else if (chance <= 0.01)
							{
								DecimalFormat df = new DecimalFormat("#.###");
								drops = df.format(chance);
							}
							else
							{
								DecimalFormat df = new DecimalFormat("##.##");
								drops = df.format(chance);
							}	
							if (name.startsWith("Recipe - Sealed"))
								name = "<font color=00FF00>(Re)</font><font color=FF00FF>(Sl)</font>" + name.substring(16);
							if (name.startsWith("Sealed "))
								name = "<font color=FF00FF>(Sl)</font>" + name.substring(7);
							if (name.startsWith("Common Item - "))
								name = "<font color=00FFFF>(Ci)</font>" + name.substring(14);
							if (name.startsWith("Recipe: "))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Recipe -"))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Mid-Grade Life Stone"))
								name = "<font color=fff600>Mid-Grade LS</font>" + name.substring(20);
							if (name.startsWith("High-Grade Life Stone"))
								name = "<font color=fff600>High-Grade LS</font>" + name.substring(21);
							if (name.startsWith("Top-Grade Life Stone"))
								name = "<font color=fff600>Top-Grade LS</font>" + name.substring(20);
							if (name.startsWith("Forgotten Scroll - "))
								name = "<font color=fff600>FS - </font>" + name.substring(19);
							if (name.startsWith("Greater Dye of "))
								name = "<font color=fff600>G Dye of </font>" + name.substring(15);

							droptext1.append(ta_op + item.getIcon()+ta_op2+"<td align=left width=16>" +smind+ "</td><td align=left width=260><font color=fff600>" +name+ "</font></td></tr><tr><td align=left width=16><img src=\"L2UI_CH3.QuestWndToolTipBtn\" width=16 height=16></td><td align=left width=55><font color=E15656>" +drops+ "%</font></td></tr></table></td></tr></table>" + imgsg);
						}
					}
					droptext = droptext1.toString();
					html.replace("%drops%", droptext);
					activeChar.sendPacket(html);
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Something went wrong with the drop preview.");
				}
			}
			if (command.startsWith("spoil"))
			{
				try
				{
					html.setFile(activeChar.getHtmlPrefix(), "data/html/custom/mobspoil.htm");
					
					if (!(npc.isChampion() || npc.isMinion() || npc.isRaid() || npc.isRaidMinion() || npc.isMob()))
						return false;
					if (npc.getTemplate().getDropData().isEmpty())
					{
						droptext = "WARNING: This Npc has no Drops!";
						html.replace("%drops%", droptext);
						activeChar.sendPacket(html);
						return false;
					}
					String champ = "";
					String imgsg = "<img src=\"l2ui.squaregray\" width=\"274\" height=\"1\">";
					String ta_op = "<table bgcolor=333333 cellspacing=2 cellpadding=1><tr><td height=38 fixwidth=36><img src=\"";
					String ta_op2 = "\" height=32 width=32></td><td fixwidth=234><table VALIGN=top valing = top width=234 cellpadding=0 cellspacing=1><tr>";
					final StringBuilder droptext1 = StringUtil.startAppend(1000, champ + "<br>" + imgsg);
					for (L2DropCategory cat : npc.getTemplate().getDropData())
					{
						for (L2DropData drop : cat.getAllDrops())
						{
							final L2Item item = ItemTable.getInstance().getTemplate(drop.getItemId());
							if (item == null)
								continue;
							if (!(cat.isSweep()))
								continue;
							


							String smind = null, drops = null;
							String name = item.getName();
							double chance = (drop.getChance()/10000);
							

							if (item.getCrystalType() == 0)
							{
								smind = "<img src=\"L2UI_CH3.joypad_shortcut\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 1)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 2)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 3)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 4)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 5)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 6)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_80\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 7)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_84\" width=16 height=16>";
							}
		
							if (chance <= 0.001)
							{
								DecimalFormat df = new DecimalFormat("#.####");
								drops = df.format(chance);
							}
							else if (chance <= 0.01)
							{
								DecimalFormat df = new DecimalFormat("#.###");
								drops = df.format(chance);
							}
							else
							{
								DecimalFormat df = new DecimalFormat("##.##");
								drops = df.format(chance);
							}	
							if (name.startsWith("Recipe - Sealed"))
								name = "<font color=00FF00>(Re)</font><font color=FF00FF>(Sl)</font>" + name.substring(16);
							if (name.startsWith("Sealed "))
								name = "<font color=FF00FF>(Sl)</font>" + name.substring(7);
							if (name.startsWith("Common Item - "))
								name = "<font color=00FFFF>(Ci)</font>" + name.substring(14);
							if (name.startsWith("Recipe: "))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Recipe -"))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Mid-Grade Life Stone"))
								name = "<font color=fff600>Mid-Grade LS</font>" + name.substring(20);
							if (name.startsWith("High-Grade Life Stone"))
								name = "<font color=fff600>High-Grade LS</font>" + name.substring(21);
							if (name.startsWith("Top-Grade Life Stone"))
								name = "<font color=fff600>Top-Grade LS</font>" + name.substring(20);
							if (name.startsWith("Forgotten Scroll - "))
								name = "<font color=fff600>FS - </font>" + name.substring(19);
							if (name.startsWith("Greater Dye of "))
								name = "<font color=fff600>G Dye of </font>" + name.substring(15);

							droptext1.append(ta_op + item.getIcon()+ta_op2+"<td align=left width=16>" +smind+ "</td><td align=left width=260><font color=fff600>" +name+ "</font></td></tr><tr><td align=left width=16><img src=\"L2UI_CH3.QuestWndToolTipBtn\" width=16 height=16></td><td align=left width=55><font color=E15656>" +drops+ "%</font></td></tr></table></td></tr></table>" + imgsg);
						}
					}
					droptext = droptext1.toString();
					html.replace("%drops%", droptext);
					activeChar.sendPacket(html);
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Something went wrong with the drop preview.");
				}
			}
			if (command.startsWith("quest"))
			{
				try
				{
					html.setFile(activeChar.getHtmlPrefix(), "data/html/custom/mobquest.htm");
					
					if (!(npc.isChampion() || npc.isMinion() || npc.isRaid() || npc.isRaidMinion() || npc.isMob()))
						return false;
					if (npc.getTemplate().getDropData().isEmpty())
					{
						droptext = "WARNING: This Npc has no Drops!";
						html.replace("%drops%", droptext);
						activeChar.sendPacket(html);
						return false;
					}
					String champ = "";
					String imgsg = "<img src=\"l2ui.squaregray\" width=\"274\" height=\"1\">";
					String ta_op = "<table bgcolor=333333 cellspacing=2 cellpadding=1><tr><td height=38 fixwidth=36><img src=\"";
					String ta_op2 = "\" height=32 width=32></td><td fixwidth=234><table VALIGN=top valing = top width=234 cellpadding=0 cellspacing=1><tr>";
					final StringBuilder droptext1 = StringUtil.startAppend(1000, champ + "<br>" + imgsg);
					for (L2DropCategory cat : npc.getTemplate().getDropData())
					{
						for (L2DropData drop : cat.getAllDrops())
						{
							final L2Item item = ItemTable.getInstance().getTemplate(drop.getItemId());
							if (item == null)
								continue;
							if (!(drop.isQuestDrop()))
								continue;
							


							String smind = null, drops = null;
							String name = item.getName();
							double chance = (drop.getChance()/10000);
							

							if (item.getCrystalType() == 0)
							{
								smind = "<img src=\"L2UI_CH3.joypad_shortcut\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 1)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 2)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 3)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 4)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 5)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 6)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_80\" width=16 height=16>";
							}
							else if (item.getCrystalType() == 7)
							{
								smind = "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_84\" width=16 height=16>";
							}
		
							if (chance <= 0.001)
							{
								DecimalFormat df = new DecimalFormat("#.####");
								drops = df.format(chance);
							}
							else if (chance <= 0.01)
							{
								DecimalFormat df = new DecimalFormat("#.###");
								drops = df.format(chance);
							}
							else
							{
								DecimalFormat df = new DecimalFormat("##.##");
								drops = df.format(chance);
							}	
							if (name.startsWith("Recipe - Sealed"))
								name = "<font color=00FF00>(Re)</font><font color=FF00FF>(Sl)</font>" + name.substring(16);
							if (name.startsWith("Sealed "))
								name = "<font color=FF00FF>(Sl)</font>" + name.substring(7);
							if (name.startsWith("Common Item - "))
								name = "<font color=00FFFF>(Ci)</font>" + name.substring(14);
							if (name.startsWith("Recipe: "))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Recipe -"))
								name = "<font color=00FF00>(Re)</font>" + name.substring(8);
							if (name.startsWith("Mid-Grade Life Stone"))
								name = "<font color=fff600>Mid-Grade LS</font>" + name.substring(20);
							if (name.startsWith("High-Grade Life Stone"))
								name = "<font color=fff600>High-Grade LS</font>" + name.substring(21);
							if (name.startsWith("Top-Grade Life Stone"))
								name = "<font color=fff600>Top-Grade LS</font>" + name.substring(20);
							if (name.startsWith("Forgotten Scroll - "))
								name = "<font color=fff600>FS - </font>" + name.substring(19);
							if (name.startsWith("Greater Dye of "))
								name = "<font color=fff600>G Dye of </font>" + name.substring(15);

							droptext1.append(ta_op + item.getIcon()+ta_op2+"<td align=left width=16>" +smind+ "</td><td align=left width=260><font color=fff600>" +name+ "</font></td></tr><tr><td align=left width=16><img src=\"L2UI_CH3.QuestWndToolTipBtn\" width=16 height=16></td><td align=left width=55><font color=E15656>" +drops+ "%</font></td></tr></table></td></tr></table>" + imgsg);
						}
					}
					droptext = droptext1.toString();
					html.replace("%drops%", droptext);
					activeChar.sendPacket(html);
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Something went wrong with the drop preview.");
				}
			}
		}
		catch (Exception e)
		{
			activeChar.sendMessage("You cant use this option with this target.");
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}