package pk.elfo.log.formatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import pk.elfo.Config;

public class GMAuditFormatter extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		return record.getMessage() + Config.EOL;
	}
}