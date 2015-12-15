package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class OlMahum extends L2Transformation
{
	private static final int[] SKILLS =
	{
		749,
		750,
		751,
		5491,
		619
	};
	
	public OlMahum()
	{
		// id, colRadius, colHeight
		super(6, 23, 61);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 6) || getPlayer().isCursedWeaponEquipped())
		{
			return;
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
		if (getPlayer().getLevel() >= 76)
		{
			// Oel Mahum Stun Attack (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(749, 3), false);
			// Oel Mahum Arm Flourish (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(751, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Oel Mahum Stun Attack (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(749, 2), false);
			// Oel Mahum Arm Flourish (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(751, 2), false);
		}
		else if (getPlayer().getLevel() >= 70)
		{
			// Oel Mahum Stun Attack (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(749, 1), false);
			// Oel Mahum Arm Flourish (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(751, 1), false);
		}
		// Oel Mahum Ultimate Defense
		getPlayer().removeSkill(L2Skill.valueOf(750, 1), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		if (getPlayer().getLevel() >= 76)
		{
			// Oel Mahum Stun Attack (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(749, 3), false);
			// Oel Mahum Ultimate Defense
			getPlayer().addSkill(L2Skill.valueOf(750, 1), false);
			// Oel Mahum Arm Flourish (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(751, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Oel Mahum Stun Attack (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(749, 2), false);
			// Oel Mahum Ultimate Defense
			getPlayer().addSkill(L2Skill.valueOf(750, 1), false);
			// Oel Mahum Arm Flourish (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(751, 2), false);
		}
		else if (getPlayer().getLevel() >= 70)
		{
			// Oel Mahum Stun Attack (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(749, 1), false);
			// Oel Mahum Ultimate Defense
			getPlayer().addSkill(L2Skill.valueOf(750, 1), false);
			// Oel Mahum Arm Flourish (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(751, 1), false);
		}
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new OlMahum());
	}
}
