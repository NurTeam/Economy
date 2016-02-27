package de.nurteam.economy.utils;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class EconomyUtils {

	public static boolean isNumber(String string) {
		try {
			Long.parseLong(string);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public static ItemStack getItemstack(Material material, int amount, int damage, String displayName, String... lore) {
		ItemStack itemStack = new ItemStack(material, amount, (short) damage);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (displayName != null) {
			itemMeta.setDisplayName(displayName);
		}
		if (lore != null) {
			itemMeta.setLore(Arrays.asList(lore));
		}
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	public static ItemStack getBook(int amount, String displayName, String... pages) {
		ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, amount, (short) 0);
		BookMeta itemMeta = (BookMeta) itemStack.getItemMeta();
		if (displayName != null) {
			itemMeta.setDisplayName(displayName);
		}
		itemMeta.setAuthor("�9Economy");
		itemMeta.setPages(Arrays.asList(pages));
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}
}
