package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Kadomas extends L2Transformation
{
	private static final int[] SKILLS =
	{
		23154,
		619
	};
	
	public Kadomas()
	{
		// id, colRadius, colHeight
		super(20000, 24.5, 14);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 20000) || getPlayer().isCursedWeaponEquipped())
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
		// Kadomas Special Skill - Fireworks
		getPlayer().removeSkill(L2Skill.valueOf(23154, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Kadomas Special Skill - Fireworks
		getPlayer().addSkill(L2Skill.valueOf(23154, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Kadomas());
	}
}
