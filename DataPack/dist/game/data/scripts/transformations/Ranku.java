package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class Ranku extends L2Transformation
{
	private static final int[] SKILLS =
	{
		731,
		732,
		5491,
		619
	};
	
	public Ranku()
	{
		// id, colRadius, colHeight
		super(309, 13, 29);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 309) || getPlayer().isCursedWeaponEquipped())
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
		// Ranku Dark Explosion
		getPlayer().removeSkill(L2Skill.valueOf(731, 1), false);
		// Ranku Stun Attack
		getPlayer().removeSkill(L2Skill.valueOf(732, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Ranku Dark Explosion
		getPlayer().addSkill(L2Skill.valueOf(731, 1), false);
		// Ranku Stun Attack
		getPlayer().addSkill(L2Skill.valueOf(732, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new Ranku());
	}
}
