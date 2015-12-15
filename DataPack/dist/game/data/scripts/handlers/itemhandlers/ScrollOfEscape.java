package handlers.itemhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.GameTimeController;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.ClanHallManager;
import pk.elfo.gameserver.instancemanager.MapRegionManager;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.network.serverpackets.SetupGauge;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */

public class ScrollOfEscape implements IItemHandler
{
	// all the items ids that this handler knowns
	private static int[] _itemIds =
	{
		736,
		1830,
		1829,
		1538,
		3958,
		5858,
		5859,
		7117,
		7118,
		7119,
		7120,
		7121,
		7122,
		7123,
		7124,
		7125,
		7126,
		7127,
		7128,
		7129,
		7130,
		7131,
		7132,
		7133,
		7134,
		7554,
		7555,
		7556,
		7557,
		7558,
		7559
	};
	
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceuse)
	{
		if (!(playable instanceof L2PcInstance))
		{
			return false;
		}
		
		L2PcInstance activeChar = (L2PcInstance) playable;
		
		if (checkConditions(activeChar))
		{
			return false;
		}
		
		if (activeChar.isSitting())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.CANT_MOVE_SITTING));
			return false;
		}
		
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT));
			return false;
		}
		
		// Check to see if the player is in a festival.
		if (activeChar.isFestivalParticipant())
		{
			activeChar.sendPacket(SystemMessage.sendString("You may not use an escape skill in a festival."));
			return false;
		}
		
		// Check to see if player is in jail
		if (activeChar.isInJail())
		{
			activeChar.sendPacket(SystemMessage.sendString("You can not escape from jail."));
			return false;
		}
		
		if ((activeChar.getPvpFlag() != 0) && !Config.PVP_NO_ESCAPE)
		{
			if ((item.getItemId() == 1538) || (item.getItemId() == 5859) || (item.getItemId() == 5858))
			{
				activeChar.sendMessage("Desculpe, voce nao pode usar isso em modo PvP!");
				return false;
			}
		}
		
		// activeChar.abortCast();
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		// SoE Animation section
		activeChar.setTarget(activeChar);
		
		// Modified by Tempy - 28 Jul 05 \\
		// Check if this is a blessed scroll, if it is then shorten the cast time.
		int itemId = item.getItemId();
		int escapeSkill = ((itemId == 1538) || (itemId == 5858) || (itemId == 5859) || (itemId == 3958)) ? 2036 : 2013;
		
		if (!activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
		{
			return false;
		}
		
		activeChar.disableAllSkills();
		
		L2Skill skill = SkillTable.getInstance().getInfo(escapeSkill, 1);
		MagicSkillUse msu = new MagicSkillUse(activeChar, escapeSkill, 1, skill.getHitTime(), 0);
		activeChar.broadcastPacket(msu);
		SetupGauge sg = new SetupGauge(0, skill.getHitTime());
		activeChar.sendPacket(sg);
		// End SoE Animation section
		
		SystemMessage sm = new SystemMessage(SystemMessageId.S1_DISAPPEARED);
		sm.addItemName(itemId);
		activeChar.sendPacket(sm);
		
		EscapeFinalizer ef = new EscapeFinalizer(activeChar, itemId);
		// continue execution later
		activeChar.setSkillCast(ThreadPoolManager.getInstance().scheduleEffect(ef, skill.getHitTime()));
		activeChar.forceIsCasting(10 + GameTimeController.getGameTicks() + (skill.getHitTime() / GameTimeController.MILLIS_IN_TICK));
		return false;
	}
	
	static class EscapeFinalizer implements Runnable
	{
		private final L2PcInstance _activeChar;
		private final int _itemId;
		
		EscapeFinalizer(L2PcInstance activeChar, int itemId)
		{
			_activeChar = activeChar;
			_itemId = itemId;
		}
		
		@Override
		public void run()
		{
			if (_activeChar.isDead())
			{
				return;
			}
			_activeChar.enableAllSkills();
			
			_activeChar.setIsIn7sDungeon(false);
			
			try
			{
				if (((_itemId == 1830) || (_itemId == 5859)) && (CastleManager.getInstance().getCastleByOwner(_activeChar.getClan()) != null)) // escape to castle if own's one
				{
					_activeChar.teleToLocation(MapRegionManager.TeleportWhereType.Castle);
				}
				else if (((_itemId == 1829) || (_itemId == 5858)) && (_activeChar.getClan() != null) && (ClanHallManager.getInstance().getClanHallByOwner(_activeChar.getClan()) != null)) // escape to clan hall if own's one
				{
					_activeChar.teleToLocation(MapRegionManager.TeleportWhereType.ClanHall);
				}
				else
				{
					if (_itemId < 7117)
					{
						_activeChar.teleToLocation(MapRegionManager.TeleportWhereType.Town);
					}
					else
					{
						switch (_itemId)
						{
							case 7117:
								_activeChar.teleToLocation(-84318, 244579, -3730); // Talking Island
								break;
							case 7554:
								_activeChar.teleToLocation(-84318, 244579, -3730); // Talking Island quest scroll
								break;
							case 7118:
								_activeChar.teleToLocation(46934, 51467, -2977); // Elven Village
								break;
							case 7555:
								_activeChar.teleToLocation(46934, 51467, -2977); // Elven Village quest scroll
								break;
							case 7119:
								_activeChar.teleToLocation(9745, 15606, -4574); // Dark Elven Village
								break;
							case 7556:
								_activeChar.teleToLocation(9745, 15606, -4574); // Dark Elven Village quest scroll
								break;
							case 7120:
								_activeChar.teleToLocation(-44836, -112524, -235); // Orc Village
								break;
							case 7557:
								_activeChar.teleToLocation(-44836, -112524, -235); // Orc Village quest scroll
								break;
							case 7121:
								_activeChar.teleToLocation(115113, -178212, -901); // Dwarven Village
								break;
							case 7558:
								_activeChar.teleToLocation(115113, -178212, -901); // Dwarven Village quest scroll
								break;
							case 7122:
								_activeChar.teleToLocation(-80826, 149775, -3043); // Gludin Village
								break;
							case 7123:
								_activeChar.teleToLocation(-12678, 122776, -3116); // Gludio Castle Town
								break;
							case 7124:
								_activeChar.teleToLocation(15670, 142983, -2705); // Dion Castle Town
								break;
							case 7125:
								_activeChar.teleToLocation(17836, 170178, -3507); // Floran
								break;
							case 7126:
								_activeChar.teleToLocation(83400, 147943, -3404); // Giran Castle Town
								break;
							case 7559:
								_activeChar.teleToLocation(83400, 147943, -3404); // Giran Castle Town quest scroll
								break;
							case 7127:
								_activeChar.teleToLocation(105918, 109759, -3207); // Hardin's Private Academy
								break;
							case 7128:
								_activeChar.teleToLocation(111409, 219364, -3545); // Heine
								break;
							case 7129:
								_activeChar.teleToLocation(82956, 53162, -1495); // Oren Castle Town
								break;
							case 7130:
								_activeChar.teleToLocation(85348, 16142, -3699); // Ivory Tower
								break;
							case 7131:
								_activeChar.teleToLocation(116819, 76994, -2714); // Hunters Village
								break;
							case 7132:
								_activeChar.teleToLocation(146331, 25762, -2018); // Aden Castle Town
								break;
							case 7133:
								_activeChar.teleToLocation(147928, -55273, -2734); // Goddard Castle Town
								break;
							case 7134:
								_activeChar.teleToLocation(43799, -47727, -798); // Rune Castle Town
								break;
							case 7135:
								// _activeChar.teleToLocation(,,); // Schuttgart Castle Town
								break;
							default:
								_activeChar.teleToLocation(MapRegionManager.TeleportWhereType.Town);
								break;
						}
					}
				}
			}
			catch (Throwable e)
			{
				if (Config.DEBUG)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private static boolean checkConditions(L2PcInstance actor)
	{
		return actor.isStunned() || actor.isSleeping() || actor.isParalyzed() || actor.isFakeDeath() || actor.isTeleporting() || actor.isMuted() || actor.isAlikeDead() || actor.isAllSkillsDisabled() || actor.isCastingNow();
	}
	
	public int[] getItemIds()
	{
		return _itemIds;
	}
}