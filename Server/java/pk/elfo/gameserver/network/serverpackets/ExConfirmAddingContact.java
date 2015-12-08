package pk.elfo.gameserver.network.serverpackets;

public class ExConfirmAddingContact extends L2GameServerPacket
{
	private final String _charName;
	private final boolean _added;
	
	public ExConfirmAddingContact(String charName, boolean added)
	{
		_charName = charName;
		_added = added;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xD2);
		writeS(_charName);
		writeD(_added ? 0x01 : 0x00);
	}
}