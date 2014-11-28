package pk.elfo.gameserver.model.holders;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.skills.L2Skill;

/**
 * Classe simples para armazenar skill id/level.
 */
public class SkillHolder
{
	private final int _skillId;
	private final int _skillLvl;
	
	public SkillHolder(int skillId, int skillLvl)
	{
		_skillId = skillId;
		_skillLvl = skillLvl;
	}
	
	public SkillHolder(L2Skill skill)
	{
		_skillId = skill.getId();
		_skillLvl = skill.getLevel();
	}
	
	public final int getSkillId()
	{
		return _skillId;
	}
	
	public final int getSkillLvl()
	{
		return _skillLvl;
	}
	
	public final L2Skill getSkill()
	{
		return SkillTable.getInstance().getInfo(_skillId, _skillLvl);
	}
	
	@Override
	public String toString()
	{
		return "[SkillId: " + _skillId + " Level: " + _skillLvl + "]";
	}
}