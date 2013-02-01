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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.storage.table.ReserveTable;
import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.h2.H2Configuration;
import com.alta189.simplesave.mysql.MySQLConfiguration;
import com.alta189.simplesave.sqlite.SQLiteConfiguration;

public class Storage {
	private final StorageType type;
	private final File dbLoc;
	private Database db;
	private String dbName, hostName, username, password;
	private int port;

	public Storage(StorageType type, File dbLoc) {
		this(type, dbLoc, "test", "localhost", "spouty", "unleashtheflow", 1337);
	}

	public Storage(StorageType type, File dbLoc, String dbName, String hostName, String username, String password, int port) {
		this.type = type;
		this.dbLoc = dbLoc;
		this.dbName = dbName;
		this.hostName = hostName;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public void onLoad() {
		if (!dbLoc.exists()) {
			dbLoc.mkdirs();
		}
		switch (type) {
			case H2:
				final H2Configuration h2 = new H2Configuration();
				File h2Db = new File(dbLoc, "reserve_db");
				h2.setDatabase(h2Db.getAbsolutePath());
				db = DatabaseFactory.createNewDatabase(h2);
				break;
			case SQLITE:
				final SQLiteConfiguration sqlite = new SQLiteConfiguration(new File(dbLoc, "reserve_db").getAbsolutePath());
				db = DatabaseFactory.createNewDatabase(sqlite);
				break;
			case MYSQL:
				final MySQLConfiguration mysql = new MySQLConfiguration();
				mysql
						.setDatabase(dbName)
						.setHost(hostName)
						.setUser(username)
						.setPassword(password)
						.setPort(port);
				db = DatabaseFactory.createNewDatabase(mysql);
				break;
		}

		try {
			db.registerTable(ReserveTable.class);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
		}

		try {
			db.connect();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public void onUnLoad() {
		try {
			db.close();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public Bank loadBank(String holder, String world) {
		if (holder == null || holder.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Trying to load a holder/world that is null from the storage backend!");
		}
		for (Bank bank : getAll()) {
			if (bank.getHolder().equalsIgnoreCase(holder) && bank.getWorld().equalsIgnoreCase(world)) {
				return bank;
			}
		}
		return null;
	}

	public Storage saveBank(Bank bank) {
		return saveBank(null, bank);
	}

	public Storage saveBank(Bank oldBank, Bank newBank) {
		if (newBank == null) {
			throw new NullPointerException("Trying to save a null bank to the storage backend!");
		}
		List<String> toIgnore = new LinkedList<>();
		if (oldBank != null) {
			//Update prior accounts
			final List<ReserveTable> entries = db.select(ReserveTable.class).where().equal("holder", oldBank.getHolder()).and().equal("world", oldBank.getWorld()).execute().find();
			if (entries != null && !entries.isEmpty()) {
				//Loop through all entries in the table that has the holder and the world's name
				for (ReserveTable entry : entries) {
					//The account existed in the old bank.
					if (oldBank.getAccount(entry.getAccountName()) != null) {
						//The account exists in the new bank, save it to the entry.
						if (newBank.getAccount(entry.getAccountName()) != null) {
							entry.setBalance(newBank.getAccount(entry.getAccountName()).getBalance());
							toIgnore.add(entry.getAccountName());
						//The account exists in the old bank, exists as an entry, but doesn't exist in the new bank. Need to delete this entry.
						} else {
							toIgnore.add(entry.getAccountName());
							db.remove(entry);
						}
						continue;
					}
				}
			}
		} else {
			for (Account account : newBank.retrieveAccounts()) {
				if (toIgnore.contains(account.getName())) {
					continue;
				}
				db.save(new ReserveTable(newBank.getHolder(), newBank.getWorld(), account.getName(), account.getBalance()));
			}
		}
		return this;
	}

	public Storage deleteBank(Bank bank) {
		return this;
	}

	public Collection<Bank> getAll() {
		return null;
	}
}
