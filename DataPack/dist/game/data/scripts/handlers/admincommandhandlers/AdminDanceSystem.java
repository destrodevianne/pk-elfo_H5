package handlers.admincommandhandlers;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.AbnormalEffect;
import pk.elfo.gameserver.network.serverpackets.ExShowScreenMessage;
import pk.elfo.gameserver.network.serverpackets.PlaySound;

/**
 * Projeto PkElfo
 */
 
public class AdminDanceSystem implements IAdminCommandHandler
{
	boolean _temp = false;
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_dance",
		"admin_gangnam",
		"admin_sexi"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_dance"))
		{
			AdminHelpPage.showHelpPage(activeChar, "dancesystem.htm");
		}
		
		if (command.equals("admin_gangnam"))
		{
			if (_temp == true)
			{
				ExShowScreenMessage message1 = new ExShowScreenMessage("Ja existe um evento de danca acontecendo! Tente mais tarde!", 4000);
				activeChar.sendPacket(message1);
				return false;
			}
			_temp = true;
			ExShowScreenMessage message1 = new ExShowScreenMessage("Vamos ter um pouco de diversao! Em 30 seg o evento de danca comecara!", 4000);
			activeChar.sendPacket(message1);
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						for (L2PcInstance player : L2World.getInstance().getAllPlayersArray())
						{
							PlaySound _song = new PlaySound(1, "Gangnam", 0, 0, 0, 0, 0);
							player.sendPacket(_song);
							ExShowScreenMessage message1 = new ExShowScreenMessage("Mostre-me o que voce pode fazer entao vamos sacudir !", 8000);
							player.sendPacket(message1);
							player.setIsParalyzed(true);
							player.setIsInvul(true);
							player.broadcastSocialAction(10);
							player.startAbnormalEffect(AbnormalEffect.FLAME);
							ThreadPoolManager.getInstance().scheduleGeneral(new MyTask(), 3500);
							ThreadPoolManager.getInstance().scheduleGeneral(new MyTask2(), 40000);
						}
					}
					catch (Exception e)
					{
					}
				}
			}, (30000));
		}
		
		if (command.equals("admin_sexi"))
		{
			if (_temp == true)
			{
				ExShowScreenMessage message1 = new ExShowScreenMessage("Ja existe um evento de danca acontecendo! Tente mais tarde!", 4000);
				activeChar.sendPacket(message1);
				return false;
			}
			_temp = true;
			ExShowScreenMessage message1 = new ExShowScreenMessage("Vamos ter um pouco de diversao! Em 30 seg o evento de danca comecara!", 4000);
			activeChar.sendPacket(message1);
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						for (L2PcInstance player : L2World.getInstance().getAllPlayersArray())
						{
							PlaySound _song = new PlaySound(1, "sexi", 0, 0, 0, 0, 0);
							player.sendPacket(_song);
							ExShowScreenMessage message1 = new ExShowScreenMessage("Mostre-me o que voce pode fazer entao vamos sacudir !", 8000);
							player.sendPacket(message1);
							player.setIsParalyzed(true);
							player.setIsInvul(true);
							player.broadcastSocialAction(10);
							player.startAbnormalEffect(AbnormalEffect.FLAME);
							ThreadPoolManager.getInstance().scheduleGeneral(new MyTask(), 3500);
							ThreadPoolManager.getInstance().scheduleGeneral(new MyTask2(), 43000);
						}
					}
					catch (Exception e)
					{
					}
				}
			}, (30000));
		}
		
		return false;
	}
	
	class MyTask implements Runnable
	{
		@Override
		public void run()
		{
			if (_temp == true)
			{
				for (L2PcInstance player : L2World.getInstance().getAllPlayersArray())
				{
					player.broadcastSocialAction(18);
				}
				ThreadPoolManager.getInstance().scheduleGeneral(new MyTask(), 18000);
			}
		}
	}
	
	class MyTask2 implements Runnable
	{
		
		@Override
		public void run()
		{
			for (L2PcInstance player : L2World.getInstance().getAllPlayersArray())
			{
				_temp = false;
				player.setIsParalyzed(false);
				player.setIsInvul(false);
				player.broadcastSocialAction(10);
				player.broadcastSocialAction(11);
				player.stopAbnormalEffect(AbnormalEffect.FLAME);
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}