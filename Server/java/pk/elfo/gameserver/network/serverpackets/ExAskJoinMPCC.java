package pk.elfo.gameserver.network.serverpackets;

/**
 * Asks the player to join a CC
 */
public class ExAskJoinMPCC extends L2GameServerPacket
{
	private final String _requestorName;
	
	/**
	 * @param requestorName
	 */
	public ExAskJoinMPCC(String requestorName)
	{
		_requestorName = requestorName;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1a);
		writeS(_requestorName); // name of CCLeader
	}
}