package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DragonBomberNormal extends L2Transformation
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
	
	public DragonBomberNormal()
	{
		// id, colRadius, colHeight
		super(217, 16, 24);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 217) || getPlayer().isCursedWeaponEquipped())
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
		getPlayer().removeSkill(L2Skill.valueOf(580, 3), false);
		// Sand Cloud (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(581, 3), false);
		// Scope Bleed (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(582, 3), false);
		// Assimilation (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(583, 3), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Death Blow (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(580, 3), false);
		// Sand Cloud (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(581, 3), false);
		// Scope Bleed (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(582, 3), false);
		// Assimilation (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(583, 3), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DragonBomberNormal());
	}
}