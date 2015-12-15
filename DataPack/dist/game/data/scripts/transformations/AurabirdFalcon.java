package transformations;

import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class AurabirdFalcon extends L2Transformation
{
	private static final int[] SKILLS = new int[]
	{
		884,
		885,
		886,
		888,
		890,
		891,
		894,
		911,
		932,
		619
	};
	
	public AurabirdFalcon()
	{
		// id, colRadius, colHeight
		super(8, 38, 14.25);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 8) || getPlayer().isCursedWeaponEquipped())
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
		getPlayer().removeSkill(L2Skill.valueOf(894, 1), false);
		
		int lvl = getPlayer().getLevel() - 74;
		
		if (lvl > 0)
		{
			// Air Assault (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(884, lvl), false);
			// Air Shock Bomb (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(886, lvl), false);
			// Energy Storm (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(888, lvl), false);
			// Prodigious Flare (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(890, lvl), false);
			// Energy Shot (up to 11 levels)
			getPlayer().removeSkill(L2Skill.valueOf(891, lvl), false);
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
			getPlayer().addSkill(L2Skill.valueOf(894, 1), false);
		}
		
		int lvl = getPlayer().getLevel() - 74;
		
		if (lvl > 0)
		{
			// Air Assault (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(884, lvl), false);
			// Air Shock Bomb (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(886, lvl), false);
			// Energy Storm (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(888, lvl), false);
			// Prodigious Flare (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(890, lvl), false);
			// Energy Shot (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(891, lvl), false);
			// Energy Burst (up to 11 levels)
			getPlayer().addSkill(L2Skill.valueOf(911, lvl), false);
		}
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new AurabirdFalcon());
	}
}