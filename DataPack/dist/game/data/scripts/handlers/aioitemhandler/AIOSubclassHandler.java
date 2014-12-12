package handlers.aioitemhandler;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.ClassListData;
import pk.elfo.gameserver.handler.IAIOItemHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2VillageMasterInstance;
import pk.elfo.gameserver.model.base.ClassId;
import pk.elfo.gameserver.model.base.PlayerClass;
import pk.elfo.gameserver.model.base.Race;
import pk.elfo.gameserver.model.base.SubClass;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;

/**
 * Projeto PkElfo
 */

public class AIOSubclassHandler implements IAIOItemHandler
{
	private final Logger _log = Logger.getLogger(AIOSubclassHandler.class.getName());
	private final String BYPASS = "subclass";
	
	@Override
	public String getBypass()
	{
		return BYPASS;
	}
	
	@Override
	public void onBypassUse(L2PcInstance player, String command)
	{
		// Subclasses may not be changed while a skill is in use.
		if (player.isCastingNow() || player.isAllSkillsDisabled())
		{
			player.sendPacket(SystemMessageId.SUBCLASS_NO_CHANGE_OR_CREATE_WHILE_SKILL_IN_USE);
			return;
		}
		final NpcHtmlMessage html = new NpcHtmlMessage(player.getObjectId());
		// Subclasses may not be changed while a transformated state.
		if (player.getTransformation() != null)
		{
			html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_NoTransformed.htm");
			player.sendPacket(html);
			return;
		}
		// Subclasses may not be changed while a summon is active.
		if (player.hasSummon())
		{
			html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_NoSummon.htm");
			player.sendPacket(html);
			return;
		}
		// Subclasses may not be changed while you have exceeded your inventory limit.
		if (!player.isInventoryUnder90(true))
		{
			player.sendPacket(SystemMessageId.NOT_SUBCLASS_WHILE_INVENTORY_FULL);
			return;
		}
		// Subclasses may not be changed while a you are over your weight limit.
		if (player.getWeightPenalty() >= 2)
		{
			player.sendPacket(SystemMessageId.NOT_SUBCLASS_WHILE_OVERWEIGHT);
			return;
		}
		
		int cmdChoice = 0;
		int paramOne = 0;
		int paramTwo = 0;
		try
		{
			if (command.contains(" "))
			{
				String[] subCmd = command.split(" ");
				if (subCmd.length == 3)
				{
					cmdChoice = Integer.valueOf(subCmd[0]);
					paramOne = Integer.valueOf(subCmd[1]);
					paramTwo = Integer.valueOf(subCmd[2]);
				}
				else if (subCmd.length == 2)
			{
					cmdChoice = Integer.valueOf(subCmd[0]);
					paramOne = Integer.valueOf(subCmd[1]);
				}
				else
				{
					cmdChoice = Integer.valueOf(subCmd[0]);
				}
			}
			else
			{
				cmdChoice = Integer.valueOf(command);
			}
		}
		catch (Exception NumberFormatException)
		{
			_log.warning(AIOSubclassHandler.class.getName() + ": Wrong numeric values for command " + command);
		}
		Set<PlayerClass> subsAvailable = null;
		switch (cmdChoice)
		{
			case 0: // Subclass change menu
			{
				html.setFile(player.getHtmlPrefix(), getSubClassMenu(player.getRace()));
				break;
			}
			case 1: // Add Subclass - Initial
			{ // Avoid giving player an option to add a new sub class, if they have max sub-classes already.
				if (player.getTotalSubClasses() >= Config.MAX_SUBCLASS)
				{
					html.setFile(player.getHtmlPrefix(), getSubClassFail());
					break;
				}
				
				subsAvailable = getAvailableSubClasses(player);
				if ((subsAvailable != null) && !subsAvailable.isEmpty())
				{
					html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_Add.htm");
					final StringBuilder content1 = StringUtil.startAppend(200);
					for (PlayerClass subClass : subsAvailable)
					{
						StringUtil.append(content1, "<a action=\"bypass -h Aioitem_subclass_4 ", String.valueOf(subClass.ordinal()), "\" msg=\"1268;", ClassListData.getInstance().getClass(subClass.ordinal()).getClassName(), "\">", ClassListData.getInstance().getClass(subClass.ordinal()).getClientCode(), "</a><br>");
					}
					html.replace("%list%", content1.toString());
				}
				else
				{
					if ((player.getRace() == Race.Elf) || (player.getRace() == Race.DarkElf))
					{
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_Fail_Elves.htm");
						player.sendPacket(html);
					}
					else if (player.getRace() == Race.Kamael)
					{
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_Fail_Kamael.htm");
						player.sendPacket(html);
					}
					else
					{
						// TODO: Retail message
						player.sendMessage("There are no sub classes available at this time.");
					}
					return;
				}
				break;
			}
			case 2: // Change Class - Initial
			{
				if (player.getSubClasses().isEmpty())
				{
					html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ChangeNo.htm");
				}
				else
				{
					final StringBuilder content2 = StringUtil.startAppend(200);
					if (checkVillageMaster(player.getBaseClass()))
					{
						StringUtil.append(content2, "<a action=\"bypass -h Aioitem_subclass_5 0\">", ClassListData.getInstance().getClass(player.getBaseClass()).getClientCode(), "</a><br>");
					}
					
					for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
					{
						SubClass subClass = subList.next();
						if (checkVillageMaster(subClass.getClassDefinition()))
						{
							StringUtil.append(content2, "<a action=\"bypass -h Aioitem_subclass_5 ", String.valueOf(subClass.getClassIndex()), "\">", ClassListData.getInstance().getClass(subClass.getClassId()).getClientCode(), "</a><br>");
						}
					}
					
					if (content2.length() > 0)
					{
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_Change.htm");
						html.replace("%list%", content2.toString());
					}
					else
					{
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ChangeNotFound.htm");
					}
				}
				break;
			}
			case 3: // Change/Cancel Subclass - Initial
			{
				if ((player.getSubClasses() == null) || player.getSubClasses().isEmpty())
				{
					html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ModifyEmpty.htm");
					break;
				}
				
				// custom value
				if (player.getTotalSubClasses() > 3)
				{
					html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ModifyCustom.htm");
					final StringBuilder content3 = StringUtil.startAppend(200);
					int classIndex = 1;
					
					for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
					{
						SubClass subClass = subList.next();
						
						StringUtil.append(content3, "Sub-class ", String.valueOf(classIndex++), "<br>", "<a action=\"bypass -h Aioitem_subclass_6 ", String.valueOf(subClass.getClassIndex()), "\">", ClassListData.getInstance().getClass(subClass.getClassId()).getClientCode(), "</a><br>");
					}
					html.replace("%list%", content3.toString());
				}
				else
				{
					// retail html contain only 3 subclasses
					html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_Modify.htm");
					if (player.getSubClasses().containsKey(1))
					{
						html.replace("%sub1%", ClassListData.getInstance().getClass(player.getSubClasses().get(1).getClassId()).getClientCode());
					}
					else
					{
						html.replace("<a action=\"bypass -h Aioitem_subclass_6 1\">%sub1%</a><br>", "");
					}
					
					if (player.getSubClasses().containsKey(2))
					{
						html.replace("%sub2%", ClassListData.getInstance().getClass(player.getSubClasses().get(2).getClassId()).getClientCode());
					}
					else
					{
						html.replace("<a action=\"bypass -h Aioitem_subclass_6 2\">%sub2%</a><br>", "");
					}
					
					if (player.getSubClasses().containsKey(3))
					{
						html.replace("%sub3%", ClassListData.getInstance().getClass(player.getSubClasses().get(3).getClassId()).getClientCode());
					}
					else
					{
						html.replace("<a action=\"bypass -h Aioitem_subclass_6 3\">%sub3%</a><br>", "");
					}
				}
				break;
			}
			case 4: // Add Subclass - Action (Subclass 4 x[x])
			{
				/**
				 * If the character is less than level 75 on any of their previously chosen classes then disallow them to change to their most recently added sub-class choice.
				 */
				if (!player.getFloodProtectors().getSubclass().tryPerformAction("add subclass"))
				{
					_log.warning(L2VillageMasterInstance.class.getName() + ": Player " + player.getName() + " has performed a subclass change too fast");
					return;
				}
				
				boolean allowAddition = true;
				
				if (player.getTotalSubClasses() >= Config.MAX_SUBCLASS)
				{
					allowAddition = false;
				}
				
				if (player.getLevel() < 75)
				{
					allowAddition = false;
				}
				
				if (allowAddition)
				{
					if (!player.getSubClasses().isEmpty())
					{
						for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
						{
							SubClass subClass = subList.next();
							
							if (subClass.getLevel() < 75)
							{
								allowAddition = false;
								break;
							}
						}
					}
				}
				
				/**
				 * If quest checking is enabled, verify if the character has completed the Mimir's Elixir (Path to Subclass) and Fate's Whisper (A Grade Weapon) quests by checking for instances of their unique reward items. If they both exist, remove both unique items and continue with adding the
				 * sub-class.
				 */
				if (allowAddition && !Config.ALT_GAME_SUBCLASS_WITHOUT_QUESTS)
				{
					allowAddition = checkQuests(player);
				}
				
				if (allowAddition && isValidNewSubClass(player, paramOne))
				{
					if (!player.addSubClass(paramOne, player.getTotalSubClasses() + 1))
					{
						return;
					}
					
					player.setActiveClass(player.getTotalSubClasses());
					
					html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_AddOk.htm");
					
					player.sendPacket(SystemMessageId.ADD_NEW_SUBCLASS); // Subclass added.
				}
				else
				{
					html.setFile(player.getHtmlPrefix(), getSubClassFail());
				}
				break;
			}
			case 5: // Change Class - Action
			{
				/**
				 * If the character is less than level 75 on any of their previously chosen classes then disallow them to change to their most recently added sub-class choice. Note: paramOne = classIndex
				 */
				if (!player.getFloodProtectors().getSubclass().tryPerformAction("change class"))
				{
					_log.warning(L2VillageMasterInstance.class.getName() + ": Player " + player.getName() + " has performed a subclass change too fast");
					return;
				}
				
				if (player.getClassIndex() == paramOne)
				{
				html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_Current.htm");
					break;
				}
				
				if (paramOne == 0)
				{
					if (!checkVillageMaster(player.getBaseClass()))
					{
						return;
					}
				}
				else
				{
					try
					{
						if (!checkVillageMaster(player.getSubClasses().get(paramOne).getClassDefinition()))
						{
							return;
						}
					}
					catch (NullPointerException e)
					{
						return;
					}
				}
				
				player.setActiveClass(paramOne);
				player.sendPacket(SystemMessageId.SUBCLASS_TRANSFER_COMPLETED); // Transfer completed.
				return;
			}
			case 6: // Change/Cancel Subclass - Choice
			{ // validity check
				if ((paramOne < 1) || (paramOne > Config.MAX_SUBCLASS))
				{
					return;
				}
				
				subsAvailable = getAvailableSubClasses(player);
				// another validity check
				if ((subsAvailable == null) || subsAvailable.isEmpty())
				{
					// TODO: Retail message
					player.sendMessage("There are no sub classes available at this time.");
					return;
				}
				
				final StringBuilder content6 = StringUtil.startAppend(200);
				for (PlayerClass subClass : subsAvailable)
				{
					StringUtil.append(content6, "<a action=\"bypass -h Aioitem_subclass_7 ", String.valueOf(paramOne), " ", String.valueOf(subClass.ordinal()), "\" msg=\"1445;", "\">", ClassListData.getInstance().getClass(subClass.ordinal()).getClientCode(), "</a><br>");
				}
				
				switch (paramOne)
				{
					case 1:
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ModifyChoice1.htm");
						break;
					case 2:
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ModifyChoice2.htm");
						break;
					case 3:
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ModifyChoice3.htm");
						break;
					default:
						html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ModifyChoice.htm");
				}
				html.replace("%list%", content6.toString());
				break;
			}
			case 7: // Change Subclass - Action
			{
				/**
				 * Warning: the information about this subclass will be removed from the subclass list even if false!
				 */
				if (!player.getFloodProtectors().getSubclass().tryPerformAction("change class"))
				{
					_log.warning(L2VillageMasterInstance.class.getName() + ": Player " + player.getName() + " has performed a subclass change too fast");
					return;
				}
				
				if (!isValidNewSubClass(player, paramTwo))
				{
					return;
				}
				
				if (player.modifySubClass(paramOne, paramTwo))
				{
					player.abortCast();
					player.stopAllEffectsExceptThoseThatLastThroughDeath(); // all effects from old subclass stopped!
					player.stopAllEffectsNotStayOnSubclassChange();
					player.stopCubics();
					player.setActiveClass(paramOne);
					
					html.setFile(player.getHtmlPrefix(), "data/html/aioitem/subclass/SubClass_ModifyOk.htm");
					html.replace("%name%", ClassListData.getInstance().getClass(paramTwo).getClientCode());
					
					player.sendPacket(SystemMessageId.ADD_NEW_SUBCLASS); // Subclass added.
				}
				else
				{
					/**
					 * This isn't good! modifySubClass() removed subclass from memory we must update _classIndex! Else IndexOutOfBoundsException can turn up some place down the line along with other seemingly unrelated problems.
					 */
					player.setActiveClass(0); // Also updates _classIndex plus switching _classid to baseclass.
					
					player.sendMessage("The sub class could not be added, you have been reverted to your base class.");
					return;
				}
				break;
			}
		}
		html.replace("%objectId%", String.valueOf(player.getObjectId()));
		player.sendPacket(html);
	}
	
