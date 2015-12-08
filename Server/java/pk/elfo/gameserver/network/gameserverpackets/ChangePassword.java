package pk.elfo.gameserver.network.gameserverpackets;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.datatables.CharNameTable;
import pk.elfo.util.network.BaseSendablePacket;

public class ChangePassword extends BaseSendablePacket
{
	public ChangePassword(String accountName, String characterName, String oldPass, String newPass)
	{
		Date d = new Date();
		writeC(0x0B);
		writeS(accountName);
		writeS(characterName);
		writeS(oldPass);
		writeS(newPass);
		
		// Grabando la password en una tabla para poder tener acceso desde la web.
		// La idea es grabar el nombre de la cuenta, la nueva contraseña y la fecha en que se hizo, mas la ID de la operacion.
		// Ejecuta este SQL en tu base de datos para crear la tabla.
		/*
		CREATE TABLE `account_changepassword_log` (
		`ID` int(11) NOT NULL AUTO_INCREMENT,
		`date` bigint(20) NOT NULL,
		`account` varchar(255) NOT NULL,
	 	`password` varchar(255) NOT NULL,
	 	PRIMARY KEY (`ID`)
		) ENGINE=InnoDB;
		 */
		try {
			String query = "Insert INTO account_changepassword_log (date,account,password) VALUES ('" + d.getTime() + "','" + accountName + "','" + newPass + "')";
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			Statement st = con.createStatement();
			st.execute(query);
			con.close();
		}
		catch (SQLException e)
		{
			Logger.getLogger(CharNameTable.class.getName()).log(Level.WARNING, "No se pudo grabar la nueva password para la cuenta: " + accountName + "\r\n " + e.getMessage(), e);
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}