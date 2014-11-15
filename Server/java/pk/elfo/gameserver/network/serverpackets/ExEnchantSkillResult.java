package pk.elfo.gameserver.network.serverpackets;

public class ExEnchantSkillResult extends L2GameServerPacket
{
	private static final ExEnchantSkillResult STATIC_PACKET_TRUE = new ExEnchantSkillResult(true);
	private static final ExEnchantSkillResult STATIC_PACKET_FALSE = new ExEnchantSkillResult(false);
	
	public static final ExEnchantSkillResult valueOf(boolean result)
	{
		return result ? STATIC_PACKET_TRUE : STATIC_PACKET_FALSE;
	}
	
	private final boolean _enchanted;
	
	public ExEnchantSkillResult(boolean enchanted)
	{
		_enchanted = enchanted;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xA7);
		writeD(_enchanted ? 1 : 0);
	}
}