	protected String getSubClassMenu(Race pRace)
	{
		if (Config.ALT_GAME_SUBCLASS_EVERYWHERE || (pRace != Race.Kamael))
		{
			return "data/html/aioitem/subclass/SubClass.htm";
		}
		
		return "data/html/aioitem/subclass/SubClass_NoOther.htm";
	}
	
	protected String getSubClassFail()
	{
		return "data/html/aioitem/subclass/SubClass_Fail.htm";
	}
	
	protected boolean checkQuests(L2PcInstance player)
	{
		// Noble players can add Sub-Classes without quests
		if (player.isNoble())
		{
			return true;
		}
		
		QuestState qs = player.getQuestState("234_FatesWhisper");
		if ((qs == null) || !qs.isCompleted())
		{
			return false;
		}
		
		qs = player.getQuestState("235_MimirsElixir");
		if ((qs == null) || !qs.isCompleted())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Returns list of available subclasses Base class and already used subclasses removed
	 * @param player
	 * @return
	 */
	private final Set<PlayerClass> getAvailableSubClasses(L2PcInstance player)
	{
		// get player base class
		final int currentBaseId = player.getBaseClass();
		final ClassId baseCID = ClassId.getClassId(currentBaseId);
		
		// we need 2nd occupation ID
		final int baseClassId;
		if (baseCID.level() > 2)
		{
			baseClassId = baseCID.getParent().ordinal();
		}
		else
		{
			baseClassId = currentBaseId;
		}
		
		/**
		 * If the race of your main class is Elf or Dark Elf, you may not select each class as a subclass to the other class. If the race of your main class is Kamael, you may not subclass any other race If the race of your main class is NOT Kamael, you may not subclass any Kamael class You may not
		 * select Overlord and Warsmith class as a subclass. You may not select a similar class as the subclass. The occupations classified as similar classes are as follows: Treasure Hunter, Plainswalker and Abyss Walker Hawkeye, Silver Ranger and Phantom Ranger Paladin, Dark Avenger, Temple Knight
		 * and Shillien Knight Warlocks, Elemental Summoner and Phantom Summoner Elder and Shillien Elder Swordsinger and Bladedancer Sorcerer, Spellsinger and Spellhowler Also, Kamael have a special, hidden 4 subclass, the inspector, which can only be taken if you have already completed the other
		 * two Kamael subclasses
		 */
		Set<PlayerClass> availSubs = PlayerClass.values()[baseClassId].getAvailableSubclasses(player);
		
		if ((availSubs != null) && !availSubs.isEmpty())
		{
			for (Iterator<PlayerClass> availSub = availSubs.iterator(); availSub.hasNext();)
			{
				PlayerClass pclass = availSub.next();
				
				// check for the village master
				if (!checkVillageMaster(pclass))
				{
					availSub.remove();
					continue;
				}
				
				// scan for already used subclasses
				int availClassId = pclass.ordinal();
				ClassId cid = ClassId.getClassId(availClassId);
				SubClass prevSubClass;
				ClassId subClassId;
			for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
				{
					prevSubClass = subList.next();
					subClassId = ClassId.getClassId(prevSubClass.getClassId());
					
					if (subClassId.equalsOrChildOf(cid))
					{
						availSub.remove();
						break;
					}
				}
			}
		}
		
		return availSubs;
	}
	
	/**
	 * Check new subclass classId for validity (villagemaster race/type, is not contains in previous subclasses, is contains in allowed subclasses) Base class not added into allowed subclasses.
	 * @param player
	 * @param classId
	 * @return
	 */
	private final boolean isValidNewSubClass(L2PcInstance player, int classId)
	{
		if (!checkVillageMaster(classId))
		{
			return false;
		}
		
		final ClassId cid = ClassId.values()[classId];
		SubClass sub;
		ClassId subClassId;
		for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
		{
			sub = subList.next();
			subClassId = ClassId.values()[sub.getClassId()];
			
			if (subClassId.equalsOrChildOf(cid))
			{
				return false;
			}
		}
		
		// get player base class
		final int currentBaseId = player.getBaseClass();
		final ClassId baseCID = ClassId.getClassId(currentBaseId);
		
		// we need 2nd occupation ID
		final int baseClassId;
		if (baseCID.level() > 2)
		{
			baseClassId = baseCID.getParent().ordinal();
		}
		else
		{
			baseClassId = currentBaseId;
		}
		
		Set<PlayerClass> availSubs = PlayerClass.values()[baseClassId].getAvailableSubclasses(player);
		if ((availSubs == null) || availSubs.isEmpty())
		{
			return false;
		}
		
		boolean found = false;
		for (PlayerClass pclass : availSubs)
		{
			if (pclass.ordinal() == classId)
			{
				found = true;
				break;
			}
		}
		return found;
	}
	
	protected boolean checkVillageMasterRace(PlayerClass pclass)
	{
		return true;
	}
	
	protected boolean checkVillageMasterTeachType(PlayerClass pclass)
	{
		return true;
	}
	
	/**
	 * Returns true if this classId allowed for master
	 * @param classId
	 * @return
	 */
	public final boolean checkVillageMaster(int classId)
	{
		return checkVillageMaster(PlayerClass.values()[classId]);
	}
	
	/**
	 * Returns true if this PlayerClass is allowed for master
	 * @param pclass
	 * @return
	 */
	public final boolean checkVillageMaster(PlayerClass pclass)
	{
		if (Config.ALT_GAME_SUBCLASS_EVERYWHERE)
		{
			return true;
		}
		
		return checkVillageMasterRace(pclass) && checkVillageMasterTeachType(pclass);
	}
	
	private static final Iterator<SubClass> iterSubClasses(L2PcInstance player)
	{
		return player.getSubClasses().values().iterator();
	}
}