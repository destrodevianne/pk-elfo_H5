package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DivineWarrior extends L2Transformation
{
	private static final int[] SKILLS =
	{
		675,
		676,
		677,
		678,
		679,
		798,
		5491,
		619
	};
	
	public DivineWarrior()
	{
		// id, colRadius, colHeight
		super(253, 14.5, 29);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 253) || getPlayer().isCursedWeaponEquipped())
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
		// Cross Slash
		getPlayer().removeSkill(L2Skill.valueOf(675, 1), false);
		// Sonic Blaster
		getPlayer().removeSkill(L2Skill.valueOf(676, 1), false);
		// Transfixition of Earth
		getPlayer().removeSkill(L2Skill.valueOf(677, 1), false);
		// Divine Warrior War Cry
		getPlayer().removeSkill(L2Skill.valueOf(678, 1), false, false);
		// Sacrifice Warrior
		getPlayer().removeSkill(L2Skill.valueOf(679, 1), false, false);
		// Divine Warrior Assault Attack
		getPlayer().removeSkill(L2Skill.valueOf(798, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Cross Slash
		getPlayer().addSkill(L2Skill.valueOf(675, 1), false);
		// Sonic Blaster
		getPlayer().addSkill(L2Skill.valueOf(676, 1), false);
		// Transfixition of Earth
		getPlayer().addSkill(L2Skill.valueOf(677, 1), false);
		// Divine Warrior War Cry
		getPlayer().addSkill(L2Skill.valueOf(678, 1), false);
		// Sacrifice Warrior
		getPlayer().addSkill(L2Skill.valueOf(679, 1), false);
		// Divine Warrior Assault Attack
		getPlayer().addSkill(L2Skill.valueOf(798, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DivineWarrior());
	}
}