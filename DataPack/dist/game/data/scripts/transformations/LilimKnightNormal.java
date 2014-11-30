package transformations;

import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class LilimKnightNormal extends L2Transformation
{
	private static final int[] SKILLS =
	{
		568,
		569,
		570,
		571,
		5491,
		619
	};
	
	public LilimKnightNormal()
	{
		// id, colRadius, colHeight
		super(208, 12, 25.5);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 208) || getPlayer().isCursedWeaponEquipped())
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
		// Attack Buster (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(568, 3), false);
		// Attack Storm (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(569, 3), false);
		// Attack Rage (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(570, 3), false);
		// Poison Dust (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(571, 3), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Attack Buster (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(568, 3), false);
		// Attack Storm (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(569, 3), false);
		// Attack Rage (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(570, 3), false);
		// Poison Dust (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(571, 3), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new LilimKnightNormal());
	}
}
