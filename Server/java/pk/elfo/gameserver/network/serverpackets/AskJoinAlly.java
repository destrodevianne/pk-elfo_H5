package pk.elfo.gameserver.network.serverpackets;

public class AskJoinAlly extends L2GameServerPacket
{
	private final String _requestorName;
	private final int _requestorObjId;
	
	/**
	 * @param requestorObjId
	 * @param requestorName
	 */
	public AskJoinAlly(int requestorObjId, String requestorName)
	{
		_requestorName = requestorName;
		_requestorObjId = requestorObjId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xbb);
		writeD(_requestorObjId);
		writeS(_requestorName);
	}
}