package transformations;

import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DoomWraith extends L2Transformation
{
	private static final int[] SKILLS =
	{
		586,
		587,
		588,
		589,
		5491,
		619
	};
	
	public DoomWraith()
	{
		// id, colRadius, colHeight
		super(2, 13, 25);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 2) || getPlayer().isCursedWeaponEquipped())
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
		// Rolling Attack (up to 2 levels)
		getPlayer().removeSkill(L2Skill.valueOf(586, 2), false);
		// Earth Storm (up to 2 levels)
		getPlayer().removeSkill(L2Skill.valueOf(587, 2), false);
		// Curse of Darkness (up to 2 levels)
		getPlayer().removeSkill(L2Skill.valueOf(588, 2), false);
		// Darkness Energy Drain (up to 2 levels)
		getPlayer().removeSkill(L2Skill.valueOf(589, 2), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Rolling Attack (up to 2 levels)
		getPlayer().addSkill(L2Skill.valueOf(586, 2), false);
		// Earth Storm (up to 2 levels)
		getPlayer().addSkill(L2Skill.valueOf(587, 2), false);
		// Curse of Darkness (up to 2 levels)
		getPlayer().addSkill(L2Skill.valueOf(588, 2), false);
		// Darkness Energy Drain (up to 2 levels)
		getPlayer().addSkill(L2Skill.valueOf(589, 2), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DoomWraith());
	}
}