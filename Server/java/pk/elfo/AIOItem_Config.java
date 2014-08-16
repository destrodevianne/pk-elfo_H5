package pk.elfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import pk.elfo.util.L2Properties;

/**
 * PkElfo
 */

public final class AIOItem_Config
{
    private static final Logger _log = Logger.getLogger(AIOItem_Config.class.getName());

    private static final String AIO_CONFIG_FILE = "./config/AIOx/AIOItem.properties";

    public static boolean AIOITEM_ENABLEME;
    public static boolean AIOITEM_ENABLESHOP;
    public static boolean AIOITEM_ENABLEGK;
    public static boolean AIOITEM_ENABLEWH;
    public static boolean AIOITEM_ENABLEBUFF;
    public static boolean AIOITEM_ENABLESCHEMEBUFF;
    public static boolean AIOITEM_ENABLESERVICES;
    public static boolean AIOITEM_ENABLESUBCLASS;
    public static boolean AIOITEM_ENABLETOPLIST;
    public static boolean AIOITEM_ENABLESIEGEREG;
    public static int AIOITEM_GK_COIN;
    public static int AIOITEM_GK_PRICE;
    public static int AIOITEM_BUFF_COIN;
    public static int AIOITEM_BUFF_PRICE;
    public static int AIOITEM_SCHEME_COIN;
    public static int AIOITEM_SCHEME_PRICE;
    public static int AIOITEM_SCHEME_PROFILE_PRICE;
    public static int AIOITEM_SCHEME_MAX_PROFILES;
    public static int AIOITEM_SCHEME_MAX_PROFILE_BUFFS;

    public static void load()
    {
        InputStream is = null;
        try
        {
            try
            {
                L2Properties aioSettings = new L2Properties();
                is = new FileInputStream(new File(AIO_CONFIG_FILE));
                aioSettings.load(is);

                AIOITEM_ENABLEME = Boolean.parseBoolean(aioSettings.getProperty("EnableAIOItem", "false"));
                AIOITEM_ENABLESHOP = Boolean.parseBoolean(aioSettings.getProperty("EnableGMShop", "false"));
                AIOITEM_ENABLEGK = Boolean.parseBoolean(aioSettings.getProperty("EnableGk", "false"));
                AIOITEM_ENABLEWH = Boolean.parseBoolean(aioSettings.getProperty("EnableWh", "false"));
                AIOITEM_ENABLEBUFF = Boolean.parseBoolean(aioSettings.getProperty("EnableBuffer", "false"));
                AIOITEM_ENABLESCHEMEBUFF = Boolean.parseBoolean(aioSettings.getProperty("EnableSchemeBuffer", "false"));
                AIOITEM_ENABLESERVICES = Boolean.parseBoolean(aioSettings.getProperty("EnableServices", "false"));
                AIOITEM_ENABLESUBCLASS = Boolean.parseBoolean(aioSettings.getProperty("EnableSubclassManager", "false"));
                AIOITEM_ENABLETOPLIST = Boolean.parseBoolean(aioSettings.getProperty("EnableTopListManager", "false"));
                AIOITEM_ENABLESIEGEREG = Boolean.parseBoolean(aioSettings.getProperty("EnableSiegeRegistration", "false"));
                AIOITEM_GK_COIN = Integer.parseInt(aioSettings.getProperty("GkCoin", "57"));
                AIOITEM_GK_PRICE = Integer.parseInt(aioSettings.getProperty("GkPrice", "100"));
                AIOITEM_BUFF_COIN = Integer.parseInt(aioSettings.getProperty("BufferCoin", "57"));
                AIOITEM_BUFF_PRICE = Integer.parseInt(aioSettings.getProperty("BufferPrice", "100"));
                AIOITEM_SCHEME_COIN = Integer.parseInt(aioSettings.getProperty("SchemeCoin", "57"));
                AIOITEM_SCHEME_PRICE = Integer.parseInt(aioSettings.getProperty("SchemePrice", "100"));
                AIOITEM_SCHEME_PROFILE_PRICE = Integer.parseInt(aioSettings.getProperty("SchemeProfileCreationPrice", "1000"));
                AIOITEM_SCHEME_MAX_PROFILES = Integer.parseInt(aioSettings.getProperty("SchemeMaxProfiles", "4"));
                AIOITEM_SCHEME_MAX_PROFILE_BUFFS = Integer.parseInt(aioSettings.getProperty("SchemeMaxProfileBuffs", "24"));
            }
            catch (Exception e)
            {
                _log.warning("CustomConfig.load(): Couldn't load AIO Item settings. Reason:");
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            _log.warning("CustomConfig.load(): Problems during initialization. Reason:");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
    }
}