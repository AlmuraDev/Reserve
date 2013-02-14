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
	 * Returns the name of this bank.
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this bank.
	 * @param name The new name
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
	 * Returns who holds this bank.
	 * @return The holder
	 */
	public String getHolder() {
		return holder;
	}

	/**
	 * Sets who holds this bank.
	 * @param holder The new holder
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
	 * Adds a new account to this bank. If this account exists in the bank, it simply returns it.
	 * @param account The new account
	 * @return The added or existing account. Guaranteed to never be null.
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

	/**
	 * Adds a new account type to this bank. If the account type exists in the bank, it simply returns it.
	 * @param type The new account type
	 * @return The added or existing account type. Guaranteed to never be null.
	 */
	public AccountType addType(AccountType type) {
		if (type == null) {
			throw new NullPointerException("Specified type is null!");
		}

		if (!types.contains(type)) {
			types.add(type);
			setDirty(true);
		}
		return type;
	}

	/**
	 * Gets an account specified by the name and holder.
	 * @param name The name of the account
	 * @param holder Who holds the account
	 * @return The account or null if not found
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

	/**
	 * Gets an account type specified by the name.
	 * @param name The name of the account type
	 * @return The account type or null if not found
	 */
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
	 * Removes an account from this bank.
	 * @param name The name of the account
	 * @param holder Who holds the account
	 * @return The account removed or null if not found
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

	/**
	 * Removes an account type from this bank.
	 * @param name The name of the account type
	 * @return The account type removed or null if not found
	 */
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
	 * Retrieves all accounts in this bank.
	 * @return List of accounts
	 */
	public List<Account> retrieveAccounts() {
		return Collections.unmodifiableList(accounts);
	}

	/**
	 * Retrieves all account types in this bank.
	 * @return List of account types
	 */
	public List<AccountType> retrieveTypes() {
		return Collections.unmodifiableList(types);
	}

	/**
	 * Returns if this bank has accounts or not.
	 * @return True if amount of accounts is greater than 0, false if not
	 */
	public boolean hasAccounts() {
		return accounts.size() > 0;
	}

	/**
	 * Returns if this bank has account types or not.
	 * @return True if amount of account types is greater than 0, false if not
	 */
	public boolean hasTypes() {
		return types.size() > 0;
	}

	/**
	 * Wipes this bank, setting all account balances to 0.0.
	 * @return This bank, wiped.
	 */
	public Bank wipe() {
		return wipe(false);
	}

	/**
	 * Wipes this bank, setting all account balances to 0.0. The optional parameter allows the option to delete
	 * accounts as well.
	 * @param erase If true, deletes accounts. False simply sets balances.
	 * @return
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
	 * Gets the total balance across all accounts.
	 * @return The total of all account balances
	 */
	public double getTotalBalance() {
		double total = 0;
		for (Account account : accounts) {
			total += account.getBalance();
		}
		return total;
	}

	/**
	 * Gets the total balance of all accounts the holder holds.
	 * @param holder Who holds the accounts
	 * @return The total of all held account balances
	 */
	public double getTotalBalanceFor(String holder) {
		double total = 0;

		for (Account account : accounts) {
			if (account.getHolder().equalsIgnoreCase(holder)) {
				total += account.getBalance();
			}
		}

		return total;
	}

	/**
	 * Gets the amount of accounts in this bank.
	 * @return The amount of accounts
	 */
	public int getAmountOfAccounts() {
		return accounts.size();
	}

	/**
	 * Gets the amount of accounts in this bank this holder holds.
	 * @param holder Who holds the accounts
	 * @return The amount of accounts the holder holds.
	 */
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
	 * Gets all accounts for the specified holder.
	 * @param holder Who holds the accounts
	 * @return A list of all accounts
	 */
	public List<Account> getAccountsFor(String holder) {
		if (holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified holder is null!");
		}
		final ArrayList<Account> accounts = new ArrayList<>();
		final List<Account> injected = retrieveAccounts();
		for (Account account : injected) {
			if (account.getHolder().equalsIgnoreCase(holder)) {
				accounts.add(account);
			}
		}
		return accounts;
	}

	/**
	 * Sets an interest rate on all account types.
	 * @param interestRate The interest rate
	 * @return This bank
	 */
	public Bank setGlobalInterestRate(double interestRate) {
		for (AccountType type : types)  {
			type.shouldReceiveInterest(true);
			type.setInterestRate(interestRate);
		}
		return this;
	}

	/**
	 * Determines if the bank is dirty. This means the bank has been modified since the last time it
	 * was saved to file.
	 * @return True if dirty, false if not
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
	 * Sets the bank as dirty. Great caution should be used as unintentionally setting a dirty bank to false means
	 * the storage system won't save it. On the flip side, setting a bank to always dirty will kill SSD computers. In short,
	 * if un-decided, leave it alone.
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
