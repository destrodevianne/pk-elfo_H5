package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

/*
 * TODO: Skill levels. How do they work? Transformation is given at level 83, there are 6 levels of the skill. How are they assigned? Based on player level somehow? Based on servitor?
 */
public class Unicorniun extends L2Transformation
{
	private static final int[] SKILLS =
	{
		906,
		907,
		908,
		909,
		910,
		5491,
		619
	};
	
	public Unicorniun()
	{
		// id, colRadius, colHeight
		super(220, 8, 30);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 220) || getPlayer().isCursedWeaponEquipped())
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
		// Lance Step (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(906, 4), false);
		// Aqua Blast (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(907, 4), false);
		// Spin Slash (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(908, 4), false);
		// Ice Focus (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(909, 4), false);
		// Water Jet (up to 6 levels)
		getPlayer().removeSkill(L2Skill.valueOf(910, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Lance Step (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(906, 4), false);
		// Aqua Blast (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(907, 4), false);
		// Spin Slash (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(908, 4), false);
		// Ice Focus (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(909, 4), false);
		// Water Jet (up to 6 levels)
		getPlayer().addSkill(L2Skill.valueOf(910, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Unicorniun());
	}
}
