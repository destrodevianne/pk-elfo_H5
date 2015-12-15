package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Rabbit extends L2Transformation
{
	private static final int[] SKILLS =
	{
		629,
		630,
		5491,
		619
	};
	
	public Rabbit()
	{
		// id, colRadius, colHeight
		super(105, 5, 4.5);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 105) || getPlayer().isCursedWeaponEquipped())
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
		// Rabbit Magic Eye
		getPlayer().removeSkill(L2Skill.valueOf(629, 1), false);
		// Rabbit Tornado
		getPlayer().removeSkill(L2Skill.valueOf(630, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Rabbit Magic Eye
		getPlayer().addSkill(L2Skill.valueOf(629, 1), false);
		// Rabbit Tornado
		getPlayer().addSkill(L2Skill.valueOf(630, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Rabbit());
	}
}
