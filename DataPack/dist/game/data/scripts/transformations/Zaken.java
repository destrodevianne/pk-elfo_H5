package transformations;

import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Zaken extends L2Transformation
{
	private static final int[] SKILLS =
	{
		715,
		716,
		717,
		718,
		719,
		5491,
		619
	};
	
	public Zaken()
	{
		// id, colRadius, colHeight
		super(305, 16, 32);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 305) || getPlayer().isCursedWeaponEquipped())
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
		// Zaken Energy Drain (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(715, 4), false);
		// Zaken Hold (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(716, 4), false);
		// Zaken Concentrated Attack (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(717, 4), false);
		// Zaken Dancing Sword (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(718, 4), false);
		// Zaken Vampiric Rage
		getPlayer().removeSkill(L2Skill.valueOf(719, 1), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Zaken Energy Drain (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(715, 4), false);
		// Zaken Hold (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(716, 4), false);
		// Zaken Concentrated Attack (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(717, 4), false);
		// Zaken Dancing Sword (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(718, 4), false);
		// Zaken Vampiric Rage
		getPlayer().addSkill(L2Skill.valueOf(719, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Zaken());
	}
}