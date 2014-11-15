package pk.elfo.gameserver.network.serverpackets;

public class ExDuelAskStart extends L2GameServerPacket
{
	private final String _requestorName;
	private final int _partyDuel;
	
	public ExDuelAskStart(String requestor, int partyDuel)
	{
		_requestorName = requestor;
		_partyDuel = partyDuel;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x4c);
		
		writeS(_requestorName);
		writeD(_partyDuel);
	}
}