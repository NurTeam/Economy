package de.nurteam.economy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.nurteam.economy.Economy;

public class EconomyPlayerJoinListener implements Listener
{
	
	Economy economy;
	
	public EconomyPlayerJoinListener(Economy economy)
	{
		this.economy = economy;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		if(Economy.economyManager.hasAccount(player.getUniqueId()))
		{
			player.sendMessage(economy.getPrefix() + "§7Bitte warte kurz bis dein Bankkonto geladen wurde.");
			Economy.economyManager.loadFromMySQL(player.getUniqueId());
			player.sendMessage(economy.getPrefix() + "§7Dein Bankkonto wurde §aerfolgreich §7geladen.");
		}
	}
}
