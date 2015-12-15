package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class UnicornNormal extends L2Transformation
{
	private static final int[] SKILLS =
	{
		563,
		564,
		565,
		567,
		5491,
		619
	};
	
	public UnicornNormal()
	{
		// id, colRadius, colHeight
		super(205, 15, 28);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 205) || getPlayer().isCursedWeaponEquipped())
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
		// Horn of Doom (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(563, 3), false);
		// Gravity Control (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(564, 3), false);
		// Horn Assault (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(565, 3), false);
		// Light of Heal (up to 4 levels)
		getPlayer().removeSkill(L2Skill.valueOf(567, 3), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Horn of Doom (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(563, 3), false);
		// Gravity Control (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(564, 3), false);
		// Horn Assault (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(565, 3), false);
		// Light of Heal (up to 4 levels)
		getPlayer().addSkill(L2Skill.valueOf(567, 3), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new UnicornNormal());
	}
}
