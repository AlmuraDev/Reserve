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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.almuradev.reserve.econ.Bank;

public final class Reserve implements Runnable {
	private static final HashMap<String, List<Bank>> BANKS = new HashMap<>();
	private final Storage storage;

	public Reserve(Storage storage) {
		this.storage = storage;
	}

	public void onEnable() {
		storage.load();
	}

	public void onDisable() {
		run();
	}

	/**
	 * Adds a new bank to the reserve. If the specified name exists in this world, return the existing bank.
	 * @param name The name of the bank
	 * @param holder Who holds the bank
	 * @param world The world the bank is in
	 * @return The added bank or the existing one. Will never be null
	 */
	public Bank add(String name, String holder, String world) {
		if (name == null || name.isEmpty() || holder == null || holder.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified name, holder, or world is null!");
		}
		List<Bank> ENTRY = BANKS.get(world);
		if (ENTRY != null) {
			for (Bank bank : ENTRY) {
				if (bank.getName().equalsIgnoreCase(name) && bank.getHolder().equalsIgnoreCase(holder)) {
					return bank;
				}
			}
		} else {
			ENTRY = new ArrayList<>();
			BANKS.put(world, ENTRY);
		}
		final Bank toReturn = new Bank(name, holder);
		ENTRY.add(toReturn);
		return toReturn;
	}

	/**
	 * Gets a bank from the reserve based upon name and world.
	 * @param name The name of the bank
	 * @param world The world the bank is in
	 * @return The bank or null if not found
	 */
	public Bank get(String name, String world) {
		if (name == null || name.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified name or world is null!");
		}
		List<Bank> WORLD_BANKS = BANKS.get(world);
		if (WORLD_BANKS != null) {
			for (Bank bank : WORLD_BANKS) {
				if (bank.getName().equalsIgnoreCase(name)) {
					return bank;
				}
			}
		}
		return null;
	}

	/**
	 * Removes a bank from the reserve.
	 * @param name The name of the bank
	 * @param world The world the bank is in
	 * @return The removed bank or null if not found
	 */
	public Bank remove(String name, String world) {
		if (name == null || name.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified name or world is null!");
		}
		final List<Bank> entry = BANKS.get(world);
		if (entry != null) {
			final Iterator<Bank> entryIterator = entry.iterator();
			while (entryIterator.hasNext()) {
				final Bank temp = entryIterator.next();
				if (temp.getName().equalsIgnoreCase(name)) {
					entryIterator.remove();
					return temp;
				}
			}
		}
		return null;
	}

	/**
	 * Returns an unmodifiable map of the reserve tied to: World, List<Bank>.
	 * @return A map of all banks
	 */
	public Map<String, List<Bank>> retrieveBanks() {
		return Collections.unmodifiableMap(BANKS);
	}

	/**
	 * INTERNAL USE ONLY
	 * @param world
	 * @param injectBank
	 */
	protected void add(String world, Bank injectBank) {
		if (world == null || world.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		List<Bank> ENTRY = BANKS.get(world);
		if (ENTRY != null) {
			for (Bank bank : ENTRY) {
				if (bank.equals(injectBank)) {
					return;
				}
			}
		} else {
			ENTRY = new ArrayList<>();
			BANKS.put(world, ENTRY);
		}
		ENTRY.add(injectBank);
		injectBank.setDirty(false);
	}

	@Override
	public void run() {
		//Step 1, save all to files
		for (String world : BANKS.keySet()) {
			for (Bank bank : BANKS.get(world)) {
				if (!bank.isDirty()) {
					continue;
				}
				storage.save(world, bank);
				//Save to flat file.
				bank.setDirty(false);
			}
		}
		//Step 2, cleanup
		storage.cleanup();
	}
}
