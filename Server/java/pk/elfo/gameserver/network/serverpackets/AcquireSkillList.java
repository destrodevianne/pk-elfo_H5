package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.base.AcquireSkillType;
import javolution.util.FastList;

public final class AcquireSkillList extends L2GameServerPacket
{
	private final FastList<Skill> _skills;
	private final AcquireSkillType _skillType;
	
	/**
	 * Private class containing learning skill information.
	 */
	private static class Skill
	{
		public int id;
		public int nextLevel;
		public int maxLevel;
		public int spCost;
		public int requirements;
		
		public Skill(int pId, int pNextLevel, int pMaxLevel, int pSpCost, int pRequirements)
		{
			id = pId;
			nextLevel = pNextLevel;
			maxLevel = pMaxLevel;
			spCost = pSpCost;
			requirements = pRequirements;
		}
	}
	
	public AcquireSkillList(AcquireSkillType type)
	{
		_skillType = type;
		_skills = new FastList<>();
	}
	
	public void addSkill(int id, int nextLevel, int maxLevel, int spCost, int requirements)
	{
		_skills.add(new Skill(id, nextLevel, maxLevel, spCost, requirements));
	}
	
	@Override
	protected void writeImpl()
	{
		if (_skills == null)
		{
			return;
		}
		
		writeC(0x90);
		writeD(_skillType.ordinal());
		writeD(_skills.size());
		
		for (Skill temp : _skills)
		{
			writeD(temp.id);
			writeD(temp.nextLevel);
			writeD(temp.maxLevel);
			writeD(temp.spCost);
			writeD(temp.requirements);
			if (_skillType == AcquireSkillType.SubPledge)
			{
				writeD(0); // TODO: ?
			}
		}
	}
}