package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

/*
 * TODO: Skill levels. How do they work? Transformation is given at level 83, there are 6 levels of the skill. How are they assigned? Based on player level somehow? Based on servitor?
 */
public class DemonRace extends L2Transformation
{
	private static final int[] SKILLS =
	{
		901,
		902,
		903,
		904,
		905,
		5491,
		619
	};
	
	public DemonRace()
	{
		// id, colRadius, colHeight
		super(221, 11, 27);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 221) || getPlayer().isCursedWeaponEquipped())
		{
			return;
		}
		
		if (getPlayer().hasSummon())
		{
			getPlayer().getSummon().unSummon(getPlayer());
		}
		transformedSkills();
	}
	
	@Override
	public void onUntransform()
	{
		removeSkills();
	}
	
	public void removeSkills()
	{
		// Dark Strike
		getPlayer().removeSkill(L2Skill.valueOf(901, 4), false);
		// Bursting Flame
		getPlayer().removeSkill(L2Skill.valueOf(902, 4), false);
		// Stratum Explosion
		getPlayer().removeSkill(L2Skill.valueOf(903, 4), false);
		// Corpse Burst
		getPlayer().removeSkill(L2Skill.valueOf(904, 4), false);
		// Dark Detonation
		getPlayer().removeSkill(L2Skill.valueOf(905, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Dark Strike (up to 6)
		getPlayer().addSkill(L2Skill.valueOf(901, 4), false);
		// Bursting Flame (up to 6)
		getPlayer().addSkill(L2Skill.valueOf(902, 4), false);
		// Stratum Explosion (up to 6)
		getPlayer().addSkill(L2Skill.valueOf(903, 4), false);
		// Corpse Burst (up to 6)
		getPlayer().addSkill(L2Skill.valueOf(904, 4), false);
		// Dark Detonation (up to 6)
		getPlayer().addSkill(L2Skill.valueOf(905, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DemonRace());
	}
}