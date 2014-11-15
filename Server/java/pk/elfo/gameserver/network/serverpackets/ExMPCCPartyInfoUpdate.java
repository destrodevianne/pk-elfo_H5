package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.L2Party;

public class ExMPCCPartyInfoUpdate extends L2GameServerPacket
{
	private final L2Party _party;
	private final int _mode, _LeaderOID, _memberCount;
	private final String _name;
	
	/**
	 * @param party
	 * @param mode 0 = Remove, 1 = Add
	 */
	public ExMPCCPartyInfoUpdate(L2Party party, int mode)
	{
		_party = party;
		_name = _party.getLeader().getName();
		_LeaderOID = _party.getLeaderObjectId();
		_memberCount = _party.getMemberCount();
		_mode = mode;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x5B);
		writeS(_name);
		writeD(_LeaderOID);
		writeD(_memberCount);
		writeD(_mode); // mode 0 = Remove Party, 1 = AddParty, maybe more...
	}
}