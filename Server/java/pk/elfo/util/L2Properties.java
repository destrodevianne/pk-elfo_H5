package pk.elfo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Specialized {@link java.util.Properties} class.<br>
 * Simplifies loading of property files and adds logging if a non existing property is requested.<br>
 */
 
public final class L2Properties extends Properties
{
	private static final long serialVersionUID = 1L;
	
	private static Logger _log = Logger.getLogger(L2Properties.class.getName());
	
	public L2Properties()
	{
	}
	
	public L2Properties(String name) throws IOException
	{
		try (FileInputStream fis = new FileInputStream(name))
		{
			load(fis);
		}
	}
	
	public L2Properties(File file) throws IOException
	{
		try (FileInputStream fis = new FileInputStream(file))
		{
			load(fis);
		}
	}
	
	public L2Properties(InputStream inStream) throws IOException
	{
		load(inStream);
	}
	
	public L2Properties(Reader reader) throws IOException
	{
		load(reader);
	}
	
	public void load(String name) throws IOException
	{
		try (FileInputStream fis = new FileInputStream(name))
		{
			load(fis);
		}
	}
	
	public void load(File file) throws IOException
	{
		try (FileInputStream fis = new FileInputStream(file))
		{
			load(fis);
		}
	}
	
	@Override
	public void load(InputStream inStream) throws IOException
	{
		try (InputStreamReader isr = new InputStreamReader(inStream, Charset.defaultCharset()))
		{
			super.load(isr);
		}
		finally
		{
			inStream.close();
		}
	}
	
	@Override
	public void load(Reader reader) throws IOException
	{
		try
		{
			super.load(reader);
		}
		finally
		{
			reader.close();
		}
	}
	
	@Override
	public String getProperty(String key)
	{
		String property = super.getProperty(key);
		
		if (property == null)
		{
			_log.info("L2Properties: Faltando propriedade para chave - " + key);
			
			return null;
		}
		return property.trim();
	}
	
	@Override
	public String getProperty(String key, String defaultValue)
	{
		String property = super.getProperty(key, defaultValue);
		
		if (property == null)
		{
			_log.warning("L2Properties: Faltando propriedade para chave - " + key);
			
			return null;
		}
		return property.trim();
	}
	
	// getString
	public String getString(String key, String defaultValue)
	{
		String value = getProperty(key);
		if (value == null)
		{
			_log.warning("Faltando propriedade para a chave: " + key + " usando o valor padrao: " + defaultValue);
			return defaultValue;
		}
		return value;
	}
	
    // getBoolean
    public boolean getBoolean(String key, boolean defaultValue)
    {
        String value = getProperty(key);
        if (value == null)
        {
            _log.warning("Faltando propriedade para chave: " + key + " usando o valor padrao: " + defaultValue);
            return defaultValue;
        }

        if (value.equalsIgnoreCase("true"))
        {
            return true;
        }
        else if (value.equalsIgnoreCase("false"))
        {
            return false;
        }
        else
        {
            _log.warning("Valor invalido especificado para a chave: " + key + " valor especificado: " + value + " deve ser \"boolean\" usando o valor padrao: " + defaultValue);
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue)
    {
        String value = getProperty(key);
        if (value == null)
        {
            _log.warning("Faltando propriedade para chave: " + key + " usando o valor padrao: " + defaultValue);
            return defaultValue;
        }

        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            _log.warning("Valor invalido especificado para a chave: " + key + " valor especificado: " + value + " deve ser \"int\" usando o valor padrao: " + defaultValue);
            return defaultValue;
        }
    }
}