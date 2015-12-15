package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DemonPrince extends L2Transformation
{
	private static final int[] SKILLS =
	{
		735,
		736,
		737,
		5491,
		619
	};
	
	public DemonPrince()
	{
		// id, colRadius, colHeight
		super(311, 33, 49);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 311) || getPlayer().isCursedWeaponEquipped())
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
		// Devil Spinning Weapon
		getPlayer().removeSkill(L2Skill.valueOf(735, 1), false);
		// Devil Seed
		getPlayer().removeSkill(L2Skill.valueOf(736, 1), false);
		// Devil Ultimate Defense
		getPlayer().removeSkill(L2Skill.valueOf(737, 1), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Devil Spinning Weapon
		getPlayer().addSkill(L2Skill.valueOf(735, 1), false);
		// Devil Seed
		getPlayer().addSkill(L2Skill.valueOf(736, 1), false);
		// Devil Ultimate Defense
		getPlayer().addSkill(L2Skill.valueOf(737, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DemonPrince());
	}
}