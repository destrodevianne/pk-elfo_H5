package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

public class InquisitorShilienElder extends L2Transformation
{
	public InquisitorShilienElder()
	{
		// id
		super(318);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 318) || getPlayer().isCursedWeaponEquipped())
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
		if (getPlayer().getLevel() > 43)
		{
			lvl = (getPlayer().getLevel() - 43);
		}
		
		// Divine Punishment
		getPlayer().removeSkill(L2Skill.valueOf(1523, lvl), false);
		// Divine Flash
		getPlayer().removeSkill(L2Skill.valueOf(1528, lvl), false);
		// Holy Weapon
		getPlayer().removeSkill(L2Skill.valueOf(1043, 1), false, false);
		// Surrender to the Holy
		getPlayer().removeSkill(L2Skill.valueOf(1524, lvl), false);
		// Divine Curse
		getPlayer().removeSkill(L2Skill.valueOf(1525, lvl), false);
		// Switch Stance
		getPlayer().removeSkill(L2Skill.valueOf(838, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		int lvl = 1;
		if (getPlayer().getLevel() > 43)
		{
			lvl = (getPlayer().getLevel() - 43);
		}
		
		// Divine Punishment
		getPlayer().addSkill(L2Skill.valueOf(1523, lvl), false);
		// Divine Flash
		getPlayer().addSkill(L2Skill.valueOf(1528, lvl), false);
		// Holy Weapon
		getPlayer().addSkill(L2Skill.valueOf(1043, 1), false);
		// Surrender to the Holy
		getPlayer().addSkill(L2Skill.valueOf(1524, lvl), false);
		// Divine Curse
		getPlayer().addSkill(L2Skill.valueOf(1525, lvl), false);
		// Set allowed skills
		getPlayer().setTransformAllowedSkills(new int[]
		{
			838,
			1523,
			1528,
			1524,
			1525,
			1430,
			1303,
			1059,
			1043
		});
		// Switch Stance
		getPlayer().addSkill(L2Skill.valueOf(838, 1), false);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new InquisitorShilienElder());
	}
}
