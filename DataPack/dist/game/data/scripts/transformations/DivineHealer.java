package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DivineHealer extends L2Transformation
{
	private static final int[] SKILLS =
	{
		648,
		803,
		1490,
		698,
		699,
		700,
		701,
		702,
		703,
		5491,
		619
	};
	
	public DivineHealer()
	{
		// id, colRadius, colHeight
		super(255, 10, 25);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 255) || getPlayer().isCursedWeaponEquipped())
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
		// Divine Healer Major Heal
		getPlayer().removeSkill(L2Skill.valueOf(698, 1), false);
		// Divine Healer Battle Heal
		getPlayer().removeSkill(L2Skill.valueOf(699, 1), false);
		// Divine Healer Group Heal
		getPlayer().removeSkill(L2Skill.valueOf(700, 1), false);
		// Divine Healer Resurrection
		getPlayer().removeSkill(L2Skill.valueOf(701, 1), false);
		// Divine Healer Clans
		getPlayer().removeSkill(L2Skill.valueOf(702, 1), false);
		// Sacrifice Healer
		getPlayer().removeSkill(L2Skill.valueOf(703, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Divine Healer Major Heal
		getPlayer().addSkill(L2Skill.valueOf(698, 1), false);
		// Divine Healer Battle Heal
		getPlayer().addSkill(L2Skill.valueOf(699, 1), false);
		// Divine Healer Group Heal
		getPlayer().addSkill(L2Skill.valueOf(700, 1), false);
		// Divine Healer Resurrection
		getPlayer().addSkill(L2Skill.valueOf(701, 1), false);
		// Divine Healer Cleanse
		getPlayer().addSkill(L2Skill.valueOf(702, 1), false);
		// Sacrifice Healer
		getPlayer().addSkill(L2Skill.valueOf(703, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DivineHealer());
	}
}