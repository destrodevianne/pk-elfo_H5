package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.PartyMatchRoom;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Mode:
 * <ul>
 * <li>0 - add</li>
 * <li>1 - modify</li>
 * <li>2 - quit</li>
 * </ul>
 */
public class ExManagePartyRoomMember extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	private final PartyMatchRoom _room;
	private final int _mode;
	
	public ExManagePartyRoomMember(L2PcInstance player, PartyMatchRoom room, int mode)
	{
		_activeChar = player;
		_room = room;
		_mode = mode;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x0A);
		writeD(_mode);
		writeD(_activeChar.getObjectId());
		writeS(_activeChar.getName());
		writeD(_activeChar.getActiveClass());
		writeD(_activeChar.getLevel());
		writeD(0x00); // TODO: Closes town
		if (_room.getOwner().equals(_activeChar))
		{
			writeD(1);
		}
		else
		{
			if ((_room.getOwner().isInParty() && _activeChar.isInParty()) && (_room.getOwner().getParty().getLeaderObjectId() == _activeChar.getParty().getLeaderObjectId()))
			{
				writeD(0x02);
			}
			else
			{
				writeD(0x00);
			}
		}
	}
}