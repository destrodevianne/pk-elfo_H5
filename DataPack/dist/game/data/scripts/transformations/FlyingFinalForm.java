package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class FlyingFinalForm extends L2Transformation
{
	private static final int[] SKILLS =
	{
		932,
		950,
		951,
		953,
		1544,
		1545,
		619
	};
	
	public FlyingFinalForm()
	{
		// id, colRadius, colHeight
		super(260, 9, 38);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 260) || getPlayer().isCursedWeaponEquipped())
		{
			return;
		}
		
		getPlayer().setIsFlyingMounted(true);
		
		transformedSkills();
	}
	
	@Override
	public void onUntransform()
	{
		getPlayer().setIsFlyingMounted(false);
		
		removeSkills();
	}
	
	public void removeSkills()
	{
		// Life to Soul
		getPlayer().removeSkill(L2Skill.valueOf(953, 1), false);
		// Soul Sucking
		getPlayer().removeSkill(L2Skill.valueOf(1545, 1), false);
		
		int lvl = getPlayer().getLevel() - 78;
		
		if (lvl > 0)
		{
			// Nail Attack (up to 7 levels)
			getPlayer().removeSkill(L2Skill.valueOf(950, lvl), false);
			// Wing Assault (up to 7 levels)
			getPlayer().removeSkill(L2Skill.valueOf(951, lvl), false);
			// Death Beam (up to 7 levels)
			getPlayer().removeSkill(L2Skill.valueOf(1544, lvl), false);
		}
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Life to Soul
		getPlayer().addSkill(L2Skill.valueOf(953, 1), false);
		// Soul Sucking
		getPlayer().addSkill(L2Skill.valueOf(1545, 1), false);
		
		int lvl = getPlayer().getLevel() - 78;
		
		if (lvl > 0)
		{
			// Nail Attack (up to 7 levels)
			getPlayer().addSkill(L2Skill.valueOf(950, lvl), false);
			// Wing Assault (up to 7 levels)
			getPlayer().addSkill(L2Skill.valueOf(951, lvl), false);
			// Death Beam (up to 7 levels)
			getPlayer().addSkill(L2Skill.valueOf(1544, lvl), false);
		}
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new FlyingFinalForm());
	}
}
