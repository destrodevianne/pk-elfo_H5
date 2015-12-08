package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExBrExtraUserInfo extends L2GameServerPacket
{
	private final int _charObjId;
	private final int _val;
	
	public ExBrExtraUserInfo(L2PcInstance player)
	{
		_charObjId = player.getObjectId();
		_val = player.getEventEffectId();
		_invisible = player.getAppearance().getInvisible();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0xDA);
		writeD(_charObjId); // object ID of Player
		writeD(_val); // event effect id
		// writeC(0x00); // Event flag, added only if event is active
	}
}