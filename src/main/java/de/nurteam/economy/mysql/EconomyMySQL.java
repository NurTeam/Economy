package de.nurteam.economy.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EconomyMySQL {

	public String username;
	public String password;
	public String database;
	public String host;
	public String port;
	public Connection con;
	
	public void connect()
	{
		if(!isConnected())
		{
			try
			{
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port+ "/" + database+ "?autoReconnect=true", username, password);
				System.out.println("MySQL Database Connection opened.");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void close()
	{
		if(isConnected())
		{
			try
			{
				con.close();
				System.out.println("MySQL Database Connection closed.");
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected()
	{
		return con != null;
	}

	public void createTable()
	{
		//TODO: Syntax: UUID, Euros, Cents, PIN, createDate
		if(isConnected())
		{
			try {
				con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS Economy (UUID VARCHAR(100), Euros VARCHAR(100), Cents VARCHAR(100), PIN VARCHAR(100), createDate VARCHAR(100))");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	public void update(String qry)
	{
		if(isConnected())
		{
			try
			{
				con.createStatement().executeUpdate(qry);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public ResultSet getResult(String qry)
	{
		if(isConnected()) {
			try {
				return con.createStatement().executeQuery(qry);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
