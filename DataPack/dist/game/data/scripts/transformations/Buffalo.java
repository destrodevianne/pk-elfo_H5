package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Buffalo extends L2Transformation
{
	private static final int[] SKILLS = new int[]
	{
		5437,
		619
	};
	
	public Buffalo()
	{
		// id, colRadius, colHeight
		super(103, 22, 31);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 103) || getPlayer().isCursedWeaponEquipped())
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
		// Dissonance
		getPlayer().removeSkill(L2Skill.valueOf(5437, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Dissonance
		getPlayer().addSkill(L2Skill.valueOf(5437, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Buffalo());
	}
}