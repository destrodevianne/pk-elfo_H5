package transformations;
 
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Transformation;
import pk.elfo.gameserver.model.skills.L2Skill;

/**
 * TODO: Buffs disappear once you get transformed, but reappear after the transformed state wears off. Skills involved in the minigame but are not assigned directly to players: Flip Nearby Blocks - 5847 - For Flip Block, there are two skills, one for each side (makes sense). For this, there is only
 * one skill. Thus it is probably not assigned to the transformation. Block Trigger Slow - 5848 - This may be assigned to players, unsure. Decrease Speed - 5849 - This is possibly assigned to all players to set all players to the same running speed for the duration of the game. Block Trigger Stun -
 * 5849 - From L2Vault: "The squares gives drops of "bond" and "landmine
 * ". I wasn't able to figure out what the bond did as it wasn't anything that seemed to go into your inventory. However, Landmine did appear in your inventory which allows you to use it before flipping a square which will give the other team a state of stun when they attempt to flip the same square (from what I can gather, it all happens so quickly ;) "
 * Shock - 5851 - Stun effect from 5849 More Info: http://l2vault.ign.com/wiki/index.php/Handy%E2%80%99s_Block_Checker
 */
public class ScrollBlue extends L2Transformation
{
	private static final int[] SKILLS =
	{
		5852,
		5491,
		619
	};
	
	public ScrollBlue()
	{
		// id, colRadius, colHeight
		super(122, 9, 28.3);
	}
	
	@Override
	public void onTransform()
	{
		if ((getPlayer().getTransformationId() != 122) || getPlayer().isCursedWeaponEquipped())
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
		// Flip Block
		getPlayer().removeSkill(L2Skill.valueOf(5852, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		// getPlayer().removeSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public void transformedSkills()
	{
		// Flip Block
		getPlayer().addSkill(L2Skill.valueOf(5852, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(L2Skill.valueOf(5491, 1), false);
		// Transform Dispel
		// getPlayer().addSkill(L2Skill.valueOf(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new ScrollBlue());
	}
}
