package de.nurteam.economy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.nurteam.economy.command.EconomyCommand;
import de.nurteam.economy.listeners.EconomyChatListener;
import de.nurteam.economy.listeners.EconomyInventoryClickListener;
import de.nurteam.economy.listeners.EconomyPlayerJoinListener;
import de.nurteam.economy.mysql.EconomyMySQL;
import de.nurteam.economy.utils.EconomyCalculator;
import de.nurteam.economy.utils.EconomyFileManager;
import de.nurteam.economy.utils.EconomyManager;

public class Economy extends JavaPlugin
{
	
	public EconomyMySQL mySQL;
	public static EconomyManager economyManager;
	public EconomyFileManager fileManager;
	public EconomyCalculator calculator;
	public List<UUID> settingPin = new ArrayList<>();
	public List<UUID> deletingAccount = new ArrayList<>();
	public List<UUID> checkingForPin = new ArrayList<>();
	
	@Override
	public void onEnable()
	{
		this.mySQL = new EconomyMySQL();
		Economy.economyManager = new EconomyManager(this);
		this.fileManager = new EconomyFileManager(this);
		this.fileManager.setStandardMySQL();
		this.fileManager.readMySQL();
		this.mySQL.connect();
		this.mySQL.createTable();
		this.calculator = new EconomyCalculator(this);
		registerListener(new EconomyPlayerJoinListener(this));
		registerListener(new EconomyChatListener(this));
		registerListener(new EconomyInventoryClickListener(this));
		getCommand("economy").setExecutor(new EconomyCommand(this));
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(economyManager.hasAccount(player.getUniqueId()))
			{
				economyManager.loadFromMySQL(player.getUniqueId());
			}
		}
		Bukkit.getConsoleSender().sendMessage(getPrefix() + "Das Plugin wurde erfolgreich §aaktiviert §8| §7Plugin by " + getHighlight() + getDescription().getAuthors() + " §8| §7Version " + getHighlight() + getDescription().getVersion());
	}
	
	@Override
	public void onDisable() {
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(economyManager.getPlayerEconomy(player.getUniqueId()) != null)
			{
				economyManager.uploadToMySQL(player.getUniqueId());
			}
		}
		Bukkit.getConsoleSender().sendMessage(getPrefix() + "Das Plugin wurde erfolgreich §cdeaktiviert §8| §7Plugin by " + getHighlight() + getDescription().getAuthors() + " §8| §7Version " + getHighlight() + getDescription().getVersion());
	}
	
	private void registerListener(Listener listener)
	{
		Bukkit.getPluginManager().registerEvents(listener, this);
	}
	
	public String getPrefix()
	{
		return "§9Economy §8| §7";
	}
	
	public String getHighlight()
	{
		return "§6";
	}

}
