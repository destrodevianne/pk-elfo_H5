package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class GrailApostleNormal extends L2Transformation
{
	private static final int[] SKILLS =
	{
		559,
		560,
		561,
		562,
		5491,
		619
	};
	
	public GrailApostleNormal()
	{
		// id, colRadius, colHeight
		super(202, 10, 35);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 202) || getPlayer().isCursedWeaponEquipped())
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
		// Spear (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(559, 3), false);
		// Power Slash (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(560, 3), false);
		// Bless of Angel (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(561, 3), false, false);
		// Wind of Angel (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(562, 3), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Spear (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(559, 3), false);
		// Power Slash (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(560, 3), false);
		// Bless of Angel (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(561, 3), false);
		// Wind of Angel (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(562, 3), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new GrailApostleNormal());
	}
}
