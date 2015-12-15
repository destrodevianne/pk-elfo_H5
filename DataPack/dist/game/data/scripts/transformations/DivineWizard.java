package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DivineWizard extends L2Transformation
{
	private static final int[] SKILLS =
	{
		692,
		693,
		694,
		695,
		696,
		697,
		5491,
		619
	};
	
	public DivineWizard()
	{
		// id, colRadius, colHeight
		super(256, 10, 26);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 256) || getPlayer().isCursedWeaponEquipped())
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
		// Divine Wizard Holy Flare
		getPlayer().removeSkill(L2Skill.valueOf(692, 1), false);
		// Divine Wizard Holy Strike
		getPlayer().removeSkill(L2Skill.valueOf(693, 1), false);
		// Divine Wizard Holy Curtain
		getPlayer().removeSkill(L2Skill.valueOf(694, 1), false);
		// Divine Wizard Holy Cloud
		getPlayer().removeSkill(L2Skill.valueOf(695, 1), false);
		// Divine Wizard Surrender to Holy
		getPlayer().removeSkill(L2Skill.valueOf(696, 1), false);
		// Sacrifice Wizard
		getPlayer().removeSkill(L2Skill.valueOf(697, 1), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Divine Wizard Holy Flare
		getPlayer().addSkill(L2Skill.valueOf(692, 1), false);
		// Divine Wizard Holy Strike
		getPlayer().addSkill(L2Skill.valueOf(693, 1), false);
		// Divine Wizard Holy Curtain
		getPlayer().addSkill(L2Skill.valueOf(694, 1), false);
		// Divine Wizard Holy Cloud
		getPlayer().addSkill(L2Skill.valueOf(695, 1), false);
		// Divine Wizard Surrender to Holy
		getPlayer().addSkill(L2Skill.valueOf(696, 1), false);
		// Sacrifice Wizard
		getPlayer().addSkill(L2Skill.valueOf(697, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DivineWizard());
	}
}