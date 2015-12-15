package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Kiyachi extends L2Transformation
{
	private static final int[] SKILLS =
	{
		733,
		734,
		5491,
		619
	};
	
	public Kiyachi()
	{
		// id, colRadius, colHeight
		super(310, 12, 29);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 310) || getPlayer().isCursedWeaponEquipped())
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
		// Kechi Double Cutter
		getPlayer().removeSkill(L2Skill.valueOf(733, 1), false);
		// Kechi Air Blade
		getPlayer().removeSkill(L2Skill.valueOf(734, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Kechi Double Cutter
		getPlayer().addSkill(L2Skill.valueOf(733, 1), false);
		// Kechi Air Blade
		getPlayer().addSkill(L2Skill.valueOf(734, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Kiyachi());
	}
}
