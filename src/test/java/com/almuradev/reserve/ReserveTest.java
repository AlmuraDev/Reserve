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

import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.storage.Reserve;
import com.almuradev.reserve.storage.Storage;
import com.almuradev.reserve.task.SaveTask;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ReserveTest {
	private static Reserve reserve;
	private static Storage storage;
	private final ReservePlugin plugin;
	private final String world = "world";

	public ReserveTest() {
		plugin = PowerMockito.mock(ReservePlugin.class);
		storage = PowerMockito.mock(Storage.class);
		reserve = new Reserve(storage);
	}

	@Test
	public void testReserve() {
		final Bank a = reserve.add("Spouty", world);
		assertEquals(a, reserve.get("Spouty", world));
		a.addAccount(new Account("NinjaZidane", "Checking", 10));
		assertTrue(a.isDirty());
		new SaveTask(plugin, reserve).run();
		assertTrue(!a.isDirty());
	}

	@Test
	public void testBank() {

	}

	@Test
	public void testAccount() {

	}

	@Test
	public void testBackend() {

	}
}
