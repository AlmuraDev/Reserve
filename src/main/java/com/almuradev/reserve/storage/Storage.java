/*
 * This file is part of Reserve.
 *
 * © 2013 AlmuraDev <http://www.almuradev.com/>
 * Reserve is licensed under the GNU General Public License.
 *
 * Reserve is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Reserve is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License. If not,
 * see <http://www.gnu.org/licenses/> for the GNU General Public License.
 */
package com.almuradev.reserve.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class Storage implements Listener {
	private final ReservePlugin plugin;

	public Storage(ReservePlugin plugin) {
		this.plugin = plugin;
	}

	public void onEnable() {
		final File banksDir = new File(plugin.getDataFolder(), "banks");
		if (!banksDir.exists()) {
			try {
				banksDir.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().severe("Could not create banks directory! Disabling...");
				plugin.getServer().getPluginManager().disablePlugin(plugin);
			}
		}
	}

	public Storage save(String world, Bank bank) {
		if (world == null || world.isEmpty() || bank == null) {
			throw new NullPointerException("Trying to save a null world or bank to the storage backend!");
		}

		return this;
	}

	public Storage remove(String world, Bank bank) {
		if (world == null || world.isEmpty() || bank == null) {
			throw new NullPointerException("Trying to remove a null bank from the storage backend!");
		}
		return this;
	}

	protected void load() {
		try {
			Files.walkFileTree(new File(plugin.getDataFolder(), "banks").toPath(), new BankFileVisitor(plugin));
		} catch (IOException ignore) {
			plugin.getLogger().severe("Encountered a major issue when attempting to traverse the bank's files. Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}
}

class BankFileVisitor extends SimpleFileVisitor<Path> {
	private final ReservePlugin plugin;

	public BankFileVisitor(ReservePlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException ioe) {
		plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		//This means that the file about to be visited is in the banks' directory. Skip those.
		if (path.getParent().equals(new File(plugin.getDataFolder(), "banks").toPath())) {
			return FileVisitResult.CONTINUE;
		}
		//Skip all subdirs or files that are not yaml.
		if (attr.isDirectory() || !path.endsWith(".yml")) {
			return FileVisitResult.CONTINUE;
		}
		//We are now visiting a file inside a directory with root as the parent. Grab that directory's name.
		//ex. pluginname/banks/world >>> world is the 2nd index starting from 0.
		final String world = path.getName(2).toString();
		final File ymlEntry = path.toFile();
		final Bank toInject = createBank(ymlEntry);
		if (toInject == null) {
			plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
			return FileVisitResult.CONTINUE;
		}
		plugin.getReserve().add(world, toInject);
		return FileVisitResult.CONTINUE;
	}

	private Bank createBank(File bankYml) {
		final YamlConfiguration reader = YamlConfiguration.loadConfiguration(bankYml);
		final ConfigurationSection general = reader.getConfigurationSection("general");
		if (general == null) {
			return null;
		}
		//Grab the bank's name
		String rawName = general.getString("name");
		if (rawName == null || rawName.isEmpty()) {
			return null;
		}
		final String name = rawName.replaceAll("^\"|\"$", "");
		//Grab the holder's name
		final String holder = general.getString("holder");
		if (holder == null || holder.isEmpty()) {
			return null;
		}
		//Create the empty bank.
		final Bank bankToInject = new Bank(holder, name);

		final ConfigurationSection accounts = reader.getConfigurationSection("accounts");
		if (accounts == null) {
			return null;
		}

		//Grab the account names.
		final Set<String> accountOwnerNames = accounts.getKeys(false);
		//Determine if there are players with accounts.
		for (String accountOwnerName : accountOwnerNames) {
			final ConfigurationSection accountTypeSection = accounts.getConfigurationSection(accountOwnerName);
			//Determine the names of accounts the player has registered.
			final Set<String> accountTypeNames = accountTypeSection.getKeys(false);
			for (String accountTypeName : accountTypeNames) {
				final ConfigurationSection accountDetailSection = accountTypeSection.getConfigurationSection(accountTypeName);
				//Grab the account name's balance.
				final double balance = accountDetailSection.getDouble("balance", 0.0);
				//Create the account.
				final Account accountToInject = new Account(accountOwnerName, accountTypeName, balance);
				//Finally add it.
				bankToInject.addAccount(accountToInject);
			}
		}
		return bankToInject;
	}
}
