package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class GuardianStrider extends L2Transformation
{
	private static final int[] SKILLS =
	{
		839
	};
	
	public GuardianStrider()
	{
		// id, colRadius, colHeight
		super(123, 13, 40);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 123) || getPlayer().isCursedWeaponEquipped())
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
		// Dismount
		getPlayer().removeSkill(L2Skill.valueOf(839, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Dismount
		getPlayer().addSkill(L2Skill.valueOf(839, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new GuardianStrider());
	}
}
