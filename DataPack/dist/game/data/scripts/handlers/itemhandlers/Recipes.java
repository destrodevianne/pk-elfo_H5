package handlers.itemhandlers;

import pk.elfo.gameserver.datatables.RecipeData;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.L2RecipeList;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class Recipes implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		if (activeChar.isInCraftMode())
		{
			activeChar.sendPacket(SystemMessageId.CANT_ALTER_RECIPEBOOK_WHILE_CRAFTING);
			return false;
		}
		
		final L2RecipeList rp = RecipeData.getInstance().getRecipeByItemId(item.getItemId());
		if (rp == null)
		{
			return false;
		}
		
		if (activeChar.hasRecipeList(rp.getId()))
		{
			activeChar.sendPacket(SystemMessageId.RECIPE_ALREADY_REGISTERED);
			return false;
		}
		
		boolean canCraft = false;
		boolean recipeLevel = false;
		boolean recipeLimit = false;
		if (rp.isDwarvenRecipe())
		{
			canCraft = activeChar.hasDwarvenCraft();
			recipeLevel = (rp.getLevel() > activeChar.getDwarvenCraft());
			recipeLimit = (activeChar.getDwarvenRecipeBook().length >= activeChar.getDwarfRecipeLimit());
		}
		else
		{
			canCraft = activeChar.hasCommonCraft();
			recipeLevel = (rp.getLevel() > activeChar.getCommonCraft());
			recipeLimit = (activeChar.getCommonRecipeBook().length >= activeChar.getCommonRecipeLimit());
		}
		
		if (!canCraft)
		{
			activeChar.sendPacket(SystemMessageId.CANT_REGISTER_NO_ABILITY_TO_CRAFT);
			return false;
		}
		
		if (recipeLevel)
		{
			activeChar.sendPacket(SystemMessageId.CREATE_LVL_TOO_LOW_TO_REGISTER);
			return false;
		}
		
		if (recipeLimit)
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.UP_TO_S1_RECIPES_CAN_REGISTER);
			sm.addNumber(rp.isDwarvenRecipe() ? activeChar.getDwarfRecipeLimit() : activeChar.getCommonRecipeLimit());
			activeChar.sendPacket(sm);
			return false;
		}
		
		if (rp.isDwarvenRecipe())
		{
			activeChar.registerDwarvenRecipeList(rp, true);
		}
		else
		{
			activeChar.registerCommonRecipeList(rp, true);
		}
		
		activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false);
		final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_ADDED);
		sm.addItemName(item);
		activeChar.sendPacket(sm);
		return true;
	}
}