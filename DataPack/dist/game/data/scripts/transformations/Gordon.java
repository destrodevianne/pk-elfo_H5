package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Gordon extends L2Transformation
{
	private static final int[] SKILLS =
	{
		728,
		729,
		730,
		5491,
		619
	};
	
	public Gordon()
	{
		// id, colRadius, colHeight
		super(308, 43, 46.6);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 308) || getPlayer().isCursedWeaponEquipped())
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
		// Gordon Beast Attack
		getPlayer().removeSkill(L2Skill.valueOf(728, 1), false);
		// Gordon Sword Stab
		getPlayer().removeSkill(L2Skill.valueOf(729, 1), false);
		// Gordon Press
		getPlayer().removeSkill(L2Skill.valueOf(730, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Gordon Beast Attack
		getPlayer().addSkill(L2Skill.valueOf(728, 1), false);
		// Gordon Sword Stab
		getPlayer().addSkill(L2Skill.valueOf(729, 1), false);
		// Gordon Press
		getPlayer().addSkill(L2Skill.valueOf(730, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Gordon());
	}
}
