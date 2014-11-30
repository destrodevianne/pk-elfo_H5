package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class EpicQuestChild extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5437,
		960
	};
	
	public EpicQuestChild()
	{
		// id, colRadius, colHeight
		super(112, 5, 12.3);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 112) || getPlayer().isCursedWeaponEquipped())
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
		// Race Running
		getPlayer().removeSkill(L2Skill.valueOf(960, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Dissonance
		getPlayer().addSkill(L2Skill.valueOf(5437, 1), false);
		// Race Running
		getPlayer().addSkill(L2Skill.valueOf(960, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new EpicQuestChild());
	}
}