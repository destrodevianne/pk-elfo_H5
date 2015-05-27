package pk.elfo.log.formatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import pk.elfo.Config;
import pk.elfo.util.StringUtil;
import pk.elfo.util.Util;

public class ConsoleLogFormatter extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		final StringBuilder output = new StringBuilder(500);
		StringUtil.append(output, record.getMessage(), Config.EOL);

		if (record.getThrown() != null)
		{
			try
			{
				StringUtil.append(output, Util.getStackTrace(record.getThrown()), Config.EOL);
			}
			catch (Exception ex) { }
		}
		return output.toString();
	}
}