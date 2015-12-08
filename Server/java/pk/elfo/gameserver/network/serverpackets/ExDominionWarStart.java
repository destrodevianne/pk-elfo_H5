package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.instancemanager.TerritoryWarManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExDominionWarStart extends L2GameServerPacket
{
	private final int _objId;
	private final int _terId;
	private final boolean _isDisguised;
	
	public ExDominionWarStart(L2PcInstance player)
	{
		_objId = player.getObjectId();
		_terId = TerritoryWarManager.getInstance().getRegisteredTerritoryId(player);
		_isDisguised = TerritoryWarManager.getInstance().isDisguised(_objId);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xA3);
		writeD(_objId);
		writeD(0x01); // ??
		writeD(_terId);
		writeD(_isDisguised ? 1 : 0);
		writeD(_isDisguised ? _terId : 0);
	}
}