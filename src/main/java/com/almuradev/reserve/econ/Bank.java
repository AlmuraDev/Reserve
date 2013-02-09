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
	private String holder;
	private String name;
	private List<Account> accounts;
	private boolean dirty = false;

	public Bank(String holder, String name) {
		this.holder = holder;
		this.name = name;
		this.accounts = new ArrayList<>();
		dirty = true;
	}

	/**
	 * @param holder
	 */
	public void setHolder(String holder) {
		this.holder = holder;
		setDirty(true);
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
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		setDirty(true);
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
	public Account getAccount(String name) {
		for (Account account : accounts) {
			if (account.getName().equalsIgnoreCase(name)) {
				return account;
			}
		}
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public Account removeAccount(String name) {
		final Account account = getAccount(name);
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
	public void wipe(boolean erase) {
		for (Account account : retrieveAccounts()) {
			account.wipe();
			if (erase) {
				removeAccount(account.getName());
			}
		}
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

	/**
	 *
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
	 *
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
		if (this.dirty) {
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
		if (dirty == false) {
			for (Account account : accounts) {
				account.setDirty(false);
			}
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Bank)) {
			return false;
		}

		final Bank bank = (Bank) other;
		return bank.getHolder().equals(holder) && bank.getName().equalsIgnoreCase(name) && !bank.retrieveAccounts().equals(accounts);
	}

	@Override
	public String toString() {
		return "Bank{holder= " + holder + ", name= " + name + ", accounts= {" + accounts.toString() + "}, dirty= " + dirty + "} ";
	}
}
