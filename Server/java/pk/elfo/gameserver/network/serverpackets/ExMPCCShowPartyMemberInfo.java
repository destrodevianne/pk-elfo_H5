package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExMPCCShowPartyMemberInfo extends L2GameServerPacket
{
	private final L2Party _party;
	
	public ExMPCCShowPartyMemberInfo(L2Party party)
	{
		_party = party;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x4B);
		writeD(_party.getMemberCount());
		for (L2PcInstance pc : _party.getMembers())
		{
			writeS(pc.getName());
			writeD(pc.getObjectId());
			writeD(pc.getClassId().getId());
		}
	}
}