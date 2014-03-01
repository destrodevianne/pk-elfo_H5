package handlers.bypasshandlers;

import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MerchantInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.network.serverpackets.SetupGauge;

/**
 * PkElfo
 */

public class RentPet implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"RentPet"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2MerchantInstance))
		{
			return false;
		}
		
		if (!Config.ALLOW_RENTPET)
		{
			return false;
		}
		
		if (!Config.LIST_PET_RENT_NPC.contains(((L2Npc) target).getTemplate().getNpcId()))
		{
			return false;
		}
		
		try
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			if (st.countTokens() < 1)
			{
				NpcHtmlMessage msg = new NpcHtmlMessage(((L2Npc) target).getObjectId());
				msg.setHtml("<html><body>Adestrador de Pet:<br>" + "Voce pode alugar um wyvern ou um strider por adena.<br>Meus precos:<br1>" + "<table border=0><tr><td>Passeio</td></tr>" + "<tr><td>Wyvern</td><td>Strider</td></tr>" + "<tr><td><a action=\"bypass -h npc_%objectId%_RentPet 1\">30 seg/1800 adena</a></td><td><a action=\"bypass -h npc_%objectId%_RentPet 11\">30 seg/900 adena</a></td></tr>" + "<tr><td><a action=\"bypass -h npc_%objectId%_RentPet 2\">1 min/7200 adena</a></td><td><a action=\"bypass -h npc_%objectId%_RentPet 12\">1 min/3600 adena</a></td></tr>" + "<tr><td><a action=\"bypass -h npc_%objectId%_RentPet 3\">10 min/720000 adena</a></td><td><a action=\"bypass -h npc_%objectId%_RentPet 13\">10 min/360000 adena</a></td></tr>" + "<tr><td><a action=\"bypass -h npc_%objectId%_RentPet 4\">30 min/6480000 adena</a></td><td><a action=\"bypass -h npc_%objectId%_RentPet 14\">30 min/3240000 adena</a></td></tr>" + "</table>" + "</body></html>");
				msg.replace("%objectId%", String.valueOf(((L2Npc) target).getObjectId()));
				activeChar.sendPacket(msg);
			}
			else
			{
				tryRentPet(activeChar, Integer.parseInt(st.nextToken()));
			}
			
			return true;
		}
		catch (Exception e)
		{
			_log.info("Exception in " + getClass().getSimpleName());
		}
		return false;
	}
	
	public static final void tryRentPet(L2PcInstance player, int val)
	{
		if ((player == null) || player.hasSummon() || player.isMounted() || player.isRentedPet() || player.isTransformed() || player.isCursedWeaponEquipped())
		{
			return;
		}
		if (!player.disarmWeapons())
		{
			return;
		}
		
		int petId;
		double price = 1;
		int cost[] =
		{
			1800,
			7200,
			720000,
			6480000
		};
		int ridetime[] =
		{
			30,
			60,
			600,
			1800
		};
		
		if (val > 10)
		{
			petId = 12526;
			val -= 10;
			price /= 2;
		}
		else
		{
			petId = 12621;
		}
		
		if ((val < 1) || (val > 4))
		{
			return;
		}
		
		price *= cost[val - 1];
		int time = ridetime[val - 1];
		
		if (!player.reduceAdena("Rent", (long) price, player.getLastFolkNPC(), true))
		{
			return;
		}
		
		player.mount(petId, 0, false);
		SetupGauge sg = new SetupGauge(3, time * 1000);
		player.sendPacket(sg);
		player.startRentPet(time);
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}