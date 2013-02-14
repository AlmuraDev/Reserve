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
package com.almuradev.reserve.econ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A wrapper class that keeps track of accounts
 */
public class Bank {
	private String name;
	private String holder;
	private List<Account> accounts;
	private List<AccountType> types;
	private boolean dirty = false;

	public Bank(String name, String holder) {
		if (name == null || name.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified name or holder is null!");
		}
		this.name = name;
		this.holder = holder;
		this.accounts = new ArrayList<>();
		types = new ArrayList<>();
		setDirty(true);
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public Bank setName(String name) {
		if (name == null || name.isEmpty()) {
			throw new NullPointerException("Specified name is null!");
		}
		this.name = name;
		setDirty(true);
		return this;
	}

	/**
	 * Returns the holder of this bank. This String is unique, no two holders can have the exact same name.
	 * <p/>
	 * This should solve the offline/online player dilemma that has plagued many other plugins.
	 * @return The name of the current holder.
	 */
	public String getHolder() {
		return holder;
	}

	/**
	 * @param holder
	 */
	public Bank setHolder(String holder) {
		if (holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified holder is null!");
		}
		this.holder = holder;
		setDirty(true);
		return this;
	}

	/**
	 * @param account
	 */
	public Account addAccount(Account account) {
		if (account == null) {
			throw new NullPointerException("Specified account is null!");
		}
		if (!accounts.contains(account)) {
			accounts.add(account);
			setDirty(true);
		}
		return account;
	}

	public AccountType addType(AccountType type) {
		if (name == null || name.isEmpty()) {
			throw new NullPointerException("Specified name is null!");
		}

		if (!types.contains(type)) {
			types.add(type);
			setDirty(true);
		}
		return type;
	}

	/**
	 * @param name
	 * @return
	 */
	public Account getAccount(String name, String holder) {
		if (name == null || name.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified name or holder is null!");
		}
		for (Account account : accounts) {
			if (account.getName().equalsIgnoreCase(name) && account.getHolder().equalsIgnoreCase(holder)) {
				return account;
			}
		}
		return null;
	}

	public AccountType getType(String name) {
		if (name == null || name.isEmpty()) {
			throw new NullPointerException("Specified name is null!");
		}

		for (AccountType type : types) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}

		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public Account removeAccount(String name, String holder) {
		if (name == null || name.isEmpty() || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified name or holder is null!");
		}
		final Iterator<Account> entry = accounts.iterator();
		while(entry.hasNext()) {
			final Account temp = entry.next();
			if (temp.getName().equalsIgnoreCase(name) && temp.getHolder().equalsIgnoreCase(holder)) {
				entry.remove();
				return temp;
			}
		}
		return null;
	}

	public AccountType removeType(String name) {
		if (name == null || name.isEmpty()) {
			throw new NullPointerException("Specified name is null!");
		}
		final Iterator<AccountType> entry = types.iterator();
		while(entry.hasNext()) {
			final AccountType temp = entry.next();
			if (temp.getName().equalsIgnoreCase(name)) {
				entry.remove();
				return temp;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public List<Account> retrieveAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	public List<AccountType> retrieveTypes() {
		return Collections.unmodifiableList(types);
	}

	/**
	 * @return
	 */
	public boolean hasAccounts() {
		return accounts.size() > 0;
	}

	public boolean hasTypes() {
		return types.size() > 0;
	}

	/**
	 * @param erase
	 */
	public Bank wipe(boolean erase) {
		for (Account account : retrieveAccounts()) {
			account.wipe();
			if (erase) {
				removeAccount(account.getName(), account.getHolder());
			}
		}
		return this;
	}

	/**
	 * @return
	 */
	public double getTotalBalance() {
		double total = 0;
		for (Account account : accounts) {
			total += account.getBalance();
		}
		return total;
	}

	public double getTotalBalanceFor(String holder) {
		double total = 0;

		for (Account account : accounts) {
			if (account.getHolder().equalsIgnoreCase(holder)) {
				total += account.getBalance();
			}
		}

		return total;
	}

	public int getAmountOfAccounts() {
		return accounts.size();
	}

	public int getAmountOfAccountsFor(String holder) {
		int amount = 0;

		for (Account account : accounts) {
			if (account.getHolder().equalsIgnoreCase(holder)) {
				amount++;
			}
		}

		return amount;
	}

	/**
	 * @param interestRate
	 * @return
	 */
	public Bank setGlobalInterestRate(double interestRate) {
		for (AccountType type : types)  {
			type.shouldReceiveInterest(true);
			type.setInterestRate(interestRate);
		}
		return this;
	}

	/**
	 * @return
	 */
	public boolean isDirty() {
		if (dirty) {
			return true;
		}
		boolean dirty = false;
		for (Account account : accounts) {
			if (account.isDirty()) {
				dirty = true;
				break;
			}
		}
		if (!dirty)  {
			for (AccountType type : types) {
				if (type.isDirty()) {
					dirty = true;
					break;
				}
			}
		}
		return dirty;
	}

	/**
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		for (Account account : accounts) {
			account.setDirty(dirty);
		}
		for (AccountType type : types) {
			type.setDirty(dirty);
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Bank)) {
			return false;
		}

		final Bank bank = (Bank) other;
		return bank.getName().equalsIgnoreCase(name) && bank.getHolder().equalsIgnoreCase(holder) && bank.retrieveAccounts().equals(accounts);
	}

	@Override
	public String toString() {
		return "Bank{name= " + name + ", holder= " + holder + ", accounts= {" + accounts.toString() + "}, dirty= " + dirty + "} ";
	}
}
