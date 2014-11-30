package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class InfernoDrakeStrong extends L2Transformation
{
	private static final int[] SKILLS =
	{
		576,
		577,
		578,
		579,
		5491,
		619
	};
	
	public InfernoDrakeStrong()
	{
		// id, colRadius, colHeight
		super(213, 15, 24);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 213) || getPlayer().isCursedWeaponEquipped())
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
		// Paw Strike (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(576, 4), false);
		// Fire Breath (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(577, 4), false);
		// Blaze Quake (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(578, 4), false);
		// Fire Armor (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(579, 4), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Paw Strike (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(576, 4), false);
		// Fire Breath (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(577, 4), false);
		// Blaze Quake (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(578, 4), false);
		// Fire Armor (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(579, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new InfernoDrakeStrong());
	}
}
