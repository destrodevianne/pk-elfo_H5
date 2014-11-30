package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Anakim extends L2Transformation
{
	private static final int[] SKILLS = new int[]
	{
		720,
		721,
		722,
		723,
		724,
		5491,
		619
	};
	
	public Anakim()
	{
		// id, colRadius, colHeight
		super(306, 15.5, 29);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 306) || getPlayer().isCursedWeaponEquipped())
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
		// Anakim Holy Light Burst (up to 2 levels)
		getPlayer().removeSkill(L2Skill.valueOf(720, 2), false);
		// Anakim Energy Attack (up to 2 levels)
		getPlayer().removeSkill(L2Skill.valueOf(721, 2), false);
		// Anakim Holy Beam (up to 2 levels)
		getPlayer().removeSkill(L2Skill.valueOf(722, 2), false);
		// Anakim Sunshine
		getPlayer().removeSkill(L2Skill.valueOf(723, 1), false);
		// Anakim Cleanse
		getPlayer().removeSkill(L2Skill.valueOf(724, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Anakim Holy Light Burst (up to 2 levels)
		getPlayer().addSkill(L2Skill.valueOf(720, 2), false);
		// Anakim Energy Attack (up to 2 levels)
		getPlayer().addSkill(L2Skill.valueOf(721, 2), false);
		// Anakim Holy Beam (up to 2 levels)
		getPlayer().addSkill(L2Skill.valueOf(722, 2), false);
		// Anakim Sunshine
		getPlayer().addSkill(L2Skill.valueOf(723, 1), false);
		// Anakim Cleanse
		getPlayer().addSkill(L2Skill.valueOf(724, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Anakim());
	}
}