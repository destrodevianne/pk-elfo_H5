package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Kamael extends L2Transformation
{
	private static final int[] SKILLS =
	{
		539,
		540,
		1471,
		1472,
		5491,
		619
	};
	
	public Kamael()
	{
		// id, duration (secs), colRadius, colHeight
		super(251, 9, 38);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 251) || getPlayer().isCursedWeaponEquipped())
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
		// Nail Attack
		getPlayer().removeSkill(L2Skill.valueOf(539, 1), false);
		// Wing Assault
		getPlayer().removeSkill(L2Skill.valueOf(540, 1), false);
		// Soul Sucking
		getPlayer().removeSkill(L2Skill.valueOf(1471, 1), false);
		// Death Beam
		getPlayer().removeSkill(L2Skill.valueOf(1472, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Nail Attack
		getPlayer().addSkill(L2Skill.valueOf(539, 1), false);
		// Wing Assault
		getPlayer().addSkill(L2Skill.valueOf(540, 1), false);
		// Soul Sucking
		getPlayer().addSkill(L2Skill.valueOf(1471, 1), false);
		// Death Beam
		getPlayer().addSkill(L2Skill.valueOf(1472, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Kamael());
	}
}
