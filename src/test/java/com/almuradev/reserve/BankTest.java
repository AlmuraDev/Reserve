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

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import org.bukkit.World;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

public class BankTest {
	private static final Reserve reserve;

	static {
		reserve = new Reserve();
	}

	@Test
	public void testBank() {
		final World world = PowerMockito.mock(World.class);
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
		assertFalse(b.getAccounts().size() == 2);
	}

	@Test
	public void testReserve() {
		final World world = PowerMockito.mock(World.class);
		final Bank a = reserve.addBank("Spouty", world);
		assertEquals(a, reserve.getBank("Spouty", world));
		a.addAccount(new Account("Checking", 10));
		assertTrue(a.isDirty());
		reserve.run();
		assertTrue(!a.isDirty());
	}
}
