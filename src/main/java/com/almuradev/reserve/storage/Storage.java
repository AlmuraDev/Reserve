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

import com.almuradev.reserve.storage.table.ReserveRecord;
import com.alta189.simplesave.Configuration;
import com.alta189.simplesave.Database;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.h2.H2Configuration;
import com.alta189.simplesave.mysql.MySQLConfiguration;
import com.alta189.simplesave.sqlite.SQLiteConfiguration;

public class Storage {
	private final File dbLoc;
	private Database db;
	private Configuration config;
	private String dbName, hostName, username, password;
	private int port;

	public Storage(Configuration config, File dbLoc) {
		this(config, dbLoc, "test", "localhost", "spouty", "unleashtheflow", 1337);
	}

	public Storage(Configuration config, File dbLoc, String dbName, String hostName, String username, String password, int port) {
		this.config = config;
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
		if (config instanceof SQLiteConfiguration) {
			SQLiteConfiguration sqlite = (SQLiteConfiguration) config;
			File sqliteDb = new File(dbLoc, "reserve_db");
			sqlite.setPath(sqliteDb.getAbsolutePath());
			db = DatabaseFactory.createNewDatabase(sqlite);
		} else if (config instanceof H2Configuration) {
			H2Configuration h2 = (H2Configuration) config;
			File h2Db = new File(dbLoc, "reserve_db");
			h2.setDatabase(h2Db.getAbsolutePath());
			db = DatabaseFactory.createNewDatabase(h2);
		} else {
			MySQLConfiguration mysql = (MySQLConfiguration) config;
			mysql
					.setDatabase(dbName)
					.setHost(hostName)
					.setUser(username)
					.setPassword(password)
					.setPort(port);
			db = DatabaseFactory.createNewDatabase(config);
		}

		try {
			db.registerTable(ReserveRecord.class);
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
}
