package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class SnowKung extends L2Transformation
{
	private static final int[] SKILLS =
	{
		940,
		943,
		5437,
		619
	};
	
	public SnowKung()
	{
		// id, colRadius, colHeight
		super(114, 28, 30);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 114) || getPlayer().isCursedWeaponEquipped())
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
		getPlayer().removeSkill(L2Skill.valueOf(943, 1), false);
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
		getPlayer().addSkill(L2Skill.valueOf(943, 1), false);
		// Dissonance
		getPlayer().addSkill(L2Skill.valueOf(5437, 2), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new SnowKung());
	}
}
