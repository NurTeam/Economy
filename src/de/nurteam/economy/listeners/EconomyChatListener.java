package de.nurteam.economy.listeners;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import de.nurteam.economy.Economy;
import de.nurteam.economy.utils.PlayerEconomy;
import de.nurteam.economy.utils.EconomyUtils;

public class EconomyChatListener implements Listener
{

	Economy economy;

	public EconomyChatListener(Economy economy)
	{
		this.economy = economy;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();

		if(economy.settingPin.contains(player.getUniqueId()))
		{
			event.setCancelled(true);
			if(event.getMessage().length() > 4 || event.getMessage().length() < 4)
			{
				player.sendMessage(economy.getPrefix() + "§cDie PIN muss " + economy.getHighlight() + "4-stellig §csein.");
				return;
			}

			if(!EconomyUtils.isNumber(event.getMessage()))
			{
				player.sendMessage(economy.getPrefix() + "§cDie PIN muss eine " + economy.getHighlight() + "4-stellige Zahl §csein.");
				return;
			}

			byte[] byteArray = event.getMessage().getBytes();
			String encodedPin = "";
			try
			{
				encodedPin = new String(Base64.getEncoder().encode(byteArray), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				player.sendMessage(economy.getPrefix() + "§cEs ist ein Fehler aufgetreten.");
			}

			if(!Economy.economyManager.hasAccount(player.getUniqueId()))
			{
				Economy.economyManager.createAccount(player.getUniqueId(), encodedPin);
				player.sendMessage(economy.getPrefix() + "Dein " + economy.getHighlight() + "Bankkonto §7wurde §aerfolgreich §7erstellt.");
			}
			else
			{
				player.sendMessage(economy.getPrefix() + "Deine " + economy.getHighlight() + "PIN §7wurde §aerfolgreich §7geändert.");
				Economy.economyManager.uploadToMySQL(player.getUniqueId());
			}

			Economy.economyManager.getPlayerEconomy(player.getUniqueId()).setEncodedPin(encodedPin);

			economy.settingPin.remove(player.getUniqueId());
		}

		else if(economy.checkingForPin.contains(player.getUniqueId()))
		{
			event.setCancelled(true);
			PlayerEconomy playerEconomy = Economy.economyManager.getPlayerEconomy(player.getUniqueId());

			byte[] byteArray = playerEconomy.getEncodedPin().getBytes();
			String decodedPin = "";
			try
			{
				decodedPin = new String(Base64.getDecoder().decode(byteArray), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				player.sendMessage(economy.getPrefix() + "§cEs ist ein Fehler aufgetreten.");
			}

			if(decodedPin.equals(event.getMessage()))
			{
				Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, economy.getHighlight() + "       Bankkontenverwaltung");

				inventory.setItem(1, EconomyUtils.getItemstack(Material.PAPER, 1, 0, "§9PIN ändern", "§7Hier kannst du deine PIN ändern."));
				inventory.setItem(3, EconomyUtils.getItemstack(Material.BARRIER, 1, 0, "§cBankkonto löschen", "§7Hier kannst du dein " + economy.getHighlight() + "Bankkonto §7löschen."));

				player.openInventory(inventory);

				economy.checkingForPin.remove(player.getUniqueId());
				player.sendMessage(economy.getPrefix() + "§7Du hast die " + economy.getHighlight() + "Bankkontenverwaltung §aerfolgreich §7geöffnet.");
				return;
			}

			player.sendMessage(economy.getPrefix() + "§cDie eingegebene " + economy.getHighlight() + "PIN §cist falsch!");
			economy.checkingForPin.remove(player.getUniqueId());

		}
		
		else if(economy.deletingAccount.contains(player.getUniqueId()))
		{
			event.setCancelled(true);
			PlayerEconomy playerEconomy = Economy.economyManager.getPlayerEconomy(player.getUniqueId());

			byte[] byteArray = playerEconomy.getEncodedPin().getBytes();
			String decodedPin = "";
			try
			{
				decodedPin = new String(Base64.getDecoder().decode(byteArray), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				player.sendMessage(economy.getPrefix() + "§cEs ist ein Fehler aufgetreten.");
			}

			if(decodedPin.equals(event.getMessage()))
			{
				player.sendMessage(economy.getPrefix() + "§7Du hast dein " + economy.getHighlight() + "Bankkonto §aerfolgreich §7gelöscht.");
				Economy.economyManager.deleteAccount(player.getUniqueId());
				economy.deletingAccount.remove(player.getUniqueId());
				return;
			}

			player.sendMessage(economy.getPrefix() + "§cDie eingegebene " + economy.getHighlight() + "PIN §cist falsch!");
			economy.deletingAccount.remove(player.getUniqueId());

		}
	}
}
