package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.events.EventsInterface;
import pk.elfo.gameserver.instancemanager.CHSiegeManager;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.instancemanager.TerritoryWarManager;
import pk.elfo.gameserver.model.L2AccessLevel;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.L2SiegeClan;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.entity.TvTRoundEvent;
import pk.elfo.gameserver.model.entity.clanhall.SiegableHall;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone1;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone2;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone3;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone4;
import pk.elfo.gameserver.model.zone.type.L2MultiFunctionZone5;

public class Die extends L2GameServerPacket
{
	private final int _charObjId;
	private boolean _canTeleport;
	private boolean _sweepable;
	private L2AccessLevel _access = AdminTable.getInstance().getAccessLevel(0);
	private L2Clan _clan;
	private final L2Character _activeChar;
	private boolean _isJailed;
	
	/**
	 * @param cha
	 */
	public Die(L2Character cha)
	{
		_activeChar = cha;
		if (cha.isPlayer())
		{
			L2PcInstance player = (L2PcInstance) cha;
			_access = player.getAccessLevel();
			_clan = player.getClan();
			_isJailed = player.isInJail();
			
		}
		_charObjId = cha.getObjectId();
		_canTeleport = !((cha.isPlayer() && ((TvTEvent.isStarted() && TvTEvent.isPlayerParticipant(_charObjId)) || (TvTRoundEvent.isStarted() && TvTRoundEvent.isPlayerParticipant(_charObjId)))) || cha.isPendingRevive()) || (cha.isInsideZone(ZoneId.MULTI_FUNCTION) && L2MultiFunctionZone.revive) || (cha.isInsideZone(ZoneId.MULTI_FUNCTION1) && L2MultiFunctionZone1.revive1) || (cha.isInsideZone(ZoneId.MULTI_FUNCTION2) && L2MultiFunctionZone2.revive2) || (cha.isInsideZone(ZoneId.MULTI_FUNCTION3) && L2MultiFunctionZone3.revive3) || (cha.isInsideZone(ZoneId.MULTI_FUNCTION4) && L2MultiFunctionZone4.revive4) || (cha.isInsideZone(ZoneId.MULTI_FUNCTION5) && L2MultiFunctionZone5.revive5);
		
		if (cha instanceof L2PcInstance)
		{
			if (EventsInterface.isParticipating(cha.getObjectId()))
			{
				_canTeleport = false;
			}
		}
		
		if (cha.isL2Attackable())
		{
			_sweepable = ((L2Attackable) cha).isSweepActive();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x00);
		writeD(_charObjId);
		writeD(_canTeleport ? 0x01 : 0);
		if (_canTeleport && (_clan != null) && !_isJailed)
		{
			boolean isInCastleDefense = false;
			boolean isInFortDefense = false;
			
			L2SiegeClan siegeClan = null;
			Castle castle = CastleManager.getInstance().getCastle(_activeChar);
			Fort fort = FortManager.getInstance().getFort(_activeChar);
			SiegableHall hall = CHSiegeManager.getInstance().getNearbyClanHall(_activeChar);
			if ((castle != null) && castle.getSiege().getIsInProgress())
			{
				// siege in progress
				siegeClan = castle.getSiege().getAttackerClan(_clan);
				if ((siegeClan == null) && castle.getSiege().checkIsDefender(_clan))
				{
					isInCastleDefense = true;
				}
			}
			else if ((fort != null) && fort.getSiege().getIsInProgress())
			{
				// siege in progress
				siegeClan = fort.getSiege().getAttackerClan(_clan);
				if ((siegeClan == null) && fort.getSiege().checkIsDefender(_clan))
				{
					isInFortDefense = true;
				}
			}
			
			writeD(_clan.getHideoutId() > 0 ? 0x01 : 0x00); // 6d 01 00 00 00 - to hide away
			writeD((_clan.getCastleId() > 0) || isInCastleDefense ? 0x01 : 0x00); // 6d 02 00 00 00 - to castle
			writeD((TerritoryWarManager.getInstance().getHQForClan(_clan) != null) || ((siegeClan != null) && !isInCastleDefense && !isInFortDefense && !siegeClan.getFlag().isEmpty()) || ((hall != null) && hall.getSiege().checkIsAttacker(_clan)) ? 0x01 : 0x00); // 6d 03 00 00 00 - to siege HQ
			writeD(_sweepable ? 0x01 : 0x00); // sweepable (blue glow)
			writeD(_access.allowFixedRes() ? 0x01 : 0x00); // 6d 04 00 00 00 - to FIXED
			writeD((_clan.getFortId() > 0) || isInFortDefense ? 0x01 : 0x00); // 6d 05 00 00 00 - to fortress
		}
		else
		{
			writeD(0x00); // 6d 01 00 00 00 - to hide away
			writeD(0x00); // 6d 02 00 00 00 - to castle
			writeD(0x00); // 6d 03 00 00 00 - to siege HQ
			writeD(_sweepable ? 0x01 : 0x00); // sweepable (blue glow)
			writeD(_access.allowFixedRes() ? 0x01 : 0x00); // 6d 04 00 00 00 - to FIXED
			writeD(0x00); // 6d 05 00 00 00 - to fortress
		}
		// TODO: protocol 152
		//@formatter:off
		/*
		 * writeC(0); //show die animation 
		 * writeD(0); //agathion ress button 
		 * writeD(0); //additional free space
		 */
		//@formatter:on
	}
}