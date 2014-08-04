package pk.elfo.gameserver.model.skills.l2skills;

import java.util.logging.Level;

import pk.elfo.gameserver.events.EventsInterface;
import pk.elfo.gameserver.instancemanager.GrandBossManager;
import pk.elfo.gameserver.instancemanager.MapRegionManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.PcCondOverride;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.PkHunterEvent;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.entity.TvTRoundEvent;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;

public class L2SkillTeleport extends L2Skill
{
	private final String _recallType;
	private final Location _loc;
	
	public L2SkillTeleport(StatsSet set)
	{
		super(set);
		
		_recallType = set.getString("recallType", "");
		String coords = set.getString("teleCoords", null);
		if (coords != null)
		{
			String[] valuesSplit = coords.split(",");
			_loc = new Location(Integer.parseInt(valuesSplit[0]), Integer.parseInt(valuesSplit[1]), Integer.parseInt(valuesSplit[2]));
		}
		else
		{
			_loc = null;
		}
	}
	
	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets)
	{
		boolean bss = isMagic() && activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		
		if (activeChar.isPlayer())
		{
			// Thanks nbd
			if (!TvTEvent.onEscapeUse(activeChar.getActingPlayer().getObjectId()))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (!TvTRoundEvent.onEscapeUse(((L2PcInstance) activeChar).getObjectId()))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (EventsInterface.isParticipating(activeChar.getObjectId()))
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (PkHunterEvent.isPk(activeChar))
			{
				activeChar.sendMessage("Voce nao pode usar habilidades de fuga, durante o evento.");
				return;
			}
			
			if (activeChar.isAfraid())
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (activeChar.getActingPlayer().isCombatFlagEquipped())
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			if (activeChar.getActingPlayer().isInOlympiadMode())
			{
				activeChar.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
				return;
			}
			
			if ((GrandBossManager.getInstance().getZone(activeChar) != null) && !activeChar.canOverrideCond(PcCondOverride.SKILL_CONDITIONS))
			{
				activeChar.sendPacket(SystemMessageId.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION);
				return;
			}
		}
		
		try
		{
			for (L2Character target : (L2Character[]) targets)
			{
				if (target.isPlayer())
				{
					L2PcInstance targetChar = target.getActingPlayer();
					
					// Check to see if player is in a duel
					if (targetChar.isInDuel())
					{
						targetChar.sendMessage("You cannot use escape skills during a duel.");
						continue;
					}
					
					if (targetChar != activeChar)
					{
						if (!TvTEvent.onEscapeUse(targetChar.getObjectId()))
						{
							continue;
						}
						
						if (!TvTRoundEvent.onEscapeUse(targetChar.getObjectId()))
						{
							continue;
						}
						
						if (EventsInterface.isParticipating(targetChar.getObjectId()))
						{
							continue;
						}
						
						if (targetChar.isInOlympiadMode())
						{
							continue;
						}
						
						if (GrandBossManager.getInstance().getZone(targetChar) != null)
						{
							continue;
						}
						
						if (targetChar.isCombatFlagEquipped())
						{
							continue;
						}
					}
				}
				Location loc = null;
				if (getSkillType() == L2SkillType.TELEPORT)
				{
					if (_loc != null)
					{
						// target is not player OR player is not flying or flymounted
						// TODO: add check for gracia continent coords
						if (!(target.isPlayer()) || !(target.isFlying() || (target.getActingPlayer().isFlyingMounted())))
						{
							loc = _loc;
						}
					}
				}
				else
				{
					if (_recallType.equalsIgnoreCase("Castle"))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.Castle);
					}
					else if (_recallType.equalsIgnoreCase("ClanHall"))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.ClanHall);
					}
					else if (_recallType.equalsIgnoreCase("Fortress"))
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.Fortress);
					}
					else
					{
						loc = MapRegionManager.getInstance().getTeleToLocation(target, MapRegionManager.TeleportWhereType.Town);
					}
				}
				if (loc != null)
				{
					target.setInstanceId(0);
					if (target.isPlayer())
					{
						target.getActingPlayer().setIsIn7sDungeon(false);
					}
					target.teleToLocation(loc, true);
				}
			}
			
			activeChar.setChargedShot(bss ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS, false);
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "", e);
		}
	}
}