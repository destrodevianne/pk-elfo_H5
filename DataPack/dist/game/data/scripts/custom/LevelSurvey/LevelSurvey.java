package custom.LevelSurvey;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import pk.elfo.Config;
import pk.elfo.gameserver.engines.DocumentParser;
import pk.elfo.gameserver.instancemanager.GlobalVariablesManager;
import pk.elfo.gameserver.instancemanager.MailManager;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Message;
import pk.elfo.gameserver.model.entity.Message.SendBySystem;
import pk.elfo.gameserver.model.holders.ItemHolder;
import pk.elfo.gameserver.scripting.scriptengine.events.PlayerLevelChangeEvent;
import pk.elfo.gameserver.scripting.scriptengine.impl.L2Script;

public class LevelSurvey extends L2Script
{
	private static final Logger _log = Logger.getLogger(LevelSurvey.class.getName());
	
	public static final List<LevelContent> _levelData = new ArrayList<>();
	
	private final class Parser extends DocumentParser
	{
		public Parser()
		{
			parseFile(new File(Config.DATAPACK_ROOT, "data/scripts/custom/LevelSurvey/data.xml"));
		}
		
		@Override
		protected void parseDocument()
		{
			NamedNodeMap attrs;
			Node att;
			StatsSet set;
			LevelContent content;
			for (Node n = getCurrentDocument().getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if ("survey".equalsIgnoreCase(d.getNodeName()))
						{
							set = new StatsSet();
							attrs = d.getAttributes();
							for (int i = 0; i < attrs.getLength(); i++)
							{
								att = attrs.item(i);
								set.set(att.getNodeName(), att.getNodeValue());
							}
							content = new LevelContent(set);
							for (Node a = d.getFirstChild(); a != null; a = a.getNextSibling())
							{
								if ("item".equals(a.getNodeName()))
								{
									attrs = a.getAttributes();
									int itemId = parseInt(attrs.getNamedItem("id"));
									int itemCount = parseInt(attrs.getNamedItem("count"));
									content.addItem(new ItemHolder(itemId, itemCount));
								}
							}
							_levelData.add(content);
						}
					}
				}
			}
		}

		/* (non-Javadoc)
		 * @see pk.elfo.gameserver.engines.DocumentParser#load()
		 */
		@Override
		public void load()
		{
			// TODO Auto-generated method stub
		}
	}
	
	@Override
	public boolean unload(boolean removeFromList)
	{
		_levelData.clear();
		removeLoginLogoutNotify();
		return super.unload(removeFromList);
	}
	
	public LevelSurvey(String name, String desc)
	{
		super(-1, name, desc);
		load();
	}
	
	public void init()
	{
		addLoginLogoutNotify();
	}
	
	@Override
	public void onPlayerLevelChange(PlayerLevelChangeEvent event)
	{
		L2PcInstance player = event.getPlayer();
		for (LevelContent content : _levelData)
		{
			if (content.getLevel() <= player.getLevel())
			{
				String var = GlobalVariablesManager.getInstance().getStoredVariable("SURVEY-" + content.getLevel() + "-" + player.getObjectId());
				if (var == null)
				{
					Message msg = new Message(player.getObjectId(), content.getMailTitle(), content.getMailContent(), SendBySystem.NEWS);
					msg.createAttachments();
					for (ItemHolder holder : content.getItems())
					{
						msg.getAttachments().addItem(getClass().getSimpleName(), holder.getId(), holder.getCount(), null, null);
					}
					MailManager.getInstance().sendMessage(msg);
					GlobalVariablesManager.getInstance().storeVariable("SURVEY-" + content.getLevel() + "-" + player.getObjectId(), "true");
				}
			}
		}
	}
	
	@Override
	public void onPlayerLogin(L2PcInstance player)
	{
		addPlayerLevelNotify(player);
	}
	
	@Override
	public void onPlayerLogout(L2PcInstance player)
	{
		removePlayerLevelNotify(player);
	}
	
	public void load()
	{
		_levelData.clear();
		new Parser();
		_log.log(Level.INFO, getClass().getSimpleName() + ": Loaded " + _levelData.size() + " Level Surveys");
	}
	
	public static final class LevelContent
	{
		private final int _level;
		private final List<ItemHolder> _items;
		private final String _mailTitle;
		private final String _mailContent;
		
		public LevelContent(StatsSet set)
		{
			_level = set.getInteger("level");
			_items = new ArrayList<>();
			_mailTitle = set.getString("title");
			_mailContent = set.getString("content");
		}
		
		public int getLevel()
		{
			return _level;
		}
		
	public boolean addItem(ItemHolder holder)
		{
			return _items.add(holder);
		}
		
		public List<ItemHolder> getItems()
		{
			return _items;
		}
		
		public String getMailTitle()
		{
			return _mailTitle;
		}
		
		public String getMailContent()
		{
			return _mailContent;
		}
	}
	
	public static void main(String[] args)
	{
		new LevelSurvey(LevelSurvey.class.getSimpleName(), "custom");
	}
}