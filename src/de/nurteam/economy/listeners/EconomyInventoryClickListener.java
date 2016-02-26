package de.nurteam.economy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import de.nurteam.economy.Economy;

public class EconomyInventoryClickListener implements Listener
{

	Economy economy;

	public EconomyInventoryClickListener(Economy economy)
	{
		this.economy = economy;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();

		if(event.getClickedInventory().getType() == InventoryType.CREATIVE)return;
		
		if(event.getClickedInventory().getTitle().contains("Bankkontenverwaltung"))
		{
			event.setCancelled(true);

			player.playSound(player.getEyeLocation(), Sound.ITEM_BREAK, 1, 1);

			if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta())
			{
				return;
			}

			ItemStack itemStack = event.getCurrentItem();

			if(itemStack.getItemMeta().getDisplayName().contains("PIN ändern"))
			{
				player.sendMessage(economy.getPrefix() + "Gebe nun eine neue " + economy.getHighlight() + "4-stellige PIN§7 ein.");
				economy.settingPin.add(player.getUniqueId());

				player.closeInventory();
				Bukkit.getScheduler().scheduleSyncDelayedTask(economy, new Runnable() {

					@Override
					public void run()
					{
						if(economy.settingPin.contains(player.getUniqueId()))
						{
							economy.settingPin.remove(player.getUniqueId());
							player.sendMessage(economy.getPrefix() + "§cDu hast zu lange gebraucht.");
						}
					}
				}, 10 * 20L);
			}
			
			if(itemStack.getItemMeta().getDisplayName().contains("Bankkonto löschen"))
			{
				player.sendMessage(economy.getPrefix() + "§7Bist du sicher, dass du " + economy.getHighlight() + "dein Bankkonto §7löschen möchtest?");
				player.sendMessage(economy.getPrefix() + "§7Es wird für immer weg sein! (Eine lange Zeit!)");
				player.sendMessage(economy.getPrefix() + "Falls du es dir anders überlegt hast, warte einfach " + economy.getHighlight() + "10 Sekunden§7.");
				player.sendMessage(economy.getPrefix() + "Gebe nun eine neue " + economy.getHighlight() + "4-stellige PIN§7 ein.");
				economy.deletingAccount.add(player.getUniqueId());

				player.closeInventory();
				Bukkit.getScheduler().scheduleSyncDelayedTask(economy, new Runnable() {

					@Override
					public void run()
					{
						if(economy.deletingAccount.contains(player.getUniqueId()))
						{
							economy.deletingAccount.remove(player.getUniqueId());
							player.sendMessage(economy.getPrefix() + "§cDu hast zu lange gebraucht.");
						}
					}
				}, 10 * 20L);
			}
		}
	}
}
