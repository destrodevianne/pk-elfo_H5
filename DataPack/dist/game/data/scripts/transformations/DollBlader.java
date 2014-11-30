package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DollBlader extends L2Transformation
{
	private static final int[] SKILLS =
	{
		752,
		753,
		754,
		5491,
		619
	};
	
	public DollBlader()
	{
		// id, colRadius, colHeight
		super(7, 6, 12);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 7) || getPlayer().isCursedWeaponEquipped())
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
		// Doll Blader Clairvoyance
		getPlayer().removeSkill(L2Skill.valueOf(754, 1), false, false);
		
		if (getPlayer().getLevel() >= 76)
		{
			// Doll Blader Sting (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(752, 3), false);
			// Doll Blader Throwing Knife (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(753, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Doll Blader Sting (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(752, 2), false);
			// Doll Blader Throwing Knife (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(753, 2), false);
		}
		else
		{
			// Doll Blader Sting (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(752, 1), false);
			// Doll Blader Throwing Knife (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(753, 1), false);
		}
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Doll Blader Clairvoyance
		getPlayer().addSkill(L2Skill.valueOf(754, 1), false);
		
		if (getPlayer().getLevel() >= 76)
		{
			// Doll Blader Sting (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(752, 3), false);
			// Doll Blader Throwing Knife (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(753, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Doll Blader Sting (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(752, 2), false);
			// Doll Blader Throwing Knife (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(753, 2), false);
		}
		else if (getPlayer().getLevel() >= 70)
		{
			// Doll Blader Sting (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(752, 1), false);
			// Doll Blader Throwing Knife (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(753, 1), false);
		}
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DollBlader());
	}
}