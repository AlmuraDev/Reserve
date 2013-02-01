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
package com.almuradev.reserve;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.storage.Reserve;
import com.almuradev.reserve.storage.Storage;
import com.almuradev.reserve.storage.StorageType;
import com.almuradev.reserve.storage.table.ReserveTable;
import com.alta189.simplesave.DatabaseFactory;
import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;
import com.alta189.simplesave.h2.H2Configuration;
import com.alta189.simplesave.h2.H2Database;
import com.alta189.simplesave.sqlite.SQLiteConfiguration;
import com.alta189.simplesave.sqlite.SQLiteDatabase;

import junit.framework.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import org.bukkit.World;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ReserveTest {
	private static final Reserve reserve;
	private final String world = "world";

	static {
		reserve = new Reserve();
	}

	@Test
	public void testReserve() {
		final Bank a = reserve.addBank("Spouty", world);
		assertEquals(a, reserve.getBank("Spouty", world));
		a.addAccount(new Account("Checking", 10));
		assertTrue(a.isDirty());
		reserve.run();
		assertTrue(!a.isDirty());
	}

	@Test
	public void testBank() {
		final Bank a = new Bank("Spouty", world);
		final Bank b = new Bank("Sprouty", world);
		assertNotEquals(a, b);
		b.setHolder("Spouty");
		assertEquals(a, b);
		a.addAccount(new Account("Checking", 10));
		assertNotEquals(a, b);
		b.addAccount(new Account("Checking", 10));
		assertEquals(a, b);
		b.addAccount(new Account("Checking", 10));
		assertFalse(b.retrieveAccounts().size() == 2);
		b.addAccount(new Account("Savings", 100));
		assertTrue(b.retrieveAccounts().size() == 2);
	}

	@Test
	public void testAccount() {
		final Account a = new Account("Checking");
		assertTrue(a.isDirty());
		final Account b = new Account("Checking", 1);
		assertNotEquals(a, b);
		reserve.run();
		assertTrue(a.isDirty());
		assertTrue(b.isDirty());
		b.setBalance(0);
		assertEquals(a, b);
	}

	@Test
	public void testSqlite() {
		final SQLiteConfiguration config = new SQLiteConfiguration();
		File tmpfile = null;
		try {
			tmpfile = File.createTempFile("sqlite_test_", ".db");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException occured: " + e.toString());
		}
		assertNotNull(tmpfile);
		config.setPath(tmpfile.getAbsolutePath().substring(0, tmpfile.getAbsolutePath().indexOf(".db")));
		tmpfile.deleteOnExit();
		final SQLiteDatabase db = (SQLiteDatabase) DatabaseFactory.createNewDatabase(config);
		try {
			db.registerTable(ReserveTable.class);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
			fail("Exception occured too early! " + e.toString());
		}
		try {
			db.connect();
		} catch (ConnectionException e) {
			fail("Failed to connect to database! " + e.toString());
		}
		final ReserveTable one = new ReserveTable();
		db.save(ReserveTable.class, one);
		assertEquals(db.select(ReserveTable.class).execute().find().size(), 1);
		try {
			db.close();
		} catch (ConnectionException e) {
			fail("Failed to close database! " + e.toString());
		}
		tmpfile.delete();
	}

	@Test
	public void testH2() {
		final H2Configuration h2 = new H2Configuration();
		File tmpfile = null;
		try {
			tmpfile = File.createTempFile("h2_test_", ".db");
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException occurred: " + e.toString());
		}
		assertNotNull(tmpfile);
		h2.setDatabase(tmpfile.getAbsolutePath().substring(0, tmpfile.getAbsolutePath().indexOf(".db")));
		tmpfile.deleteOnExit();
		final H2Database db = (H2Database) DatabaseFactory.createNewDatabase(h2);
		try {
			db.registerTable(ReserveTable.class);
		} catch (TableRegistrationException e) {
			e.printStackTrace();
			fail("Exception occurred too early! " + e.toString());
		}
		try {
			db.connect();
		} catch (ConnectionException e) {
			fail("Failed to connect to database! " + e.toString());
		}
		final Bank test = new Bank("Spouty", world);
		final Bank test2 = new Bank("Spouty", world, new Account("Checking", 100));
		db.save(new ReserveTable(test));
		db.save(new ReserveTable(test2));
		assertEquals(db.select(ReserveTable.class).execute().find().size(), 2);
		assertEquals(db.select(ReserveTable.class).where().equal("bank", test).execute().findOne().getBank(), test);
		assertEquals(db.select(ReserveTable.class).where().equal("bank", test2).execute().findOne().getBank(), test2);
		try {
			db.close();
		} catch (ConnectionException e) {
			fail("Failed to close database! " + e.toString());
		}
		tmpfile.delete();
	}

	@Test
	public void testBackend() {
		Path test = null;
		try {
			test = Files.createTempDirectory("test");
		} catch (IOException e) {
			fail("Could not create temporary folder!");
		}
		final Storage storage = new Storage(StorageType.H2, test.toFile());
		storage.onLoad();
		final Bank a = new Bank("Spouty", world, new Account("Checking", 100));
		final Bank b = new Bank("Spouty", world, new Account("Savings", 100));
		storage.saveBank(a);
		storage.saveBank(a, b);
		assertEquals(storage.getAll().size(), 1);
		final Bank bank = storage.loadBank("Spouty", world);
		assertNotNull(bank);
		assertEquals(bank.getAccount("Checking"), new Account("Checking", 100));
		storage.deleteBank(b);
		assertEquals(storage.getAll().size(), 0);
		storage.onUnLoad();
	}
}
