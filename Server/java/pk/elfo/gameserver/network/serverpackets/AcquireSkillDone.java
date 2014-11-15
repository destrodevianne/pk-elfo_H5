package pk.elfo.gameserver.network.serverpackets;

public class AcquireSkillDone extends L2GameServerPacket
{
	public AcquireSkillDone()
	{
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x94);
	}
}