package de.nurteam.economy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.nurteam.economy.Economy;

public class EconomyPlayerQuitListener implements Listener {

	Economy economy;

	public EconomyPlayerQuitListener(Economy economy) {
		this.economy = economy;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (Economy.economyManager.hasAccount(player.getUniqueId())) {
			Economy.economyManager.uploadToMySQL(player.getUniqueId());
		}
	}
}
