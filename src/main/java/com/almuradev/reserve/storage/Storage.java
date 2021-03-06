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
import com.almuradev.reserve.econ.type.AccountType;

import org.apache.commons.lang.StringUtils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Storage {
	private final ReservePlugin plugin;
	private final File dir;

	public Storage(ReservePlugin plugin) {
		this.plugin = plugin;
		dir = new File(plugin.getDataFolder(), "banks");
	}

	public void onEnable() {
		try {
			Files.createDirectory(dir.toPath());
		} catch (FileAlreadyExistsException fafe) {
			;
		} catch (IOException e) {
			plugin.getLogger().severe("Could not create " + dir.getPath() + "! Disabling...");
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
			worldDir = Files.createDirectory(new File(plugin.getDataFolder(), "banks" + File.separator + world).toPath());
		} catch (FileAlreadyExistsException fafe) {
			worldDir = new File(plugin.getDataFolder(), "banks" + File.separator + world).toPath();
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
		reader.set("balance", bank.getBalance());
		final ConfigurationSection accounts = reader.createSection("accounts");
		for (Account account : bank.retrieveAccounts()) {
			ConfigurationSection accountTypeSection = accounts.getConfigurationSection(account.getType().getName().toLowerCase());
			if (accountTypeSection == null) {
				accountTypeSection = accounts.createSection(account.getType().getName().toLowerCase());
			}
			final ConfigurationSection accountHolderSection = accountTypeSection.createSection(account.getHolder());
			accountHolderSection.set("name", account.getName());
			accountHolderSection.set("balance", account.getBalance());
			if (account.isInterestPayable()) {
				accountHolderSection.set("interest-payable", true);
			}
		}
		final ConfigurationSection types = reader.createSection("types");
		for (AccountType type : bank.retrieveTypes()) {
			final ConfigurationSection typeNameSection = types.createSection(type.getName());
			typeNameSection.set("interest", type.receivesInterest());
			typeNameSection.set("interest-rate", type.getInterestRate());
			typeNameSection.set("image-path", type.getImagePath());
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
			Files.walkFileTree(dir.toPath(), new FileSavingVisitor(plugin));
		} catch (IOException ignore) {
			plugin.getLogger().severe("Encountered a major issue while attempting to traverse " + dir.toPath() + ". Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}

	protected void cleanup() {
		try {
			Files.walkFileTree(dir.toPath(), new FileCleaningVisitor(plugin));
		} catch (IOException ignore) {
			plugin.getLogger().severe("Encountered a major issue while attempting to traverse " + dir.toPath() + ". Disabling...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}
}

class FileCleaningVisitor extends SimpleFileVisitor<Path> {
	private final ReservePlugin plugin;

	public FileCleaningVisitor(ReservePlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException ioe) {
		plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		if (path.getFileName().toString().endsWith(".yml") && path.getNameCount() == 5) {
			if (plugin.getReserve().get(path.getName(4).toString().split(".yml")[0], path.getName(3).toString()) == null) {
				try {
					Files.deleteIfExists(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return FileVisitResult.CONTINUE;
	}
}

class FileSavingVisitor extends SimpleFileVisitor<Path> {
	private final ReservePlugin plugin;

	public FileSavingVisitor(ReservePlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException ioe) {
		plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
		if (path.getFileName().toString().endsWith(".yml") && path.getNameCount() == 5) {
			final String world = path.getName(3).toString();
			final String name = path.getName(4).toString().split(".yml")[0];
			final File ymlEntry = path.toFile();
			final Bank toInject = createBank(name, ymlEntry);
			if (toInject == null) {
				plugin.getLogger().severe("Could not load: " + path.getFileName() + ". Skipping...");
				return FileVisitResult.CONTINUE;
			}
			plugin.getReserve().add(world, toInject);
		}
		return FileVisitResult.CONTINUE;
	}

	private Bank createBank(String name, File bankYml) {
		final YamlConfiguration reader = YamlConfiguration.loadConfiguration(bankYml);
		//Grab the holder's name
		final String holder = reader.getString("holder");
		if (holder == null || holder.isEmpty()) {
			return null;
		}
		final double balance = reader.getDouble("balance", 0.0);

		//Create the empty bank.
		final Bank bankToInject = new Bank(name, holder);
		bankToInject.setBalance(balance);

		final ConfigurationSection types = reader.getConfigurationSection("types");
		if (types == null) {
			return null;
		}

		//Grab the type names
		final Set<String> typeNames = types.getKeys(false);
		for (String typeName : typeNames) {
			final ConfigurationSection typeSection = types.getConfigurationSection(typeName);
			final boolean hasInterest = typeSection.getBoolean("interest", false);
			final double interestRate = typeSection.getDouble("interest-rate", 0.0);
			final String imagePath = typeSection.getString("image-path", "");
			final AccountType type = new AccountType(typeName);
			type
					.shouldReceiveInterest(hasInterest)
					.setInterestRate(interestRate)
					.setImagePath(imagePath);
			bankToInject.addType(type);
		}

		final ConfigurationSection accounts = reader.getConfigurationSection("accounts");
		if (accounts == null) {
			return null;
		}

		//Grab the account names.
		final Set<String> accountTypeNames = accounts.getKeys(false);
		//Determine if there are types of accounts.
		for (String accountTypeName : accountTypeNames) {
			final AccountType type = bankToInject.getType(accountTypeName);
			if (type == null) {
				plugin.getLogger().severe("Account type " + accountTypeName + " in accounts section is not found in this bank! Skipping...");
				continue;
			}
			final ConfigurationSection accountTypeSection = accounts.getConfigurationSection(accountTypeName);
			//Determine the names of who holds the accounts.
			final Set<String> accountHolderNames = accountTypeSection.getKeys(false);
			for (String accountHolderName : accountHolderNames) {
				final ConfigurationSection accountHolderSection = accountTypeSection.getConfigurationSection(accountHolderName);
				//Grab the account name (nickname).
				final String accountName = accountHolderSection.getString("name", "My " + StringUtils.capitalize(type.getName().toLowerCase()));
				//Grab the account holder's balance.
				final double accountBalance = accountHolderSection.getDouble("balance", 0.0);
				//Grab the interest holder status.
				final boolean interestPayable = accountHolderSection.getBoolean("interest-payable", false);
				//Create the account.
				final Account accountToInject = new Account(type, accountName, accountHolderName);
				accountToInject
						.setBalance(accountBalance);
				if (interestPayable) {
					bankToInject.setInterestPayable(accountToInject);
				}
				//Finally add it.
				bankToInject.addAccount(accountToInject);
			}
		}
		return bankToInject;
	}
}
