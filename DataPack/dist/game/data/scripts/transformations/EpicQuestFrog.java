package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class EpicQuestFrog extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5437,
		959
	};
	
	public EpicQuestFrog()
	{
		// id, colRadius, colHeight
		super(111, 20, 10);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 111) || getPlayer().isCursedWeaponEquipped())
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
		// Frog Jump
		getPlayer().removeSkill(L2Skill.valueOf(959, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Dissonance
		getPlayer().addSkill(L2Skill.valueOf(5437, 1), false);
		// Frog Jump
		getPlayer().addSkill(L2Skill.valueOf(959, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new EpicQuestFrog());
	}
}