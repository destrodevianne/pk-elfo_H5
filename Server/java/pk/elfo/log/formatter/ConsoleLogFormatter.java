package pk.elfo.log.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import pk.elfo.Config;
import pk.elfo.util.StringUtil;
import pk.elfo.util.Util;

/**
 * This class ...
 * @version $Revision: 1.1.4.2 $ $Date: 2005/03/27 15:30:08 $
 */
public class ConsoleLogFormatter extends Formatter
{
	private final SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm:ss");
	
	@Override
	public String format(LogRecord record)
	{
		final StringBuilder output = new StringBuilder(500);

		StringUtil.append(output, " PKELFO: " + record.getMessage(), Config.EOL);

		if (record.getThrown() != null)
		{
			try
			{
				StringUtil.append(output, Util.getStackTrace(record.getThrown()), Config.EOL);
			}
			catch (Exception ex)
			{
			}
		}
		return output.toString();
	}
}
