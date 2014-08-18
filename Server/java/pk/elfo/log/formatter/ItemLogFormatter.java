package pk.elfo.log.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import pk.elfo.Config;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.util.StringUtil;

public class ItemLogFormatter extends Formatter
{
	private final SimpleDateFormat dateFmt = new SimpleDateFormat("dd MMM H:mm:ss");
	
	@Override
	public String format(LogRecord record)
	{
		final Object[] params = record.getParameters();
		final StringBuilder output = StringUtil.startAppend(30 + record.getMessage().length() + (params.length * 50), "[", dateFmt.format(new Date(record.getMillis())), "] ", record.getMessage());
		
		for (Object p : record.getParameters())
		{
			if (p == null)
			{
				continue;
			}
			output.append(", ");
			if (p instanceof L2ItemInstance)
			{
				L2ItemInstance item = (L2ItemInstance) p;
				StringUtil.append(output, "item ", String.valueOf(item.getObjectId()), ":");
				if (item.getEnchantLevel() > 0)
				{
					StringUtil.append(output, "+", String.valueOf(item.getEnchantLevel()), " ");
				}
				
				StringUtil.append(output, item.getItem().getName(), "(", String.valueOf(item.getCount()), ")");
			}
			// else if (p instanceof L2PcInstance)
			// output.append(((L2PcInstance)p).getName());
			else
			{
				output.append(p.toString()/* + ":" + ((L2Object)p).getObjectId() */);
			}
		}
		output.append(Config.EOL);
		
		return output.toString();
	}
}