package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class JetBike extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5491,
		839
	};
	
	public JetBike()
	{
		// id, colRadius, colHeight
		super(20001, 21.5, 27);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 20001) || getPlayer().isCursedWeaponEquipped())
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
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Dismount
		getPlayer().removeSkill(L2Skill.valueOf(839, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Dismount
		getPlayer().addSkill(L2Skill.valueOf(839, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new JetBike());
	}
}
