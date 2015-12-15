package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DwarfGolem extends L2Transformation
{
	private static final int[] SKILLS =
	{
		806,
		807,
		808,
		809,
		5491,
		619
	};
	
	public DwarfGolem()
	{
		// id, colRadius, colHeight
		super(259, 31, 51.8);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 259) || getPlayer().isCursedWeaponEquipped())
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
		// Magic Obstacle
		getPlayer().removeSkill(L2Skill.valueOf(806, 1), false);
		// Over-hit
		getPlayer().removeSkill(L2Skill.valueOf(807, 1), false);
		// Golem Punch
		getPlayer().removeSkill(L2Skill.valueOf(808, 1), false);
		// Golem Tornado Swing
		getPlayer().removeSkill(L2Skill.valueOf(809, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Magic Obstacle
		getPlayer().addSkill(L2Skill.valueOf(806, 1), false);
		// Over-hit
		getPlayer().addSkill(L2Skill.valueOf(807, 1), false);
		// Golem Punch
		getPlayer().addSkill(L2Skill.valueOf(808, 1), false);
		// Golem Tornado Swing
		getPlayer().addSkill(L2Skill.valueOf(809, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DwarfGolem());
	}
}