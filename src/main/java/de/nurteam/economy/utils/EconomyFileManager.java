package de.nurteam.economy.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.nurteam.economy.Economy;
import de.nurteam.economy.mysql.EconomyMySQL;

public class EconomyFileManager {

	Economy economy;
	EconomyMySQL mySQL;

	public EconomyFileManager(Economy economy) {
		this.economy = economy;
		this.mySQL = this.economy.mySQL;
	}

	public File getMySQLFile() {
		return new File("plugins/Economy", "mysql.yml");
	}

	public FileConfiguration getMySQLFileConfiguration() {
		return YamlConfiguration.loadConfiguration(getMySQLFile());
	}

	public void setStandardMySQL() {
		FileConfiguration cfg = getMySQLFileConfiguration();
		cfg.options().copyDefaults(true);
		cfg.addDefault("username", "root");
		cfg.addDefault("password", "password");
		cfg.addDefault("database", "localhost");
		cfg.addDefault("host", "localhost");
		cfg.addDefault("port", "3306");
		try {
			cfg.save(getMySQLFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readMySQL() {
		FileConfiguration cfg = getMySQLFileConfiguration();
		mySQL.username = cfg.getString("username");
		mySQL.password = cfg.getString("password");
		mySQL.database = cfg.getString("database");
		mySQL.host = cfg.getString("host");
		mySQL.port = cfg.getString("port");

	}
}
