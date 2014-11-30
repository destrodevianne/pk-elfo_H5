package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DivineEnchanter extends L2Transformation
{
	private static final int[] SKILLS =
	{
		704,
		705,
		706,
		707,
		708,
		709,
		5779,
		619
	};
	
	public DivineEnchanter()
	{
		// id, colRadius, colHeight
		super(257, 8, 18.25);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 257) || getPlayer().isCursedWeaponEquipped())
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
		// Divine Enchanter Water Spirit
		getPlayer().removeSkill(L2Skill.valueOf(704, 1), false, false);
		// Divine Enchanter Fire Spirit
		getPlayer().removeSkill(L2Skill.valueOf(705, 1), false, false);
		// Divine Enchanter Wind Spirit
		getPlayer().removeSkill(L2Skill.valueOf(706, 1), false, false);
		// Divine Enchanter Hero Spirit
		getPlayer().removeSkill(L2Skill.valueOf(707, 1), false, false);
		// Divine Enchanter Mass Binding
		getPlayer().removeSkill(L2Skill.valueOf(708, 1), false, false);
		// Sacrifice Enchanter
		getPlayer().removeSkill(L2Skill.valueOf(709, 1), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Divine Enchanter Water Spirit
		getPlayer().addSkill(L2Skill.valueOf(704, 1), false);
		// Divine Enchanter Fire Spirit
		getPlayer().addSkill(L2Skill.valueOf(705, 1), false);
		// Divine Enchanter Wind Spirit
		getPlayer().addSkill(L2Skill.valueOf(706, 1), false);
		// Divine Enchanter Hero Spirit
		getPlayer().addSkill(L2Skill.valueOf(707, 1), false);
		// Divine Enchanter Mass Binding
		getPlayer().addSkill(L2Skill.valueOf(708, 1), false);
		// Sacrifice Enchanter
		getPlayer().addSkill(L2Skill.valueOf(709, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DivineEnchanter());
	}
}