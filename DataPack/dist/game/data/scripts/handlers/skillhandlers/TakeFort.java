package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

/**
 * Projeto PkElfo
 */
 
public class TakeFort implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.TAKEFORT
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer() || (targets.length == 0))
		{
			return;
		}
		
		L2PcInstance player = activeChar.getActingPlayer();
		if (player.getClan() == null)
		{
			return;
		}
		
		Fort fort = FortManager.getInstance().getFort(player);
		if ((fort == null) || !player.checkIfOkToCastFlagDisplay(fort, true, skill, targets[0]))
		{
			return;
		}
		
		try
		{
			fort.endOfSiege(player.getClan());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
	
	public static void main(String[] args)
	{
		new TakeFort();
	}
}