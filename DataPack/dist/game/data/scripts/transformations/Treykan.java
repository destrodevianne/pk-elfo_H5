package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Treykan extends L2Transformation
{
	private static final int[] SKILLS = new int[]
	{
		619,
		967,
		968,
		969,
		5437
	};
	
	public Treykan()
	{
		// id, colRadius, colHeight
		super(126, 25, 27.00);
	}
	
	@Override
	public void onTransform()
	{
		if (getPlayer().getTransformationId() != 126 || getPlayer().isCursedWeaponEquipped())
			return;
		
		transformedSkills();
	}
	
	@Override
	public void onUntransform()
	{
		removeSkills();
	}
	
	public void removeSkills()
	{
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		// Cursed Body
		getPlayer().removeSkill(L2Skill.valueOf(967, 1), false);
		// Treykan Claw
		getPlayer().removeSkill(L2Skill.valueOf(968, 1), false);
		// Treykan Dash
		getPlayer().removeSkill(L2Skill.valueOf(969, 1), false);
		// Dissonance
		getPlayer().removeSkill(L2Skill.valueOf(5437, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		// Cursed Body
		getPlayer().addSkill(L2Skill.valueOf(967, 1), false);
		// Treykan Claw
		getPlayer().addSkill(L2Skill.valueOf(968, 1), false);
		// Treykan Dash
		getPlayer().addSkill(L2Skill.valueOf(969, 1), false);
		// Dissonance
		getPlayer().addSkill(L2Skill.valueOf(5437, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Treykan());
	}
}
