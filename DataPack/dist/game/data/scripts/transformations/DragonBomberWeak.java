package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DragonBomberWeak extends L2Transformation
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
	
	public DragonBomberWeak()
	{
		// id, colRadius, colHeight
		super(218, 16, 24);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 218) || getPlayer().isCursedWeaponEquipped())
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
		getPlayer().removeSkill(L2Skill.valueOf(580, 2), false);
		// Sand Cloud (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(581, 2), false);
		// Scope Bleed (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(582, 2), false);
		// Assimilation (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(583, 2), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Death Blow (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(580, 2), false);
		// Sand Cloud (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(581, 2), false);
		// Scope Bleed (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(582, 2), false);
		// Assimilation (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(583, 2), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DragonBomberWeak());
	}
}