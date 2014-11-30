package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Scarecrow extends L2Transformation
{
	private static final int[] SKILLS =
	{
		940,
		942,
		5437,
		619
	};
	
	public Scarecrow()
	{
		// id, colRadius, colHeight
		super(115, 13, 30);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 115) || getPlayer().isCursedWeaponEquipped())
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
		// Fake Attack
		getPlayer().removeSkill(L2Skill.valueOf(940, 1), false);
		// Special Motion
		getPlayer().removeSkill(L2Skill.valueOf(942, 1), false);
		// Dissonance
		getPlayer().removeSkill(L2Skill.valueOf(5437, 2), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Fake Attack
		getPlayer().addSkill(L2Skill.valueOf(940, 1), false);
		// Special Motion
		getPlayer().addSkill(L2Skill.valueOf(942, 1), false);
		// Dissonance
		getPlayer().addSkill(L2Skill.valueOf(5437, 2), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Scarecrow());
	}
}
