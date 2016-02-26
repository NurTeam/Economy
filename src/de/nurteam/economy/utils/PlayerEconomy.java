package de.nurteam.economy.utils;

import java.util.Date;
import java.util.UUID;

public class PlayerEconomy
{

	private UUID uuid;
	private long euros;
	private long cents;
	private String encodedPin;
	private Date date;

	public PlayerEconomy(UUID uuid, long euros, long cents, String encodedPin, Date date)
	{
		this.uuid = uuid;
		this.euros = euros;
		this.cents = cents;
		this.encodedPin = encodedPin;
		this.date = date;
	}

	public long getCents()
	{
		return cents;
	}
	
	public Date getCreateDate()
	{
		return date;
	}

	public String getEncodedPin()
	{
		return encodedPin;
	}

	public long getEuros()
	{
		return euros;
	}

	public UUID getUUID()
	{
		return uuid;
	}
	
	public void setCents(long cents)
	{
		this.cents = cents;
	}
	
	public void setEncodedPin(String encodedPin)
	{
		this.encodedPin = encodedPin;
	}
	
	public void setEuros(long euros)
	{
		this.euros = euros;
	}
	
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}
	
	public void setCreateDate(Date date) {
		this.date = date;
	}
	
}
