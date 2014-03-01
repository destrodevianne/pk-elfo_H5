package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */
 
public class Negate extends L2Effect
{
	public Negate(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.NEGATE;
	}
	
	@Override
	public boolean onStart()
	{
		L2Skill skill = getSkill();
		
		for (int negateSkillId : skill.getNegateId())
		{
			if (negateSkillId != 0)
			{
				getEffected().stopSkillEffects(negateSkillId);
			}
		}
		for (L2SkillType negateSkillType : skill.getNegateStats())
		{
			getEffected().stopSkillEffects(negateSkillType, skill.getNegateLvl());
		}
		if (skill.getNegateAbnormals() != null)
		{
			for (L2Effect effect : getEffected().getAllEffects())
			{
				if (effect == null)
				{
					continue;
				}
				
				for (String negateAbnormalType : skill.getNegateAbnormals().keySet())
				{
					if (negateAbnormalType.equalsIgnoreCase(effect.getAbnormalType()) && (skill.getNegateAbnormals().get(negateAbnormalType) >= effect.getAbnormalLvl()))
					{
						effect.exit();
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}