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

import com.almuradev.reserve.econ.Account;
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
				storage.save(world, bank);
			}
		}
	}

	public Bank add(String name, String holder, String world) {
		if (name == null || name.isEmpty() || holder == null || holder.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified name, holder, or world is null!");
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
		final Bank toReturn = new Bank(name, holder);
		ENTRY.add(toReturn);
		return toReturn;
	}

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

	public Bank remove(String name, String world) {
		if (name == null || name.isEmpty() || world == null || world.isEmpty()) {
			throw new NullPointerException("Specified holder or world is null!");
		}
		final List<Bank> ENTRY = BANKS.get(world);
		if (ENTRY != null) {
			for (Bank bank : ENTRY) {
				if (bank.getName().equalsIgnoreCase(name)) {
					ENTRY.remove(bank);
					return bank;
				}
			}
		}
		return null;
	}

	public Map<String, List<Bank>> retrieveBanks() {
		return Collections.unmodifiableMap(BANKS);
	}

	public List<Account> getAccountsInBankFor(String holder, Bank bank) {
		if (holder == null || holder.isEmpty() || bank == null) {
			throw new NullPointerException("Specified holder or bank is null!");
		}
		final ArrayList<Account> accounts = new ArrayList<>();
		final List<Account> injected = bank.retrieveAccounts();
		if (injected != null) {
			for (Account account : injected) {
				if (account.getHolder().equalsIgnoreCase(holder)) {
					accounts.add(account);
				}
			}
		}
		return accounts;
	}

	public Account getAccountFromNameIn(Bank bank, String name, String holder) {
		if (holder == null || holder.isEmpty() || bank == null || name == null || name.isEmpty()) {
			throw new NullPointerException("Specified holder, bank, or name is null!");
		}

		final List<Account> injected = bank.retrieveAccounts();
		if (injected != null) {
			for (Account account : bank.retrieveAccounts()) {
				if (account.getName().equalsIgnoreCase(name) && account.getHolder().equalsIgnoreCase(holder)) {
					return account;
				}
			}
		}
		return null;
	}

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
