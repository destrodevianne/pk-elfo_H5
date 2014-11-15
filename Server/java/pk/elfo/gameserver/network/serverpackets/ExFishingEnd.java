package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExFishingEnd extends L2GameServerPacket
{
	private final boolean _win;
	private final L2Character _activeChar;
	
	public ExFishingEnd(boolean win, L2PcInstance character)
	{
		_win = win;
		_activeChar = character;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1F);
		writeD(_activeChar.getObjectId());
		writeC(_win ? 1 : 0);
	}
}