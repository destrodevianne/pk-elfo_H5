package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.zone.ZoneId;

public class EtcStatusUpdate extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	
	public EtcStatusUpdate(L2PcInstance activeChar)
	{
		_activeChar = activeChar;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xf9); // several icons to a separate line (0 = disabled)
		writeD(_activeChar.getCharges()); // 1-7 increase force, lvl
		writeD(_activeChar.getWeightPenalty()); // 1-4 weight penalty, lvl (1=50%, 2=66.6%, 3=80%, 4=100%)
		writeD((_activeChar.getMessageRefusal() || _activeChar.isChatBanned() || _activeChar.isSilenceMode()) ? 1 : 0); // 1 = block all chat
		writeD(_activeChar.isInsideZone(ZoneId.DANGER_AREA) ? 1 : 0); // 1 = danger area
		writeD(_activeChar.getExpertiseWeaponPenalty()); // Weapon Grade Penalty [1-4]
		writeD(_activeChar.getExpertiseArmorPenalty()); // Armor Grade Penalty [1-4]
		writeD(_activeChar.isAffected(EffectFlag.CHARM_OF_COURAGE) ? 1 : 0); // 1 = charm of courage (allows resurrection on the same spot upon death on the siege battlefield)
		writeD(_activeChar.getDeathPenaltyBuffLevel()); // 1-15 death penalty, lvl (combat ability decreased due to death)
		writeD(_activeChar.getSouls());
	}
}