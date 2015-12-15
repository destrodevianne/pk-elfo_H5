package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Heretic extends L2Transformation
{
	private static final int[] SKILLS =
	{
		738,
		739,
		740,
		741,
		5491,
		619
	};
	
	public Heretic()
	{
		// id, colRadius, colHeight
		super(3, 7.7, 28.4);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 3) || getPlayer().isCursedWeaponEquipped())
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
			// Heretic Heal (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(738, 3), false);
			// Heretic Battle Heal (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(739, 3), false);
			// Heretic Resurrection (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(740, 3), false);
			// Heretic Heal Side Effect (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(741, 3), false, false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Heretic Heal (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(738, 2), false);
			// Heretic Battle Heal (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(739, 2), false);
			// Heretic Resurrection (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(740, 2), false);
			// Heretic Heal Side Effect (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(741, 2), false, false);
		}
		else
		{
			// Heretic Heal (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(738, 1), false);
			// Heretic Battle Heal (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(739, 1), false);
			// Heretic Resurrection (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(740, 1), false);
			// Heretic Heal Side Effect (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(741, 1), false, false);
		}
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
			// Heretic Heal (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(738, 3), false);
			// Heretic Battle Heal (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(739, 3), false);
			// Heretic Resurrection (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(740, 3), false);
			// Heretic Heal Side Effect (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(741, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Heretic Heal (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(738, 2), false);
			// Heretic Battle Heal (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(739, 2), false);
			// Heretic Resurrection (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(740, 2), false);
			// Heretic Heal Side Effect (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(741, 2), false);
		}
		else if (getPlayer().getLevel() >= 70)
		{
			// Heretic Heal (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(738, 1), false);
			// Heretic Battle Heal (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(739, 1), false);
			// Heretic Resurrection (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(740, 1), false);
			// Heretic Heal Side Effect (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(741, 1), false);
		}
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Heretic());
	}
}
