package handlers.voicedcommandhandlers;

import java.util.StringTokenizer;
import java.util.logging.Level;

import pk.elfo.gameserver.LoginServerThread;
import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */

public class ChangePassword implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"changepassword"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (target != null)
		{
			final StringTokenizer st = new StringTokenizer(target);
			try
			{
				String curpass = null, newpass = null, repeatnewpass = null;
				if (st.hasMoreTokens())
				{
					curpass = st.nextToken();
				}
				if (st.hasMoreTokens())
				{
					newpass = st.nextToken();
				}
				if (st.hasMoreTokens())
				{
					repeatnewpass = st.nextToken();
				}
				
				if (!((curpass == null) || (newpass == null) || (repeatnewpass == null)))
				{
					if (!newpass.equals(repeatnewpass))
					{
						activeChar.sendMessage("A nova senha nao corresponde com a repetida!");
						return false;
					}
					if (newpass.length() < 3)
					{
						activeChar.sendMessage("A nova senha esta menor que 3 caracteres! Por favor, tente uma mais longa.");
						return false;
					}
					if (newpass.length() > 30)
					{
						activeChar.sendMessage("A nova senha tem mais de 30 caracteres! Por favor, tente uma menor.");
						return false;
					}
					
					LoginServerThread.getInstance().sendChangePassword(activeChar.getAccountName(), activeChar.getName(), curpass, newpass);
				}
				else
				{
					activeChar.sendMessage("Dados de senha invalida! Voce deve preencher todas as caixas.");
					return false;
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Ocorreu um problema ao alterar a senha!");
				_log.log(Level.WARNING, "", e);
			}
		}
		else
		{
			// showHTML(activeChar);
			String html = HtmCache.getInstance().getHtm("en", "data/html/mods/ChangePassword.htm");
			if (html == null)
			{
				html = "<html><body><br><br><center><font color=LEVEL>404:</font> Arquivo nao encontrado</center></body></html>";
			}
			activeChar.sendPacket(new NpcHtmlMessage(1, html));
			return true;
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}