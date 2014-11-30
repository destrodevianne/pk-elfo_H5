package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class VanguardDarkAvenger extends L2Transformation
{
	public VanguardDarkAvenger()
	{
		// id
		super(313);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 313) || getPlayer().isCursedWeaponEquipped())
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
		int lvl = 1;
		if (getPlayer().getLevel() > 42)
		{
			lvl = (getPlayer().getLevel() - 42);
		}
		
		// Dual Weapon Mastery
		getPlayer().removeSkill(L2Skill.valueOf(144, lvl), false);
		// Blade Hurricane
		getPlayer().removeSkill(L2Skill.valueOf(815, lvl), false);
		// Double Strike
		getPlayer().removeSkill(L2Skill.valueOf(817, lvl), false);
		// Switch Stance
		getPlayer().removeSkill(L2Skill.valueOf(838, 1), false);
		// Boost Morale
		getPlayer().removeSkill(L2Skill.valueOf(956, lvl), false, false);
		// Triple Blade Slash
		getPlayer().removeSkill(L2Skill.valueOf(958, lvl), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		int lvl = 1;
		if (getPlayer().getLevel() > 42)
		{
			lvl = (getPlayer().getLevel() - 42);
		}
		
		// Dual Weapon Mastery
		getPlayer().addSkill(L2Skill.valueOf(144, lvl), false);
		// Blade Hurricane
		getPlayer().addSkill(L2Skill.valueOf(815, lvl), false);
		// Double Strike
		getPlayer().addSkill(L2Skill.valueOf(817, lvl), false);
		// Boost Morale
		getPlayer().addSkill(L2Skill.valueOf(956, lvl), false);
		// Triple Blade Slash
		getPlayer().addSkill(L2Skill.valueOf(958, lvl), false);
		// Switch Stance
		getPlayer().addSkill(L2Skill.valueOf(838, 1), false);
		// Set allowed skills
		getPlayer().setTransformAllowedSkills(new int[]
		{
			18,
			28,
			65,
			86,
			144,
			283,
			815,
			817,
			838,
			956,
			958,
			401
		});
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new VanguardDarkAvenger());
	}
}
