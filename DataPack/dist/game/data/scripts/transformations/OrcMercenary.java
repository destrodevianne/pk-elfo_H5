package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class OrcMercenary extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5491,
		619
	};
	
	public OrcMercenary()
	{
		// id, colRadius, colHeight
		super(13, 8, 27);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 13) || getPlayer().isCursedWeaponEquipped())
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
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new OrcMercenary());
	}
}
