/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.gameserver.network.gameserverpackets;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import king.server.L2DatabaseFactory;
import king.server.gameserver.datatables.CharNameTable;
import king.server.util.network.BaseSendablePacket;

/**
 * PkElfo
 */
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