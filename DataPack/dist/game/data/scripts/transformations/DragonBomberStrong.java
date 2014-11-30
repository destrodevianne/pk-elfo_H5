package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DragonBomberStrong extends L2Transformation
{
	private static final int[] SKILLS =
	{
		580,
		581,
		582,
		583,
		5491,
		619
	};
	
	public DragonBomberStrong()
	{
		// id, colRadius, colHeight
		super(216, 16, 24);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 216) || getPlayer().isCursedWeaponEquipped())
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
		// Death Blow (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(580, 4), false);
		// Sand Cloud (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(581, 4), false);
		// Scope Bleed (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(582, 4), false);
		// Assimilation (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(583, 4), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Death Blow (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(580, 4), false);
		// Sand Cloud (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(581, 4), false);
		// Scope Bleed (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(582, 4), false);
		// Assimilation (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(583, 4), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DragonBomberStrong());
	}
}