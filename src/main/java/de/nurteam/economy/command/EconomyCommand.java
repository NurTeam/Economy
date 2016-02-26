package de.nurteam.economy.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.nurteam.economy.Economy;
import de.nurteam.economy.utils.EconomyUtils;
import de.nurteam.economy.utils.PlayerEconomy;

public class EconomyCommand implements CommandExecutor
{
	Economy economy;

	public EconomyCommand(Economy economy)
	{
		this.economy = economy;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{

		if(!(sender instanceof Player))
		{
			sender.sendMessage("§cDu musst ein Spieler sein.");
			return true;
		}

		final Player player = (Player) sender;
		if(args.length == 0)
		{
			player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");

			player.sendMessage(" §7Bankkonto erstellen: " + economy.getHighlight() + "/economy create");
			player.sendMessage(" §7Bankkonto verwalten: " + economy.getHighlight() + "/economy manage");
			player.sendMessage(" §7Geld überweisen: " + economy.getHighlight() + "/economy pay <Spieler> <Geldmenge>");
			player.sendMessage(" §7Kontostatistiken einsehen: " + economy.getHighlight() + "/economy check [Spieler]");

			player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");
		}

		else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("create"))
			{
				if(Economy.economyManager.hasAccount(player.getUniqueId()))
				{
					player.sendMessage(economy.getPrefix() + "§cDu kannst nur " + economy.getHighlight() + "ein Bankonto §cbesitzen.");
					return true;
				}

				player.sendMessage(economy.getPrefix() + "Gebe nun eine " + economy.getHighlight() + "4-stellige PIN§7 ein.");

				economy.settingPin.add(player.getUniqueId());

				Bukkit.getScheduler().scheduleSyncDelayedTask(economy, new Runnable() {

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

			else if(args[0].equalsIgnoreCase("manage"))
			{
				if(!Economy.economyManager.hasAccount(player.getUniqueId()))
				{
					player.sendMessage(economy.getPrefix() + "§cDu musst dir zuerst " + economy.getHighlight() + "ein Bankkonto §cerstellen.");
					return true;
				}

				if(economy.checkingForPin.contains(player.getUniqueId()))
				{
					player.sendMessage(economy.getPrefix() + "§7Du hast die " + economy.getHighlight() + "Bankkontenverwaltung §cabgebrochen§7.");
					economy.checkingForPin.remove(player.getUniqueId());
					return true;
				}

				if(economy.settingPin.contains(player.getUniqueId()))
				{
					return true;
				}

				player.sendMessage(economy.getPrefix() + "Gebe nun deine " + economy.getHighlight() + "4-stellige PIN§7 ein.");
				economy.checkingForPin.add(player.getUniqueId());
			}

			else if(args[0].equalsIgnoreCase("check"))
			{
				if(!Economy.economyManager.hasAccount(player.getUniqueId()))
				{
					player.sendMessage(economy.getPrefix() + "§cDu hast " + economy.getHighlight() + "kein Bankkonto§c.");
					return true;
				}

				PlayerEconomy playerEconomy = Economy.economyManager.getPlayerEconomy(player.getUniqueId());

				Date date = playerEconomy.getCreateDate();
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
				String output = format.format(date);
				SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
				String outputTime = formatTime.format(date);

				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Kontostatistiken §7§m------------");

				player.sendMessage(" §7Inhaber: " + economy.getHighlight() + player.getDisplayName() + " (Du)");
				player.sendMessage(" §7Erstellt am: " + economy.getHighlight() + output + " §7um " + economy.getHighlight() + outputTime + " Uhr");
				player.sendMessage(" §7Kontostand: " + economy.getHighlight() + economy.calculator.toMoney(playerEconomy.getEuros(), playerEconomy.getCents()));

				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Kontostatistiken §7§m------------");
			}

			else
			{
				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");

				player.sendMessage(" §7Bankkonto erstellen: " + economy.getHighlight() + "/economy create");
				player.sendMessage(" §7Bankkonto verwalten: " + economy.getHighlight() + "/economy manage");
				player.sendMessage(" §7Geld §berweisen: " + economy.getHighlight() + "/economy pay <Spieler> <Geldmenge>");
				player.sendMessage(" §7Kontostatistiken einsehen: " + economy.getHighlight() + "/economy check [Spieler]");

				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");
			}
		}

		else if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("check"))
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

				if(!offlinePlayer.isOnline())
				{
					Economy.economyManager.loadFromMySQL(offlinePlayer.getUniqueId());
				}

				if(!Economy.economyManager.hasAccount(offlinePlayer.getUniqueId()))
				{
					player.sendMessage(economy.getPrefix() + "§cDer Spieler " + economy.getHighlight() + args[1].toString() + " §cbesitzt " + economy.getHighlight() + "kein Bankkonto§c.");
					return true;
				}

				PlayerEconomy playerEconomy = Economy.economyManager.getPlayerEconomy(offlinePlayer.getUniqueId());

				Date date = playerEconomy.getCreateDate();
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
				String output = format.format(date);
				SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
				String outputTime = formatTime.format(date);

				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Kontostatistiken §7§m------------");

				player.sendMessage(" §7Inhaber: " + economy.getHighlight() + offlinePlayer.getName());
				player.sendMessage(" §7Erstellt am: " + economy.getHighlight() + output + " §7um " + economy.getHighlight() + outputTime + " Uhr");
				player.sendMessage(" §7Kontostand: " + economy.getHighlight() + economy.calculator.toMoney(playerEconomy.getEuros(), playerEconomy.getCents()));

				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Kontostatistiken §7§m------------");

				return true;
			}
			else
			{
				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");

				player.sendMessage(" §7Bankkonto erstellen: " + economy.getHighlight() + "/economy create");
				player.sendMessage(" §7Bankkonto verwalten: " + economy.getHighlight() + "/economy manage");
				player.sendMessage(" §7Geld überweisen: " + economy.getHighlight() + "/economy pay <Spieler> <Geldmenge>");
				player.sendMessage(" §7Kontostatistiken einsehen: " + economy.getHighlight() + "/economy check [Spieler]");

				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");
			}
		}
		
