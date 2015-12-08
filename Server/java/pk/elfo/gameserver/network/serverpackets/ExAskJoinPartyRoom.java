package pk.elfo.gameserver.network.serverpackets;

public class ExAskJoinPartyRoom extends L2GameServerPacket
{
	private final String _charName;
	
	public ExAskJoinPartyRoom(String charName)
	{
		_charName = charName;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x35);
		writeS(_charName);
	}
}