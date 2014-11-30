package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class VanguardPaladin extends L2Transformation
{
	public VanguardPaladin()
	{
		// id
		super(312);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 312) || getPlayer().isCursedWeaponEquipped())
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
		int lvl = 1;
		if (getPlayer().getLevel() > 42)
		{
			lvl = (getPlayer().getLevel() - 42);
		}
		
		// Two handed mastery
		getPlayer().removeSkill(L2Skill.valueOf(293, lvl), false);
		// Full Swing
		getPlayer().removeSkill(L2Skill.valueOf(814, lvl), false);
		// Power Divide
		getPlayer().removeSkill(L2Skill.valueOf(816, lvl), false);
		// Boost Morale
		getPlayer().removeSkill(L2Skill.valueOf(956, lvl), false, false);
		// Guillotine Attack
		getPlayer().removeSkill(L2Skill.valueOf(957, lvl), false);
		// Switch Stance
		getPlayer().removeSkill(L2Skill.valueOf(838, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		int lvl = 1;
		if (getPlayer().getLevel() > 42)
		{
			lvl = (getPlayer().getLevel() - 42);
		}
		
		// Two handed mastery
		getPlayer().addSkill(L2Skill.valueOf(293, lvl), false);
		// Full Swing
		getPlayer().addSkill(L2Skill.valueOf(814, lvl), false);
		// Power Divide
		getPlayer().addSkill(L2Skill.valueOf(816, lvl), false);
		// Boost Morale
		getPlayer().addSkill(L2Skill.valueOf(956, lvl), false);
		// Guillotine Attack
		getPlayer().addSkill(L2Skill.valueOf(957, lvl), false);
		// Switch Stance
		getPlayer().addSkill(L2Skill.valueOf(838, 1), false);
		// Set allowed skills
		getPlayer().setTransformAllowedSkills(new int[]
		{
			18,
			28,
			196,
			197,
			293,
			400,
			406,
			814,
			816,
			838,
			956,
			957
		});
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new VanguardPaladin());
	}
}
