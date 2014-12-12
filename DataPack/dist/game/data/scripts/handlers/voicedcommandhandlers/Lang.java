package handlers.voicedcommandhandlers;

import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;
 
/**
 * Projeto PkElfo
 */

public class Lang implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"lang"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (!Config.L2JMOD_MULTILANG_ENABLE || !Config.L2JMOD_MULTILANG_VOICED_ALLOW)
		{
			return false;
		}
		
		final NpcHtmlMessage msg = new NpcHtmlMessage(1);
		if (params == null)
		{
			final StringBuilder html = StringUtil.startAppend(100);
			for (String lang : Config.L2JMOD_MULTILANG_ALLOWED)
			{
				StringUtil.append(html, "<button value=\"", lang.toUpperCase(), "\" action=\"bypass -h voice .lang ", lang, "\" width=60 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>");
			}
			
			msg.setFile(activeChar.getHtmlPrefix(), "data/html/mods/Lang/LanguageSelect.htm");
			msg.replace("%list%", html.toString());
			activeChar.sendPacket(msg);
			return true;
		}
		
		final StringTokenizer st = new StringTokenizer(params);
		if (st.hasMoreTokens())
		{
			final String lang = st.nextToken().trim();
			if (activeChar.setLang(lang))
			{
				msg.setFile(activeChar.getHtmlPrefix(), "data/html/mods/Lang/Ok.htm");
				activeChar.sendPacket(msg);
				return true;
			}
			msg.setFile(activeChar.getHtmlPrefix(), "data/html/mods/Lang/Error.htm");
			activeChar.sendPacket(msg);
			return true;
		}
		return false;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}