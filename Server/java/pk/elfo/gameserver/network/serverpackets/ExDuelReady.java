package pk.elfo.gameserver.network.serverpackets;

public class ExDuelReady extends L2GameServerPacket
{
	private final int _unk1;
	
	public ExDuelReady(int unk1)
	{
		_unk1 = unk1;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x4D);
		
		writeD(_unk1);
	}
}