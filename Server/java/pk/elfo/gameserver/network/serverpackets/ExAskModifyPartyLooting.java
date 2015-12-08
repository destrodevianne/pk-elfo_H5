package pk.elfo.gameserver.network.serverpackets;

public class ExAskModifyPartyLooting extends L2GameServerPacket
{
	private final String _requestor;
	private final byte _mode;
	
	public ExAskModifyPartyLooting(String name, byte mode)
	{
		_requestor = name;
		_mode = mode;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xBF);
		writeS(_requestor);
		writeD(_mode);
	}
}