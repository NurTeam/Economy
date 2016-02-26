package de.nurteam.economy.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.nurteam.economy.Economy;
import de.nurteam.economy.mysql.EconomyMySQL;

public class EconomyManager
{
	Economy economy;
	EconomyMySQL mySQL;
	
	private Map<UUID, PlayerEconomy> economies = new HashMap<UUID, PlayerEconomy>();
	
	public EconomyManager(Economy economy)
	{
		this.economy = economy;
		this.mySQL = this.economy.mySQL;
	}
	
	//TODO: Syntax: UUID, Euros, Cents, PIN
	
	public void createAccount(UUID uuid, String encodedPin)
	{
		if(!hasAccount(uuid))
		{
			Calendar calendar = Calendar.getInstance();
			economies.put(uuid, new PlayerEconomy(uuid, 0, 0, encodedPin, calendar.getTime()));
			uploadToMySQL(uuid);
			return;
		}
		System.err.println("Cannot create account for uuid " + uuid.toString() + " because it already exists.");
	}
	
	public void deleteAccount(UUID uuid)
	{
		if(hasAccount(uuid))
		{
			economies.remove(uuid);
			deleteFromMySQL(uuid);
			return;
		}
		System.err.println("Cannot delete account for uuid " + uuid.toString() + " because it does not exist.");
	}
	
	private void deleteFromMySQL(UUID uuid)
	{
		if(hasAccount(uuid))
		{
			mySQL.update("DELETE FROM Economy WHERE UUID='" + uuid.toString() + "'");
		}
	}
	
	public PlayerEconomy getPlayerEconomy(UUID uuid)
	{
		return economies.get(uuid);
	}
	
	@SuppressWarnings("deprecation")
	public void uploadToMySQL(UUID uuid)
	{
		PlayerEconomy playerEconomy = getPlayerEconomy(uuid);
		Date original = playerEconomy.getCreateDate();
		
		String date = original.getDate() + "_" + original.getMonth() + "_" + original.getYear() + "_" + original.getHours() + "_" + original.getMinutes();
		
		if(!hasAccount(uuid))
		{
			
			mySQL.update("INSERT INTO Economy (UUID, Euros, Cents, PIN, createDate) VALUES ('" + uuid.toString() + "', '" + playerEconomy.getEuros() + "', '" + playerEconomy.getCents() + "', '" + playerEconomy.getEncodedPin() + "', '" + date + "')");
			return;
		}
		
		mySQL.update("UPDATE Economy SET Euros='" + playerEconomy.getEuros() + "' WHERE UUID='" + uuid.toString() + "'");
		mySQL.update("UPDATE Economy SET Cents='" + playerEconomy.getCents() + "' WHERE UUID='" + uuid.toString() + "'");
		mySQL.update("UPDATE Economy SET PIN='" + playerEconomy.getEncodedPin() + "' WHERE UUID='" + uuid.toString() + "'");
		mySQL.update("UPDATE Economy SET createDate='" + date + "' WHERE UUID='" + uuid.toString() + "'");
	}
	
	public void loadFromMySQL(UUID uuid)
	{	
		long euros = getEuros(uuid);
		long cents = getCents(uuid);
		String encodedPin = getEncodedPin(uuid);
		Date date = getCreateDate(uuid);
		
		economies.put(uuid, new PlayerEconomy(uuid, euros, cents, encodedPin, date));
	}
	
	public void addMoney(UUID uuid, long euros, long cents)
	{
		PlayerEconomy playerEconomy = economies.get(uuid);
		
		playerEconomy.setCents(playerEconomy.getCents() + cents);
		playerEconomy.setEuros(playerEconomy.getEuros() + euros);
		
		economies.remove(uuid);
		economies.put(uuid, playerEconomy);
	}
	
	public void removeMoney(UUID uuid, long euros, long cents)
	{
		PlayerEconomy playerEconomy = getPlayerEconomy(uuid);
		
		playerEconomy.setCents(playerEconomy.getCents() - cents);
		playerEconomy.setEuros(playerEconomy.getEuros() - euros);
		
		economies.remove(uuid);
		economies.put(uuid, playerEconomy);
	}
	
	public boolean hasEnoughMoney(UUID uuid, long euros, long cents)
	{
		PlayerEconomy playerEconomy = getPlayerEconomy(uuid);

		long playerCents = playerEconomy.getCents() + (playerEconomy.getEuros() * 100);
		long neededCents = cents + (euros * 100);

		if(playerCents >= neededCents)
		{
			return true;
		}
		
		return false;
	}

	private long getEuros(UUID uuid)
	{
		ResultSet rs = mySQL.getResult("SELECT * FROM Economy WHERE UUID='" + uuid.toString() + "'");
		try
		{
			while(rs.next())
			{
				return rs.getLong("Euros");
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	private Date getCreateDate(UUID uuid)
	{
		ResultSet rs = mySQL.getResult("SELECT * FROM Economy WHERE UUID='" + uuid.toString() + "'");
		try
		{
			while(rs.next()) {
				String[] original = rs.getString("createDate").split("_");
				Date date = new Date(Integer.valueOf(original[2]), Integer.valueOf(original[1]), Integer.valueOf(original[0]), Integer.valueOf(original[3]), Integer.valueOf(original[4]));
				return date;
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private long getCents(UUID uuid)
	{
		ResultSet rs = mySQL.getResult("SELECT * FROM Economy WHERE UUID='" + uuid.toString() + "'");
		try
		{
			while(rs.next()) {
				return rs.getLong("Cents");
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	private String getEncodedPin(UUID uuid)
	{
		ResultSet rs = mySQL.getResult("SELECT * FROM Economy WHERE UUID='" + uuid.toString() + "'");
		try
		{
			while(rs.next()) {
				return rs.getString("PIN");
			}
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}


	public boolean hasAccount(UUID uuid)
	{
		ResultSet rs = mySQL.getResult("SELECT UUID FROM Economy WHERE UUID='" + uuid.toString() + "'");
		try
		{
			return rs.next();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
