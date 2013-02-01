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

	public Storage saveBank(Bank bank) {
		return saveBank(null, bank);
	}

	public Storage saveBank(Bank oldBank, Bank newBank) {
		if (newBank == null) {
			throw new NullPointerException("Trying to save a null bank to the storage backend!");
		}
		if (oldBank != null) {
			final ReserveTable entry = db.select(ReserveTable.class).where().equal("bank", oldBank).execute().findOne();
			if (entry != null) {
				entry.setBank(newBank);
				db.save(entry);
			} else {
				db.save(new ReserveTable(newBank));
			}
		} else {
			db.save(new ReserveTable(newBank));
		}
		return this;
	}

	public Storage deleteBank(Bank bank) {
		if (bank == null) {
			throw new NullPointerException("Trying to remove a null bank from the storage backend!");
		}
		final ReserveTable record = db.select(ReserveTable.class).where().equal("bank", bank).execute().findOne();
		if (record != null) {
			db.remove(record);
		}
		return this;
	}

	public Collection<Bank> getAll() {
		final LinkedList<Bank> banks = new LinkedList<>();
		for (ReserveTable entry : db.select(ReserveTable.class).execute().find()) {
			banks.add(entry.getBank());
		}
		return banks;
	}
}
