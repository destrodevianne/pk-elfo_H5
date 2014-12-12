package handlers.actionhandlers;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.handler.IActionHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2StaticObjectInstance;
import pk.elfo.gameserver.network.serverpackets.MyTargetSelected;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Projeto PkElfo
 */

public class L2StaticObjectInstanceAction implements IActionHandler
{
	@Override
	public boolean action(final L2PcInstance activeChar, final L2Object target, final boolean interact)
	{
		final L2StaticObjectInstance staticObject = (L2StaticObjectInstance) target;
		if (staticObject.getType() < 0)
		{
			_log.info("L2StaticObjectInstance: StaticObject com o tipo invalido! StaticObjectId: " + staticObject.getStaticObjectId());
		}
		
		// Check if the L2PcInstance already target the L2NpcInstance
		if (activeChar.getTarget() != staticObject)
		{
			// Set the target of the L2PcInstance activeChar
			activeChar.setTarget(staticObject);
			activeChar.sendPacket(new MyTargetSelected(staticObject.getObjectId(), 0));
		}
		else if (interact)
		{
			activeChar.sendPacket(new MyTargetSelected(staticObject.getObjectId(), 0));
			
			// Calculate the distance between the L2PcInstance and the L2NpcInstance
			if (!activeChar.isInsideRadius(staticObject, L2Npc.INTERACTION_DISTANCE, false, false))
			{
				// Notify the L2PcInstance AI with AI_INTENTION_INTERACT
				activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, staticObject);
			}
			else
			{
				if (staticObject.getType() == 2)
				{
					final String filename = (staticObject.getStaticObjectId() == 24230101) ? "data/html/signboards/tomb_of_crystalgolem.htm" : "data/html/signboards/pvp_signboard.htm";
					final String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), filename);
					final NpcHtmlMessage html = new NpcHtmlMessage(staticObject.getObjectId());
					
					if (content == null)
					{
						html.setHtml("<html><body>Tabuleta esta faltando:<br>" + filename + "</body></html>");
					}
					else
					{
						html.setHtml(content);
					}
					
					activeChar.sendPacket(html);
				}
				else if (staticObject.getType() == 0)
				{
					activeChar.sendPacket(staticObject.getMap());
				}
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.L2StaticObjectInstance;
	}
}