package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class InfernoDrakeWeak extends L2Transformation
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
	
	public InfernoDrakeWeak()
	{
		// id, colRadius, colHeight
		super(215, 15, 24);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 215) || getPlayer().isCursedWeaponEquipped())
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
		getPlayer().removeSkill(L2Skill.valueOf(576, 2), false);
		// Fire Breath (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(577, 2), false);
		// Blaze Quake (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(578, 2), false);
		// Fire Armor (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(579, 2), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Paw Strike (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(576, 2), false);
		// Fire Breath (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(577, 2), false);
		// Blaze Quake (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(578, 2), false);
		// Fire Armor (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(579, 2), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new InfernoDrakeWeak());
	}
}
