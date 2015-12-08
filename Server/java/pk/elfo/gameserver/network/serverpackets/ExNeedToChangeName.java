package pk.elfo.gameserver.network.serverpackets;

/**
 * Dialog with input field<br>
 * type 0 = char name (Selection screen)<br>
 * type 1 = clan name
 */
public class ExNeedToChangeName extends L2GameServerPacket
{
	private final int _type, _subType;
	private final String _name;
	
	public ExNeedToChangeName(int type, int subType, String name)
	{
		super();
		_type = type;
		_subType = subType;
		_name = name;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x69);
		writeD(_type);
		writeD(_subType);
		writeS(_name);
	}
}