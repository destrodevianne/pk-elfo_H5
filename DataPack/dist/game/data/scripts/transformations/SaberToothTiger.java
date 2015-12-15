package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class SaberToothTiger extends L2Transformation
{
	private static final int[] SKILLS =
	{
		746,
		747,
		748,
		5491,
		619
	};
	
	public SaberToothTiger()
	{
		// id, colRadius, colHeight
		super(5, 34, 28);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 5) || getPlayer().isCursedWeaponEquipped())
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
			// Saber Tooth Tiger Bite (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(746, 3), false);
			// Saber Tooth Tiger Fear (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(747, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Saber Tooth Tiger Bite (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(746, 2), false);
			// Saber Tooth Tiger Fear (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(747, 2), false);
		}
		else
		{
			// Saber Tooth Tiger Bite (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(746, 1), false);
			// Saber Tooth Tiger Fear (up to 3 levels)
			getPlayer().removeSkill(L2Skill.valueOf(747, 1), false);
		}
		// Saber Tooth Tiger Sprint
		getPlayer().removeSkill(L2Skill.valueOf(748, 1), false, false);
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
			// Saber Tooth Tiger Bite (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(746, 3), false);
			// Saber Tooth Tiger Fear (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(747, 3), false);
		}
		else if (getPlayer().getLevel() >= 73)
		{
			// Saber Tooth Tiger Bite (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(746, 2), false);
			// Saber Tooth Tiger Fear (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(747, 2), false);
		}
		else if (getPlayer().getLevel() >= 70)
		{
			// Saber Tooth Tiger Bite (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(746, 1), false);
			// Saber Tooth Tiger Fear (up to 3 levels)
			getPlayer().addSkill(L2Skill.valueOf(747, 1), false);
		}
		// Saber Tooth Tiger Sprint
		getPlayer().addSkill(L2Skill.valueOf(748, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new SaberToothTiger());
	}
}
