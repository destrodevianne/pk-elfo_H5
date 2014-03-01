package handlers.bypasshandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.SevenSigns;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2WyvernManagerInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.Util;

/**
 * PkElfo
 */

public class RideWyvern implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"RideWyvern"
	};
	
	private static final int[] STRIDERS =
	{
		12526,
		12527,
		12528,
		16038,
		16039,
		16040,
		16068,
		13197
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2WyvernManagerInstance))
		{
			return false;
		}
		
		L2WyvernManagerInstance npc = (L2WyvernManagerInstance) target;
		if (!npc.isOwnerClan(activeChar))
		{
			return false;
		}
		
		if (!Config.ALLOW_WYVERN_DURING_SIEGE && (npc.isInSiege() || activeChar.isInSiege()))
		{
			activeChar.sendMessage("Voce nao pode montar em um wyvern durante a siege.");
			return false;
		}
		
		if ((SevenSigns.getInstance().getSealOwner(SevenSigns.SEAL_STRIFE) == SevenSigns.CABAL_DUSK) && SevenSigns.getInstance().isSealValidationPeriod())
		{
			activeChar.sendMessage("Voce nao pode montar em um wyvern enquanto o Seal of Strife esiver sendo controlado por Dusk.");
			return false;
		}
		
		if (!activeChar.hasSummon())
		{
			if (activeChar.isMounted())
			{
				activeChar.sendPacket(SystemMessageId.YOU_ALREADY_HAVE_A_PET);
			}
			else
			{
				activeChar.sendMessage("Chame o seu primeiro Strider.");
			}
		}
		else if (Util.contains(STRIDERS, activeChar.getSummon().getNpcId()))
		{
			if ((activeChar.getInventory().getItemByItemId(1460) != null) && (activeChar.getInventory().getItemByItemId(1460).getCount() >= 25))
			{
				if (activeChar.getSummon().getLevel() < 55)
				{
					activeChar.sendMessage("Seu Strider nao atingiu o nivel exigido.");
				}
				else
				{
					activeChar.getSummon().unSummon(activeChar);
					if (activeChar.mount(12621, 0, true))
					{
						activeChar.getInventory().destroyItemByItemId("Wyvern", 1460, 25, activeChar, npc);
						activeChar.addSkill(SkillTable.FrequentSkill.WYVERN_BREATH.getSkill());
						activeChar.sendMessage("O Wyvern foi convocado com sucesso!");
					}
					return true;
				}
			}
			else
			{
				activeChar.sendMessage("Voce precisa de 25 Crystals: B Grade.");
			}
		}
		else
		{
			activeChar.sendMessage("Para esconjurar seu animal de estimacao.");
		}
		
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}