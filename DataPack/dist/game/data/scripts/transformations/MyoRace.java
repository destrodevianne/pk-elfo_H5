package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

/*
 * TODO: Skill levels. How do they work? Transformation is given at level 83, there are 6 levels of the skill. How are they assigned? Based on player level somehow? Based on servitor?
 */
public class MyoRace extends L2Transformation
{
	private static final int[] SKILLS =
	{
		896,
		897,
		898,
		899,
		900,
		5491,
		619
	};
	
	public MyoRace()
	{
		// id, colRadius, colHeight
		super(219, 10, 23);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 219) || getPlayer().isCursedWeaponEquipped())
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
		// Rolling Step (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(896, 4), false);
		// Double Blast (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(897, 4), false);
		// Tornado Slash (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(898, 4), false);
		// Cat Roar (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(899, 4), false);
		// Energy Blast (up to 6)
		getPlayer().removeSkill(L2Skill.valueOf(900, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Rolling Step (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(896, 4), false);
		// Double Blast (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(897, 4), false);
		// Tornado Slash (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(898, 4), false);
		// Cat Roar (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(899, 4), false);
		// Energy Blast (up to 6)
		getPlayer().addSkill(L2Skill.valueOf(900, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new MyoRace());
	}
}
