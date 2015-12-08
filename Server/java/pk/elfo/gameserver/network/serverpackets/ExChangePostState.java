package pk.elfo.gameserver.network.serverpackets;

public class ExChangePostState extends L2GameServerPacket
{
	private final boolean _receivedBoard;
	private final int[] _changedMsgIds;
	private final int _changeId;
	
	public ExChangePostState(boolean receivedBoard, int[] changedMsgIds, int changeId)
	{
		_receivedBoard = receivedBoard;
		_changedMsgIds = changedMsgIds;
		_changeId = changeId;
	}
	
	public ExChangePostState(boolean receivedBoard, int changedMsgId, int changeId)
	{
		_receivedBoard = receivedBoard;
		_changedMsgIds = new int[]
		{
			changedMsgId
		};
		_changeId = changeId;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0xb3);
		writeD(_receivedBoard ? 1 : 0);
		writeD(_changedMsgIds.length);
		for (int postId : _changedMsgIds)
		{
			writeD(postId); // postId
			writeD(_changeId); // state
		}
	}
}