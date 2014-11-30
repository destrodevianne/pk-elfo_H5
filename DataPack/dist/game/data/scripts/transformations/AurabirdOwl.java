package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class AurabirdOwl extends L2Transformation
{
	private static final int[] SKILLS = new int[]
	{
		884,
		885,
		887,
		889,
		892,
		893,
		895,
		911,
		932,
		619
	};
	
	public AurabirdOwl()
	{
		// id, colRadius, colHeight
		super(9, 40, 18.57);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 9) || getPlayer().isCursedWeaponEquipped())
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
		// Air Blink
		getPlayer().removeSkill(L2Skill.valueOf(885, 1), false);
		
		// Exhilarate
		getPlayer().removeSkill(L2Skill.valueOf(895, 1), false);
		
		int lvl = getPlayer().getLevel() - 74;
		
		if (lvl > 0)
		{
			// Air Assault (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(884, lvl), false);
			// Sky Clutch (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(887, lvl), false);
			// Energy Storm (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(889, lvl), false);
			// Energy Shot (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(892, lvl), false);
			// Concentrated Energy Shot (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(893, lvl), false);
			// Energy Burst (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(911, lvl), false);
		}
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Air Blink
		if (getPlayer().getLevel() >= 75)
		{
			getPlayer().addSkill(L2Skill.valueOf(885, 1), false);
		}
		
		// Exhilarate
		if (getPlayer().getLevel() >= 83)
		{
			getPlayer().addSkill(L2Skill.valueOf(895, 1), false);
		}
		
		int lvl = getPlayer().getLevel() - 74;
		
		if (lvl > 0)
		{
			// Air Assault (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(884, lvl), false);
			// Sky Clutch (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(887, lvl), false);
			// Energy Storm (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(889, lvl), false);
			// Energy Shot (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(892, lvl), false);
			// Concentrated Energy Shot (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(893, lvl), false);
			// Energy Burst (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(911, lvl), false);
		}
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new AurabirdOwl());
	}
}