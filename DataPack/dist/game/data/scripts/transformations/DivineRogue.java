package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DivineRogue extends L2Transformation
{
	private static final int[] SKILLS =
	{
		686,
		687,
		688,
		689,
		690,
		691,
		797,
		5491,
		619
	};
	
	public DivineRogue()
	{
		// id, colRadius, colHeight
		super(254, 10, 28);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 254) || getPlayer().isCursedWeaponEquipped())
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
		// Divine Rogue Stun Shot
		getPlayer().removeSkill(L2Skill.valueOf(686, 1), false);
		// Divine Rogue Double Shot
		getPlayer().removeSkill(L2Skill.valueOf(687, 1), false);
		// Divine Rogue Bleed Attack
		getPlayer().removeSkill(L2Skill.valueOf(688, 1), false);
		// Divine Rogue Deadly Blow
		getPlayer().removeSkill(L2Skill.valueOf(689, 1), false);
		// Divine Rogue Agility
		getPlayer().removeSkill(L2Skill.valueOf(690, 1), false, false);
		// Sacrifice Rogue
		getPlayer().removeSkill(L2Skill.valueOf(691, 1), false, false);
		// Divine Rogue Piercing Attack
		getPlayer().removeSkill(L2Skill.valueOf(797, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Divine Rogue Stun Shot
		getPlayer().addSkill(L2Skill.valueOf(686, 1), false);
		// Divine Rogue Double Shot
		getPlayer().addSkill(L2Skill.valueOf(687, 1), false);
		// Divine Rogue Bleed Attack
		getPlayer().addSkill(L2Skill.valueOf(688, 1), false);
		// Divine Rogue Deadly Blow
		getPlayer().addSkill(L2Skill.valueOf(689, 1), false);
		// Divine Rogue Agility
		getPlayer().addSkill(L2Skill.valueOf(690, 1), false);
		// Sacrifice Rogue
		getPlayer().addSkill(L2Skill.valueOf(691, 1), false);
		// Divine Rogue Piercing Attack
		getPlayer().addSkill(L2Skill.valueOf(797, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DivineRogue());
	}
}