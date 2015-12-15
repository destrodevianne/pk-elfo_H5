package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DivineKnight extends L2Transformation
{
	private static final int[] SKILLS =
	{
		680,
		681,
		682,
		683,
		684,
		685,
		795,
		796,
		5491,
		619
	};
	
	public DivineKnight()
	{
		// id, colRadius, colHeight
		super(252, 16, 30);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 252) || getPlayer().isCursedWeaponEquipped())
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
		// Divine Knight Hate
		getPlayer().removeSkill(L2Skill.valueOf(680, 1), false);
		// Divine Knight Hate Aura
		getPlayer().removeSkill(L2Skill.valueOf(681, 1), false);
		// Divine Knight Stun Attack
		getPlayer().removeSkill(L2Skill.valueOf(682, 1), false);
		// Divine Knight Thunder Storm
		getPlayer().removeSkill(L2Skill.valueOf(683, 1), false);
		// Divine Knight Ultimate Defense
		getPlayer().removeSkill(L2Skill.valueOf(684, 1), false, false);
		// Sacrifice Knight
		getPlayer().removeSkill(L2Skill.valueOf(685, 1), false, false);
		// Divine Knight Brandish
		getPlayer().removeSkill(L2Skill.valueOf(795, 1), false);
		// Divine Knight Explosion
		getPlayer().removeSkill(L2Skill.valueOf(796, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Divine Knight Hate
		getPlayer().addSkill(L2Skill.valueOf(680, 1), false);
		// Divine Knight Hate Aura
		getPlayer().addSkill(L2Skill.valueOf(681, 1), false);
		// Divine Knight Stun Attack
		getPlayer().addSkill(L2Skill.valueOf(682, 1), false);
		// Divine Knight Thunder Storm
		getPlayer().addSkill(L2Skill.valueOf(683, 1), false);
		// Divine Knight Ultimate Defense
		getPlayer().addSkill(L2Skill.valueOf(684, 1), false);
		// Sacrifice Knight
		getPlayer().addSkill(L2Skill.valueOf(685, 1), false);
		// Divine Knight Brandish
		getPlayer().addSkill(L2Skill.valueOf(795, 1), false);
		// Divine Knight Explosion
		getPlayer().addSkill(L2Skill.valueOf(796, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DivineKnight());
	}
}