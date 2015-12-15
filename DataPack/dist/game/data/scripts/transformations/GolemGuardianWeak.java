package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class GolemGuardianWeak extends L2Transformation
{
	private static final int[] SKILLS =
	{
		572,
		573,
		574,
		575,
		5491,
		619
	};
	
	public GolemGuardianWeak()
	{
		// id, colRadius, colHeight
		super(212, 13, 25);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 212) || getPlayer().isCursedWeaponEquipped())
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
		// Double Slasher (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(572, 2), false);
		// Earthquake (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(573, 2), false);
		// Bomb Installation (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(574, 2), false);
		// Steel Cutter (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(575, 2), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Double Slasher (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(572, 2), false);
		// Earthquake (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(573, 2), false);
		// Bomb Installation (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(574, 2), false);
		// Steel Cutter (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(575, 2), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new GolemGuardianWeak());
	}
}
