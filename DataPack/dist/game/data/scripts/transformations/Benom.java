package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Benom extends L2Transformation
{
	private static final int[] SKILLS = new int[]
	{
		725,
		726,
		727,
		5491,
		619
	};
	
	public Benom()
	{
		// id, colRadius, colHeight
		super(307, 20, 56);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 307) || getPlayer().isCursedWeaponEquipped())
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
		// Benom Power Smash
		getPlayer().removeSkill(L2Skill.valueOf(725, 1), false);
		// Benom Sonic Storm
		getPlayer().removeSkill(L2Skill.valueOf(726, 1), false);
		// Benom Disillusion
		getPlayer().removeSkill(L2Skill.valueOf(727, 1), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Benom Power Smash
		getPlayer().addSkill(L2Skill.valueOf(725, 1), false);
		// Benom Sonic Storm
		getPlayer().addSkill(L2Skill.valueOf(726, 1), false);
		// Benom Disillusion
		getPlayer().addSkill(L2Skill.valueOf(727, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Benom());
	}
}