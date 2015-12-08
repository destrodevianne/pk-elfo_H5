package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExCubeGameAddPlayer extends L2GameServerPacket
{
	L2PcInstance _player;
	boolean _isRedTeam;
	
	/**
	 * Add Player To Minigame Waiting List
	 * @param player Player Instance
	 * @param isRedTeam Is Player from Red Team?
	 */
	public ExCubeGameAddPlayer(L2PcInstance player, boolean isRedTeam)
	{
		_player = player;
		_isRedTeam = isRedTeam;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x97);
		writeD(0x01);
		
		writeD(0xffffffff);
		
		writeD(_isRedTeam ? 0x01 : 0x00);
		writeD(_player.getObjectId());
		writeS(_player.getName());
	}
}