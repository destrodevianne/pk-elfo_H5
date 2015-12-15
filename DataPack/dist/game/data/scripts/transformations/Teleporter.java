package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Teleporter extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5656, 5657, 5658, 5659, 619
	};
	
	public Teleporter()
	{
		// id, colRadius, colHeight
		super(319, 8, 25);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 319) || getPlayer().isCursedWeaponEquipped())
		{
			return;
		}
		
		transformedSkills();
	}
	
	public void transformedSkills()
	{
		updateSkills();
		
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	@Override
	public void onUntransform()
	{
		removeSkills();
	}
	
	public void removeSkills()
	{
		final int level = getPlayer().getLevel();
		
		// Gatekeeper Aura Flare
		getPlayer().removeSkill(L2Skill.valueOf(5656, level), false);
		// Gatekeeper Prominence
		getPlayer().removeSkill(L2Skill.valueOf(5657, level), false);
		// Gatekeeper Flame Strike
		getPlayer().removeSkill(L2Skill.valueOf(5658, level), false);
		// Gatekeeper Berserker Spirit
		if (level >= 35 & level < 52)
		{
			getPlayer().removeSkill(L2Skill.valueOf(5659, 1), false);
		}
		else if (level >= 52)
		{
			getPlayer().removeSkill(L2Skill.valueOf(5659, 2), false);
		}
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	@Override
	public void onLevelUp()
	{
		updateSkills();
	}

	private void updateSkills()
	{
		final int level = getPlayer().getLevel();
		
		// Gatekeeper Aura Flare
		getPlayer().addSkill(L2Skill.valueOf(5656, level), false);
		// Gatekeeper Prominence
		getPlayer().addSkill(L2Skill.valueOf(5657, level), false);
		// Gatekeeper Flame Strike
		getPlayer().addSkill(L2Skill.valueOf(5658, level), false);
		// Gatekeeper Berserker Spirit
		if (level >= 35 & level < 52)
		{
			getPlayer().addSkill(L2Skill.valueOf(5659, 1), false);
		}
		else if (level >= 52)
		{
			getPlayer().addSkill(L2Skill.valueOf(5659, 2), false);
		}
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Teleporter());
	}
}
