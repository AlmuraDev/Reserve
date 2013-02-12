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
import java.util.List;
import java.util.Map;

import com.almuradev.reserve.econ.Bank;

public final class Reserve {
	private static final HashMap<String, List<Bank>> BANKS = new HashMap<>();
	private final Storage storage;

	public Reserve(Storage storage) {
		this.storage = storage;
	}

	public void onEnable() {
		storage.load();
	}

	public void onDisable() {
		for (String world : BANKS.keySet()) {
			for (Bank bank : BANKS.get(world)) {
				if (!bank.isDirty()) {
					continue;
				}
				storage.save(world, bank);
			}
		}
	}

	/**
	 * Adds a new bank to the reserve under the holder and world's name. If it exists,
	 * this function will return the existing one.
	 * @param holder
	 * @param world
	 * @return
	 */
	public Bank add(String holder, String name, String world) {
		if (holder == null || holder.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		List<Bank> ENTRY = BANKS.get(world);
		if (ENTRY != null) {
			for (Bank bank : ENTRY) {
				if (bank.getHolder().equalsIgnoreCase(holder)) {
					return bank;
				}
			}
		} else {
			ENTRY = new ArrayList<>();
			BANKS.put(world, ENTRY);
		}
		final Bank toReturn = new Bank(holder, name);
		ENTRY.add(toReturn);
		return toReturn;
	}

	public Bank get(String name, String world, boolean ignore) {
		if (name == null || name.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified name or world is null!");
		}
		List<Bank> WORLD_BANKS = BANKS.get(world);
		if (WORLD_BANKS != null && !WORLD_BANKS.isEmpty()) {
			for (Bank bank : WORLD_BANKS) {
				if (bank.getName().equalsIgnoreCase(name)) {
					return bank;
				}
			}
		}
		return null;
	}

	/**
	 * Fetches a bank from the reserve with the
	 * @param holder
	 * @param world
	 * @return
	 */
	public Bank get(String holder, String world) {
		if (holder == null || holder.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified holder or world is null!");
		}
		List<Bank> WORLD_BANKS = BANKS.get(world);
		if (WORLD_BANKS != null && !WORLD_BANKS.isEmpty()) {
			for (Bank bank : WORLD_BANKS) {
				if (bank.getHolder().equalsIgnoreCase(holder)) {
					return bank;
				}
			}
		}
		return null;
	}

	/**
	 * @param holder
	 * @param world
	 * @return
	 */
	public Bank remove(String holder, String world) {
		if (holder == null || holder.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified holder or world is null!");
		}
		final List<Bank> ENTRY = BANKS.get(world);
		if (ENTRY != null) {
			for (Bank bank : ENTRY) {
				if (bank.getHolder().equalsIgnoreCase(holder)) {
					ENTRY.remove(bank);
					return bank;
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves all banks in the reserve.
	 * @return All banks in the reserve.
	 */
	public Map<String, List<Bank>> retrieveBanks() {
		return Collections.unmodifiableMap(BANKS);
	}

	/**
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
}