		else if(args.length == 3)
		{
			if(args[0].equalsIgnoreCase("pay"))
			{
				if(!args[2].contains(","))
				{
					player.sendMessage(economy.getPrefix() + "§cBitte nenne einen " + economy.getHighlight() + "Geldbetrag§c.");
					player.sendMessage(economy.getPrefix() + "§7Beispiel: " + economy.getHighlight() + "10,00");
					return true;
				}
				
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				String[] input = args[2].split(",");
				
				if(!EconomyUtils.isNumber(input[0]))
				{
					player.sendMessage(economy.getPrefix() + "§cBitte nenne einen " + economy.getHighlight() + "Geldbetrag§c.");
					player.sendMessage(economy.getPrefix() + "§7Beispiel: " + economy.getHighlight() + "10,00");
					return true;
				}
				
				if(!EconomyUtils.isNumber(input[1]))
				{
					player.sendMessage(economy.getPrefix() + "§cBitte nenne einen " + economy.getHighlight() + "Geldbetrag mit einem , §7getrennt.");
					player.sendMessage(economy.getPrefix() + "§7Beispiel: " + economy.getHighlight() + "10,00");
					return true;
				}
				
				Long[] inputs = new Long[input.length];
				
				inputs[0] = Long.parseLong(input[0]);
				inputs[1] = Long.parseLong(input[1]);
				
				if(!offlinePlayer.isOnline())
				{
					Economy.economyManager.loadFromMySQL(offlinePlayer.getUniqueId());
				}
				
				if(!Economy.economyManager.hasAccount(offlinePlayer.getUniqueId()))
				{
					player.sendMessage(economy.getPrefix() + "§cDer Spieler " + economy.getHighlight() + args[1].toString() + " §cbesitzt " + economy.getHighlight() + "kein Bankkonto§c.");
					return true;
				}
				
				if(Economy.economyManager.hasEnoughMoney(player.getUniqueId(), inputs[0], inputs[1]))
				{
					Economy.economyManager.removeMoney(player.getUniqueId(), inputs[0], inputs[1]);
					Economy.economyManager.addMoney(offlinePlayer.getUniqueId(), inputs[0], inputs[1]);
					
					if(offlinePlayer.isOnline())
					{
						((Player)offlinePlayer).sendMessage(economy.getPrefix() + "Der Spieler " + economy.getHighlight() + player.getName() + " §7hat dir " + economy.getHighlight() + economy.calculator.toMoney(inputs[0], inputs[1]) + " §7§berwiesen.");
					}
					
					player.sendMessage(economy.getPrefix() + "Du hast dem Spieler " + economy.getHighlight() + offlinePlayer.getName() + " " + economy.calculator.toMoney(inputs[0], inputs[1]) + " §7überwiesen.");
					player.sendMessage(economy.getPrefix() + "Du hast einen " + economy.getHighlight() + "Kontoauszug §7erhalten. Verliere ihn nicht!");
					
					Date date = Calendar.getInstance().getTime();
					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
					String output = format.format(date);
					SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
					String outputTime = formatTime.format(date);
					
					ItemStack book = EconomyUtils.getBook(1, economy.getHighlight() + "Kontoauszug vom " + output, "§7Der Spieler " + economy.getHighlight() + player.getName() + " §7hat dem Spieler " + economy.getHighlight() + offlinePlayer.getName()
					+ " §7am " + economy.getHighlight() + output + " §7um " + economy.getHighlight() + outputTime + " Uhr " + economy.calculator.toMoney(inputs[0], inputs[1]) + " §7überwiesen.");
					
					player.getInventory().addItem(book);
					return true;
				}
				
				player.sendMessage(economy.getPrefix() + "§cDu hast zu wenig Geld.");
			}
			else
			{
				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");

				player.sendMessage(" §7Bankkonto erstellen: " + economy.getHighlight() + "/economy create");
				player.sendMessage(" §7Bankkonto verwalten: " + economy.getHighlight() + "/economy manage");
				player.sendMessage(" §7Geld überweisen: " + economy.getHighlight() + "/economy pay <Spieler> <Geldmenge>");
				player.sendMessage(" §7Kontostatistiken einsehen: " + economy.getHighlight() + "/economy check [Spieler]");

				player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");
			}
		}
		else
		{
			player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");

			player.sendMessage(" §7Bankkonto erstellen: " + economy.getHighlight() + "/economy create");
			player.sendMessage(" §7Bankkonto verwalten: " + economy.getHighlight() + "/economy manage");
			player.sendMessage(" §7Geld überweisen: " + economy.getHighlight() + "/economy pay <Spieler> <Geldmenge>");
			player.sendMessage(" §7Kontostatistiken einsehen: " + economy.getHighlight() + "/economy check [Spieler]");

			player.sendMessage("§7§m" + "------------§f " + economy.getHighlight() + "Economy §7§m------------");
		}

		return true;
	}

}
