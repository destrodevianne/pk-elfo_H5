package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.network.serverpackets.SetupGauge;
import pk.elfo.gameserver.util.Util;
 
/**
 * Projeto PkElfo
 */

public class AdminPolymorph implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_polymorph",
		"admin_unpolymorph",
		"admin_polymorph_menu",
		"admin_unpolymorph_menu",
		"admin_transform",
		"admin_untransform",
		"admin_transform_menu",
		"admin_untransform_menu",
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_untransform"))
		{
			L2Object obj = activeChar.getTarget();
			if (obj instanceof L2Character)
			{
				((L2Character) obj).stopTransformation(true);
			}
			else
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			}
		}
		else if (command.startsWith("admin_transform"))
		{
			L2Object obj = activeChar.getTarget();
			if ((obj != null) && obj.isPlayer())
			{
				L2PcInstance cha = obj.getActingPlayer();
				
				if (activeChar.isSitting())
				{
					activeChar.sendPacket(SystemMessageId.CANNOT_TRANSFORM_WHILE_SITTING);
					return false;
				}
				
				else if (cha.isTransformed() || cha.isInStance())
				{
					activeChar.sendPacket(SystemMessageId.YOU_ALREADY_POLYMORPHED_AND_CANNOT_POLYMORPH_AGAIN);
					return false;
				}
				
				else if (cha.isInWater())
				{
					activeChar.sendPacket(SystemMessageId.YOU_CANNOT_POLYMORPH_INTO_THE_DESIRED_FORM_IN_WATER);
					return false;
				}
				
				else if (cha.isFlyingMounted() || cha.isMounted() || cha.isRidingStrider())
				{
					activeChar.sendPacket(SystemMessageId.YOU_CANNOT_POLYMORPH_WHILE_RIDING_A_PET);
					return false;
				}
				
				final String[] parts = command.split(" ");
				if (parts.length > 1)
				{
					if (Util.isDigit(parts[1]))
					{
						final int id = Integer.parseInt(parts[1]);
						if (!TransformationManager.getInstance().transformPlayer(id, cha))
						{
							cha.sendMessage("Unknown transformation Id: " + id);
						}
					}
					else
					{
						activeChar.sendMessage("Usage: //transform <id>");
					}
				}
				else if (parts.length == 1)
				{
					cha.untransform();
				}
				else
				{
					activeChar.sendMessage("Usage: //transform <id>");
				}
			}
			else
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			}
		}
		if (command.startsWith("admin_polymorph"))
		{
			StringTokenizer st = new StringTokenizer(command);
			L2Object target = activeChar.getTarget();
			try
			{
				st.nextToken();
				String p1 = st.nextToken();
				if (st.hasMoreTokens())
				{
					String p2 = st.nextToken();
					doPolymorph(activeChar, target, p2, p1);
				}
				else
				{
					doPolymorph(activeChar, target, p1, "npc");
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //polymorph [type] <id>");
			}
		}
		else if (command.equals("admin_unpolymorph"))
		{
			doUnpoly(activeChar, activeChar.getTarget());
		}
		if (command.contains("_menu"))
		{
			showMainPage(activeChar, command);
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * @param activeChar
	 * @param obj
	 * @param id
	 * @param type
	 */
	private void doPolymorph(L2PcInstance activeChar, L2Object obj, String id, String type)
	{
		if (obj != null)
		{
			obj.getPoly().setPolyInfo(type, id);
			// animation
			if (obj instanceof L2Character)
			{
				L2Character Char = (L2Character) obj;
				MagicSkillUse msk = new MagicSkillUse(Char, 1008, 1, 4000, 0);
				Char.broadcastPacket(msk);
				SetupGauge sg = new SetupGauge(0, 4000);
				Char.sendPacket(sg);
			}
			// end of animation
			obj.decayMe();
			obj.spawnMe(obj.getX(), obj.getY(), obj.getZ());
			activeChar.sendMessage("Polymorph succeed");
		}
		else
		{
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
		}
	}
	
	/**
	 * @param activeChar
	 * @param target
	 */
	private void doUnpoly(L2PcInstance activeChar, L2Object target)
	{
		if (target != null)
		{
			target.getPoly().setPolyInfo(null, "1");
			target.decayMe();
			target.spawnMe(target.getX(), target.getY(), target.getZ());
			activeChar.sendMessage("Unpolymorph succeed");
		}
		else
		{
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
		}
	}
	
	/**
	 * @param activeChar
	 * @param command
	 */
	private void showMainPage(L2PcInstance activeChar, String command)
	{
		if (command.contains("transform"))
		{
			AdminHelpPage.showHelpPage(activeChar, "transform.htm");
		}
		else if (command.contains("abnormal"))
		{
			AdminHelpPage.showHelpPage(activeChar, "abnormal.htm");
		}
		else
		{
			AdminHelpPage.showHelpPage(activeChar, "effects_menu.htm");
		}
	}
}
