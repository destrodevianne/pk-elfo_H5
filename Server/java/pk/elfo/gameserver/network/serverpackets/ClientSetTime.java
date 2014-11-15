package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.GameTimeController;

public class ClientSetTime extends L2GameServerPacket
{
	public static final ClientSetTime STATIC_PACKET = new ClientSetTime();
	
	private ClientSetTime()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xf2);
		writeD(GameTimeController.getInstance().getGameTime()); // time in client minutes
		writeD(6); // constant to match the server time( this determines the speed of the client clock)
	}
}