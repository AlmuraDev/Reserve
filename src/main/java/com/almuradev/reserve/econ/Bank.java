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
import java.util.List;

/**
 * A wrapper class that keeps track of accounts
 */
public class Bank {
	private String name;
	private String holder;
	private List<Account> accounts;
	private boolean dirty = false;

	public Bank(String name, String holder) {
		this.name = name;
		this.holder = holder;
		this.accounts = new ArrayList<>();
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
		this.holder = holder;
		setDirty(true);
		return this;
	}

	/**
	 * @param account
	 */
	public Account addAccount(Account account) {
		if (!accounts.contains(account)) {
			accounts.add(account);
			setDirty(true);
		}
		return account;
	}

	/**
	 * @param name
	 * @return
	 */
	public Account getAccount(String name, String holder) {
		for (Account account : accounts) {
			if (account.getName().equalsIgnoreCase(name) && account.getHolder().equalsIgnoreCase(holder)) {
				return account;
			}
		}
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public Account removeAccount(String name, String holder) {
		final Account account = getAccount(name, holder);
		if (account == null) {
			return null;
		}
		accounts.remove(account);
		setDirty(true);
		return account;
	}

	/**
	 * @return
	 */
	public List<Account> retrieveAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	/**
	 * @return
	 */
	public boolean hasAccounts() {
		return accounts.size() > 0;
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
	 * @param taxRate
	 * @return
	 */
	public Bank setGlobalTaxRate(double taxRate) {
		for (Account account : accounts) {
			account.setTaxRate(taxRate);
		}
		return this;
	}

	/**
	 * @param interestRate
	 * @return
	 */
	public Bank setGlobalInterestRate(double interestRate) {
		for (Account account : accounts) {
			account.setInterestRate(interestRate);
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
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Bank)) {
			return false;
		}

		final Bank bank = (Bank) other;
		return bank.getName().equalsIgnoreCase(name) && bank.getHolder().equalsIgnoreCase(holder) && !bank.retrieveAccounts().equals(accounts);
	}

	@Override
	public String toString() {
		return "Bank{holder= " + holder + ", name= " + name + ", accounts= {" + accounts.toString() + "}, dirty= " + dirty + "} ";
	}
}
