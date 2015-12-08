package pk.elfo.gameserver.network.serverpackets;

public class AskJoinParty extends L2GameServerPacket
{
	private final String _requestorName;
	private final int _itemDistribution;
	
	/**
	 * @param requestorName
	 * @param itemDistribution
	 */
	public AskJoinParty(String requestorName, int itemDistribution)
	{
		_requestorName = requestorName;
		_itemDistribution = itemDistribution;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x39);
		writeS(_requestorName);
		writeD(_itemDistribution);
	}
}