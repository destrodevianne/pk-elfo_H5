package handlers.usercommandhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.GameTimeController;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.IUserCommandHandler;
import pk.elfo.gameserver.instancemanager.MapRegionManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.entity.TvTRoundEvent;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.network.serverpackets.SetupGauge;
import pk.elfo.gameserver.util.Broadcast;

/**
 * PkElfo
 */

public class Unstuck implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		52
	};
	
	@Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		// Thanks nbd
		if (!TvTEvent.onEscapeUse(activeChar.getObjectId()))
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}

		if (!TvTRoundEvent.onEscapeUse(activeChar.getObjectId()))
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if(!Config.ENABLE_UNSTUCK_PVP)
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		int unstuckTimer = (activeChar.getAccessLevel().isGm() ? 1000 : Config.UNSTUCK_INTERVAL * 1000);
		
		if (activeChar.isCastingNow() || activeChar.isMovementDisabled() || activeChar.isMuted() || activeChar.isAlikeDead() || activeChar.isInOlympiadMode() || activeChar.inObserverMode() || activeChar.isCombatFlagEquipped())
		{
			return false;
		}
		activeChar.forceIsCasting(GameTimeController.getGameTicks() + (unstuckTimer / GameTimeController.MILLIS_IN_TICK));
		
		L2Skill escape = SkillTable.getInstance().getInfo(2099, 1); // 5 minutes escape
		L2Skill GM_escape = SkillTable.getInstance().getInfo(2100, 1); // 1 second escape
		if (activeChar.getAccessLevel().isGm())
		{
			if (GM_escape != null)
			{
				activeChar.doCast(GM_escape);
				return true;
			}
			activeChar.sendMessage("Voce usou escape: 1 second.");
		}
		else if ((Config.UNSTUCK_INTERVAL == 300) && (escape != null))
		{
			activeChar.doCast(escape);
			return true;
		}
		else
		{
			if (Config.UNSTUCK_INTERVAL > 100)
			{
				activeChar.sendMessage("Voce usou escape: " + (unstuckTimer / 60000) + " minutos.");
			}
			else
			{
				activeChar.sendMessage("Voce usou escape: " + (unstuckTimer / 1000) + " segundos.");
			}
		}
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		// SoE Animation section
		activeChar.setTarget(activeChar);
		activeChar.disableAllSkills();
		
		MagicSkillUse msk = new MagicSkillUse(activeChar, Config.UNSTUCK_ANIMATION_ID, 1, unstuckTimer, 0);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, msk, 900);
		SetupGauge sg = new SetupGauge(0, unstuckTimer);
		activeChar.sendPacket(sg);
		// End SoE Animation section
		
		// continue execution later
		activeChar.setSkillCast(ThreadPoolManager.getInstance().scheduleGeneral(new EscapeFinalizer(activeChar), unstuckTimer));
		
		return true;
	}
	
	private static class EscapeFinalizer implements Runnable
	{
		private final L2PcInstance _activeChar;
		
		protected EscapeFinalizer(L2PcInstance activeChar)
		{
			_activeChar = activeChar;
		}
		
		@Override
		public void run()
		{
			if (_activeChar.isDead())
			{
				return;
			}
			
			_activeChar.setIsIn7sDungeon(false);
			_activeChar.enableAllSkills();
			_activeChar.setIsCastingNow(false);
			_activeChar.setInstanceId(0);
			if (Config.UnstuckCustom)
			{
				_activeChar.teleToLocation(Config.LocX, Config.LocY, Config.LocZ);
			}
			else
			{
				_activeChar.teleToLocation(MapRegionManager.TeleportWhereType.Town);
			}
		}
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}