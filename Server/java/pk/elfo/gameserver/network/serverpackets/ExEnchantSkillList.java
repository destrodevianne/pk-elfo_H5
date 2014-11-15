package pk.elfo.gameserver.network.serverpackets;

import java.util.List;

import javolution.util.FastList;

public class ExEnchantSkillList extends L2GameServerPacket
{
	public enum EnchantSkillType
	{
		NORMAL,
		SAFE,
		UNTRAIN,
		CHANGE_ROUTE,
	}
	
	private final EnchantSkillType _type;
	private final List<Skill> _skills;
	
	static class Skill
	{
		public int id;
		public int nextLevel;
		
		Skill(int pId, int pNextLevel)
		{
			id = pId;
			nextLevel = pNextLevel;
		}
	}
	
	public void addSkill(int id, int level)
	{
		_skills.add(new Skill(id, level));
	}
	
	public ExEnchantSkillList(EnchantSkillType type)
	{
		_type = type;
		_skills = new FastList<>();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x29);
		
		writeD(_type.ordinal());
		writeD(_skills.size());
		for (Skill sk : _skills)
		{
			writeD(sk.id);
			writeD(sk.nextLevel);
		}
	}
}