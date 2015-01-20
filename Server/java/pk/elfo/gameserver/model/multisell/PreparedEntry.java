package pk.elfo.gameserver.model.multisell;

import static pk.elfo.gameserver.model.itemcontainer.PcInventory.ADENA_ID;

import java.util.ArrayList;

import pk.elfo.gameserver.model.items.instance.L2ItemInstance;

public class PreparedEntry extends Entry
{
	private long _taxAmount = 0;
	
	public PreparedEntry(Entry template, L2ItemInstance item, boolean applyTaxes, boolean maintainEnchantment, double taxRate)
	{
		_entryId = template.getEntryId() * 100000;
		if (maintainEnchantment && (item != null))
		{
			_entryId += item.getEnchantLevel();
		}
		
		ItemInfo info = null;
		long adenaAmount = 0;
		
		_ingredients = new ArrayList<>(template.getIngredients().size());
		for (Ingredient ing : template.getIngredients())
		{
			if (ing.getItemId() == ADENA_ID)
			{
				// Tax ingredients added only if taxes enabled
				if (ing.isTaxIngredient())
				{
					// if taxes are to be applied, modify/add the adena count based on the template adena/ancient adena count
					if (applyTaxes)
					{
						_taxAmount += Math.round(ing.getItemCount() * taxRate);
					}
				}
				else
				{
					adenaAmount += ing.getItemCount();
				}
				// do not yet add this adena amount to the list as non-taxIngredient adena might be entered later (order not guaranteed)
				continue;
			}
			else if (maintainEnchantment && (item != null) && ing.isArmorOrWeapon())
			{
				info = new ItemInfo(item);
				final Ingredient newIngredient = ing.getCopy();
				newIngredient.setItemInfo(info);
				_ingredients.add(newIngredient);
			}
			else
			{
				final Ingredient newIngredient = ing.getCopy();
				_ingredients.add(newIngredient);
			}
		}
		
		// now add the adena, if any.
		adenaAmount += _taxAmount; // do not forget tax
		if (adenaAmount > 0)
		{
			_ingredients.add(new Ingredient(ADENA_ID, adenaAmount,0, false, false));
		}
		
		// now copy products
		_products = new ArrayList<>(template.getProducts().size());
		for (Ingredient ing : template.getProducts())
		{
			if (!ing.isStackable())
			{
				_stackable = false;
			}
			
			final Ingredient newProduct = ing.getCopy();
			if (maintainEnchantment && ing.isArmorOrWeapon())
			{
				newProduct.setItemInfo(info);
			}
			_products.add(newProduct);
		}
	}
	
	@Override
	public final long getTaxAmount()
	{
		return _taxAmount;
	}
}