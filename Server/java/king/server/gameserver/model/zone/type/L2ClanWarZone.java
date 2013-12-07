package king.server.gameserver.model.zone.type;

import king.server.gameserver.datatables.SkillTable;
import king.server.gameserver.instancemanager.MapRegionManager;
import king.server.gameserver.model.actor.L2Character;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.skills.L2Skill;
import king.server.gameserver.model.zone.L2ZoneType;
import king.server.gameserver.model.zone.ZoneId;

/**
 *
 * A Clan War Zone
 *
 */

public class L2ClanWarZone extends L2ZoneType
	{
		public L2ClanWarZone(int id)
		{
			super(id);
		}
		L2Skill noblesse = SkillTable.getInstance().getInfo(1323, 1);

		@Override
		protected void onEnter(L2Character character)
		{
			if (character instanceof L2PcInstance)
			{
				L2PcInstance activeChar = ((L2PcInstance) character);
				if(((L2PcInstance)character).getClan() != null)
				{         
					character.setInsideZone(ZoneId.CLANWAR, true);
					((L2PcInstance)character).sendMessage("Voce entrou em uma zona de guerra de clan. Prepare-se para luta.");
					noblesse.getEffects(activeChar, activeChar);
					if(activeChar.getPvpFlag() == 0)
						activeChar.updatePvPFlag(1);
				}
				else
				{
					((L2PcInstance) character).sendMessage("Esta area e restrita somente a membros dse clan. Voce sera teletransportado para a cidade mais proxima.");
					((L2PcInstance) character).teleToLocation(MapRegionManager.TeleportWhereType.Town);
				}
			}
		}

		@Override
		protected void onExit(L2Character character)
		{
			character.setInsideZone(ZoneId.CLANWAR, false);
			if (character instanceof L2PcInstance)
			{
				L2PcInstance activeChar = ((L2PcInstance) character);
				activeChar.stopPvPFlag();
			}
		}

		@Override
		public void onDieInside(L2Character character)
		{
		}

		@Override
		public void onReviveInside(L2Character character)
		{
			onEnter(character);
			if (character instanceof L2PcInstance)
			{
				L2PcInstance activeChar = ((L2PcInstance) character);
				noblesse.getEffects(activeChar, activeChar);
				heal(activeChar);
			}
		}

		static void heal(L2PcInstance activeChar)
		{
			activeChar.setCurrentHp(activeChar.getMaxHp());
			activeChar.setCurrentCp(activeChar.getMaxCp());
			activeChar.setCurrentMp(activeChar.getMaxMp());
		}

	}