package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class EpicQuestNative extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5437,
		961
	};
	
	public EpicQuestNative()
	{
		// id, colRadius, colHeight
		super(124, 8, 23.5);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 124) || getPlayer().isCursedWeaponEquipped())
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
		// Swift Dash
		getPlayer().removeSkill(L2Skill.valueOf(961, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Dissonance
		getPlayer().addSkill(L2Skill.valueOf(5437, 1), false);
		// Swift Dash
		getPlayer().addSkill(L2Skill.valueOf(961, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new EpicQuestNative());
	}
}
