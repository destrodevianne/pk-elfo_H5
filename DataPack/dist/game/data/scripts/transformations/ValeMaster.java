package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class ValeMaster extends L2Transformation
{
	private static final int[] SKILLS =
	{
		742,
		743,
		744,
		745,
		5491,
		619
	};
	
	public ValeMaster()
	{
		// id, colRadius, colHeight
		super(4, 12, 40);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 4) || getPlayer().isCursedWeaponEquipped())
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
			// Vale Master Bursting Flame (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(742, 3), false);
			// Vale Master Dark Explosion (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(743, 3), false);
			// Vale Master Dark Flare (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(744, 3), false);
			// Vale Master Dark Cure (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(745, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Vale Master Bursting Flame (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(742, 2), false);
			// Vale Master Dark Explosion (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(743, 2), false);
			// Vale Master Dark Flare (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(744, 2), false);
			// Vale Master Dark Cure (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(745, 2), false);
		}
		else
		{
			// Vale Master Bursting Flame (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(742, 1), false);
			// Vale Master Dark Explosion (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(743, 1), false);
			// Vale Master Dark Flare (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(744, 1), false);
			// Vale Master Dark Cure (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(745, 1), false);
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
			// Vale Master Bursting Flame (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(742, 3), false);
			// Vale Master Dark Explosion (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(743, 3), false);
			// Vale Master Dark Flare (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(744, 3), false);
			// Vale Master Dark Cure (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(745, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Vale Master Bursting Flame (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(742, 2), false);
			// Vale Master Dark Explosion (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(743, 2), false);
			// Vale Master Dark Flare (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(744, 2), false);
			// Vale Master Dark Cure (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(745, 2), false);
		}
		else if (getPlayer().getLevel() >= 70)
		{
			// Vale Master Bursting Flame (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(742, 1), false);
			// Vale Master Dark Explosion (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(743, 1), false);
			// Vale Master Dark Flare (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(744, 1), false);
			// Vale Master Dark Cure (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(745, 1), false);
		}
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new ValeMaster());
	}
}
