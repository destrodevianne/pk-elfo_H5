package pk.elfo.gameserver.network.serverpackets;

public class ExAskCoupleAction extends L2GameServerPacket
{
	private final int _charObjId;
	private final int _actionId;
	
	public ExAskCoupleAction(int charObjId, int social)
	{
		_charObjId = charObjId;
		_actionId = social;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xBB);
		writeD(_actionId);
		writeD(_charObjId);
	}
}