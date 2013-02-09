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
import java.nio.file.FileAlreadyExistsException;
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
		try {
			Files.createDirectory(new File(plugin.getDataFolder(), "banks").toPath());
		} catch (FileAlreadyExistsException fafe) {
			;
		} catch (IOException e) {
			plugin.getLogger().severe("Could not create banks directory! Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	public Storage save(String world, Bank bank) {
		if (world == null || world.isEmpty() || bank == null) {
			throw new NullPointerException("Trying to save a null world or bank to the storage backend!");
		}
		//Find (and create if needed) world directory.
		Path worldDir;
		try {
			worldDir = Files.createDirectory(new File(plugin.getDataFolder(), "banks" + File.pathSeparator + world).toPath());
		} catch (FileAlreadyExistsException fafe) {
			worldDir = new File(plugin.getDataFolder(), "banks" + File.pathSeparator + world).toPath();
		} catch (IOException ioe) {
			plugin.getLogger().severe("Could not save " + bank.toString() + ". Skipping...");
			ioe.printStackTrace();
			return this;
		}
		//Find (and create if needed) the bank file.
		final Path bankPath;
		try {
			bankPath = new File(worldDir.toFile(), bank.getName() + ".yml").toPath();
			Files.deleteIfExists(bankPath);
			Files.createFile(bankPath);
		} catch (IOException ioe) {
			plugin.getLogger().severe("Could not save " + bank.toString() + ". Skipping...");
			ioe.printStackTrace();
			return this;
		}
		//Create a reader.
		final YamlConfiguration reader = new YamlConfiguration();
		//Start writing!
		reader.set("holder", bank.getHolder());
		final ConfigurationSection accounts = reader.createSection("accounts");
		for (Account account : bank.retrieveAccounts()) {
			ConfigurationSection accountTypeSection = accounts.getConfigurationSection(account.getName());
			if (accountTypeSection == null) {
				accountTypeSection = accounts.createSection(account.getName());
			}
			final ConfigurationSection accountHolderSection = accountTypeSection.createSection(account.getHolder());
			accountHolderSection.set("balance", account.getBalance());
			accountHolderSection.set("interest-rate", account.getInterestRate());
			accountHolderSection.set("tax-rate", account.getTaxRate());
		}
		try {
			reader.save(bankPath.toFile());
		} catch (IOException ioe) {
			plugin.getLogger().severe("Could not save bank " + bank.toString());
			ioe.printStackTrace();
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
		final String name = path.getName(3).toString();
		final File ymlEntry = path.toFile();
		final Bank toInject = createBank(name, ymlEntry);
		if (toInject == null) {
			plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
			return FileVisitResult.CONTINUE;
		}
		plugin.getReserve().add(world, toInject);
		return FileVisitResult.CONTINUE;
	}

	private Bank createBank(String name, File bankYml) {
		final YamlConfiguration reader = YamlConfiguration.loadConfiguration(bankYml);
		//Grab the holder's name
		final String holder = reader.getString("holder");
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
		final Set<String> accountTypeNames = accounts.getKeys(false);
		//Determine if there are types of accounts.
		for (String accountTypeName : accountTypeNames) {
			final ConfigurationSection accountTypeSection = accounts.getConfigurationSection(accountTypeName);
			//Determine the names of who holds the accounts.
			final Set<String> accountHolderNames = accountTypeSection.getKeys(false);
			for (String accountHolderName : accountHolderNames) {
				final ConfigurationSection accountHolderSection = accountTypeSection.getConfigurationSection(accountTypeName);
				//Grab the account holder's balance.
				final double balance = accountHolderSection.getDouble("balance", 0.0);
				//Grab the account holder's interest rate.
				final double interestRate = accountHolderSection.getDouble("interest-rate", 0.0);
				//Grab the account holder's tax rate.
				final double taxRate = accountHolderSection.getDouble("tax-rate", 0.0);
				//Create the account.
				final Account accountToInject = new Account(accountTypeName, accountHolderName);
				accountToInject
						.setBalance(balance)
						.setInterestRate(interestRate)
						.setTaxRate(taxRate);
				//Finally add it.
				bankToInject.addAccount(accountToInject);
			}
		}
		return bankToInject;
	}
}
