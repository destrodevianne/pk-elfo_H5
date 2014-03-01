package handlers.telnethandlers;

import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.handler.ITelnetHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.Inventory;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.CharInfo;
import pk.elfo.gameserver.network.serverpackets.ExBrExtraUserInfo;
import pk.elfo.gameserver.network.serverpackets.InventoryUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.network.serverpackets.UserInfo;
import pk.elfo.gameserver.util.GMAudit;

/**
 * PkElfo
 */

public class PlayerHandler implements ITelnetHandler
{
	private final String[] _commands =
	{
		"kick",
		"give",
		"enchant",
		"jail",
		"unjail"
	};
	
	@Override
	public boolean useCommand(String command, PrintWriter _print, Socket _cSocket, int _uptime)
	{
		if (command.startsWith("kick"))
		{
			try
			{
				command = command.substring(5);
				L2PcInstance player = L2World.getInstance().getPlayer(command);
				if (player != null)
				{
					player.sendMessage("Voce foi chutado por um gm");
					player.logout();
					_print.println("Jogador chutado");
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				_print.println("Por favor, insira o nome do jogador para chutar");
			}
		}
		else if (command.startsWith("give"))
		{
			StringTokenizer st = new StringTokenizer(command.substring(5));
			
			try
			{
				L2PcInstance player = L2World.getInstance().getPlayer(st.nextToken());
				int itemId = Integer.parseInt(st.nextToken());
				int amount = Integer.parseInt(st.nextToken());
				
				if (player != null)
				{
					L2ItemInstance item = player.getInventory().addItem("Status-Give", itemId, amount, null, null);
					InventoryUpdate iu = new InventoryUpdate();
					iu.addItem(item);
					player.sendPacket(iu);
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_PICKED_UP_S1_S2);
					sm.addItemName(itemId);
					sm.addItemNumber(amount);
					player.sendPacket(sm);
					_print.println("ok");
					GMAudit.auditGMAction("Telnet Admin", "Give Item", player.getName(), "item: " + itemId + " amount: " + amount);
				}
				else
				{
					_print.println("Jogador nao encontrado");
				}
			}
			catch (Exception e)
			{
				
			}
		}
		else if (command.startsWith("enchant"))
		{
			StringTokenizer st = new StringTokenizer(command.substring(8), " ");
			int enchant = 0, itemType = 0;
			
			try
			{
				L2PcInstance player = L2World.getInstance().getPlayer(st.nextToken());
				itemType = Integer.parseInt(st.nextToken());
				enchant = Integer.parseInt(st.nextToken());
				
				switch (itemType)
				{
					case 1:
						itemType = Inventory.PAPERDOLL_HEAD;
						break;
					case 2:
						itemType = Inventory.PAPERDOLL_CHEST;
						break;
					case 3:
						itemType = Inventory.PAPERDOLL_GLOVES;
						break;
					case 4:
						itemType = Inventory.PAPERDOLL_FEET;
						break;
					case 5:
						itemType = Inventory.PAPERDOLL_LEGS;
						break;
					case 6:
						itemType = Inventory.PAPERDOLL_RHAND;
						break;
					case 7:
						itemType = Inventory.PAPERDOLL_LHAND;
						break;
					case 8:
						itemType = Inventory.PAPERDOLL_LEAR;
						break;
					case 9:
						itemType = Inventory.PAPERDOLL_REAR;
						break;
					case 10:
						itemType = Inventory.PAPERDOLL_LFINGER;
						break;
					case 11:
						itemType = Inventory.PAPERDOLL_RFINGER;
						break;
					case 12:
						itemType = Inventory.PAPERDOLL_NECK;
						break;
					case 13:
						itemType = Inventory.PAPERDOLL_UNDER;
						break;
					case 14:
						itemType = Inventory.PAPERDOLL_CLOAK;
						break;
					case 15:
						itemType = Inventory.PAPERDOLL_BELT;
						break;
					default:
						itemType = 0;
				}
				
				if (enchant > 65535)
				{
					enchant = 65535;
				}
				else if (enchant < 0)
				{
					enchant = 0;
				}
				
				boolean success = false;
				
				if ((player != null) && (itemType > 0))
				{
					success = setEnchant(player, enchant, itemType);
					if (success)
					{
						_print.println("Item encantado com sucesso.");
					}
				}
				else if (!success)
				{
					_print.println("Item falhou ao encantar.");
				}
			}
			catch (Exception e)
			{
				
			}
		}
		else if (command.startsWith("jail"))
		{
			StringTokenizer st = new StringTokenizer(command.substring(5));
			try
			{
				String name = st.nextToken();
				L2PcInstance playerObj = L2World.getInstance().getPlayer(name);
				int delay = 0;
				try
				{
					delay = Integer.parseInt(st.nextToken());
				}
				catch (NumberFormatException nfe)
				{
				}
				catch (NoSuchElementException nsee)
				{
				}
				// L2PcInstance playerObj = L2World.getInstance().getPlayer(player);
				
				if (playerObj != null)
				{
					playerObj.setPunishLevel(L2PcInstance.PunishLevel.JAIL, delay);
					_print.println("O Personagem " + playerObj.getName() + " foi preso por " + (delay > 0 ? delay + " minutos." : "ever!"));
				}
				else
				{
					try (Connection con = L2DatabaseFactory.getInstance().getConnection())
					{
						PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, punish_level=?, punish_timer=? WHERE char_name=?");
						statement.setInt(1, -114356);
						statement.setInt(2, -249645);
						statement.setInt(3, -2984);
						statement.setInt(4, L2PcInstance.PunishLevel.JAIL.value());
						statement.setLong(5, delay * 60000L);
						statement.setString(6, name);
						
						statement.execute();
						int count = statement.getUpdateCount();
						statement.close();
						
						if (count == 0)
						{
							_print.println("Personagem nao encontrado!");
						}
						else
						{
							_print.println("O Personagem " + name + " foi preso por " + (delay > 0 ? delay + " minutos." : "ever!"));
						}
					}
					catch (SQLException se)
					{
						_print.println("SQLException excecao ao jogador na prisao");
						if (Config.DEBUG)
						{
							se.printStackTrace();
						}
					}
				}
			}
			catch (NoSuchElementException nsee)
			{
				_print.println("Especifique o nome do personagem.");
			}
			catch (Exception e)
			{
				if (Config.DEBUG)
				{
					e.printStackTrace();
				}
			}
		}
		else if (command.startsWith("unjail"))
		{
			StringTokenizer st = new StringTokenizer(command.substring(7));
			try
			{
				String name = st.nextToken();
				L2PcInstance playerObj = L2World.getInstance().getPlayer(name);
				
				if (playerObj != null)
				{
					playerObj.setPunishLevel(L2PcInstance.PunishLevel.NONE, 0);
					_print.println("O Personagem " + playerObj.getName() + " foi tirado da cadeia");
				}
				else
				{
					try (Connection con = L2DatabaseFactory.getInstance().getConnection())
					{
						PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, punish_level=?, punish_timer=? WHERE char_name=?");
						statement.setInt(1, 17836);
						statement.setInt(2, 170178);
						statement.setInt(3, -3507);
						statement.setInt(4, 0);
						statement.setLong(5, 0);
						statement.setString(6, name);
						
						statement.execute();
						int count = statement.getUpdateCount();
						statement.close();
						
						if (count == 0)
						{
							_print.println("Personagem nao encontrado!");
						}
						else
						{
							_print.println("O Prsonagem " + name + " foi solto.");
						}
					}
					catch (SQLException se)
					{
						_print.println("SQLException excecao ao jogador na prisao");
						if (Config.DEBUG)
						{
							se.printStackTrace();
						}
					}
				}
			}
			catch (NoSuchElementException nsee)
			{
				_print.println("Especifique o nome do personagem.");
			}
			catch (Exception e)
			{
				if (Config.DEBUG)
				{
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	private boolean setEnchant(L2PcInstance activeChar, int ench, int armorType)
	{
		// now we need to find the equipped weapon of the targeted character...
		int curEnchant = 0; // display purposes only
		L2ItemInstance itemInstance = null;
		
		// only attempt to enchant if there is a weapon equipped
		L2ItemInstance parmorInstance = activeChar.getInventory().getPaperdollItem(armorType);
		if ((parmorInstance != null) && (parmorInstance.getLocationSlot() == armorType))
		{
			itemInstance = parmorInstance;
		}
		else
		{
			// for bows/crossbows and double handed weapons
			parmorInstance = activeChar.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
			if ((parmorInstance != null) && (parmorInstance.getLocationSlot() == Inventory.PAPERDOLL_RHAND))
			{
				itemInstance = parmorInstance;
			}
		}
		
		if (itemInstance != null)
		{
			curEnchant = itemInstance.getEnchantLevel();
			
			// set enchant value
			activeChar.getInventory().unEquipItemInSlot(armorType);
			itemInstance.setEnchantLevel(ench);
			activeChar.getInventory().equipItem(itemInstance);
			
			// send packets
			InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(itemInstance);
			activeChar.sendPacket(iu);
			activeChar.broadcastPacket(new CharInfo(activeChar));
			activeChar.sendPacket(new UserInfo(activeChar));
			activeChar.broadcastPacket(new ExBrExtraUserInfo(activeChar));
			
			// informations
			activeChar.sendMessage("Mudanca de encantamento do " + activeChar.getName() + "'s " + itemInstance.getItem().getName() + " a partir de " + curEnchant + " para " + ench + ".");
			activeChar.sendMessage("O Administrador mudou o encantamento da seu " + itemInstance.getItem().getName() + " a partir de " + curEnchant + " para " + ench + ".");
			
			// log
			GMAudit.auditGMAction("TelnetAdministrator", "enchant", activeChar.getName(), itemInstance.getItem().getName() + "(" + itemInstance.getObjectId() + ")" + " from " + curEnchant + " to " + ench);
			return true;
		}
		return false;
	}
	
	@Override
	public String[] getCommandList()
	{
		return _commands;
	}
}