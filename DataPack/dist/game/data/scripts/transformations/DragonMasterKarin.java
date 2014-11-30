package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DragonMasterKarin extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5491,
		619,
		20003,
		20004,
		20005
	};
	
	public DragonMasterKarin()
	{
		// id, colRadius, colHeight
		super(20006, 8, 18.6);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 20006) || getPlayer().isCursedWeaponEquipped())
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
		// Dragon Slash
		getPlayer().removeSkill(L2Skill.valueOf(20003, 1), false);
		// Dragon Dash
		getPlayer().removeSkill(L2Skill.valueOf(20004, 1), false);
		// Dragon Aura
		getPlayer().removeSkill(L2Skill.valueOf(20005, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		// Dragon Slash
		getPlayer().addSkill(L2Skill.valueOf(20003, 1), false);
		// Dragon Dash
		getPlayer().addSkill(L2Skill.valueOf(20004, 1), false);
		// Dragon Aura
		getPlayer().addSkill(L2Skill.valueOf(20005, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DragonMasterKarin());
	}
}