package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class DivineSummoner extends L2Transformation
{
	private static final int[] SKILLS =
	{
		710,
		711,
		712,
		713,
		714,
		5779,
		619
	};
	
	public DivineSummoner()
	{
		// id, colRadius, colHeight
		super(258, 10, 25);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 258) || getPlayer().isCursedWeaponEquipped())
		{
			return;
		}
		
		if (getPlayer().hasSummon())
		{
			getPlayer().getSummon().unSummon(getPlayer());
		}
		transformedSkills();
	}
	
	@Override
	public void onUntransform()
	{
		if (getPlayer().hasSummon())
		{
			getPlayer().getSummon().unSummon(getPlayer());
		}
		removeSkills();
	}
	
	public void removeSkills()
	{
		// Divine Summoner Summon Divine Beast
		getPlayer().removeSkill(L2Skill.valueOf(710, 1), false);
		// Divine Summoner Transfer Pain
		getPlayer().removeSkill(L2Skill.valueOf(711, 1), false);
		// Divine Summoner Final Servitor
		getPlayer().removeSkill(L2Skill.valueOf(712, 1), false);
		// Divine Summoner Servitor Hill
		getPlayer().removeSkill(L2Skill.valueOf(713, 1), false);
		// Sacrifice Summoner
		getPlayer().removeSkill(L2Skill.valueOf(714, 1), false, false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Divine Summoner Summon Divine Beast
		getPlayer().addSkill(L2Skill.valueOf(710, 1), false);
		// Divine Summoner Transfer Pain
		getPlayer().addSkill(L2Skill.valueOf(711, 1), false);
		// Divine Summoner Final Servitor
		getPlayer().addSkill(L2Skill.valueOf(712, 1), false);
		// Divine Summoner Servitor Hill
		getPlayer().addSkill(L2Skill.valueOf(713, 1), false);
		// Sacrifice Summoner
		getPlayer().addSkill(L2Skill.valueOf(714, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new DivineSummoner());
	}
}