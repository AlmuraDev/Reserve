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

import com.almuradev.reserve.econ.type.AccountType;

public class Account {
	private AccountType type;
	private String name;
	private String holder;
	private double balance = 0;
	private boolean dirty = false;

	public Account(AccountType type, String name, String holder) {
		this.type = type;
		this.name = name;
		this.holder = holder;
		setDirty(true);
	}

	/**
	 * Gets the account type of this account.
	 * @return The account type
	 */
	public AccountType getType() {
		return type;
	}

	/**
	 * Sets the account type for this account.
	 * @param type The new account type
	 * @return This account
	 */
	public Account setType(AccountType type) {
		this.type = type;
		setDirty(true);
		return this;
	}

	/**
	 * Gets the name of this account.
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this account.
	 * @param name The new name
	 * @return This account
	 */
	public Account setName(String name) {
		this.name = name;
		setDirty(true);
		return this;
	}

	/**
	 * Gets the holder of this account.
	 * @return The holder
	 */
	public String getHolder() {
		return holder;
	}

	/**
	 * Sets the holder of this account.
	 * @param holder The new holder
	 * @return This account
	 */
	public Account setHolder(String holder) {
		this.holder = holder;
		setDirty(true);
		return this;
	}

	/**
	 * Gets the balance of this account.
	 * @return The balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Sets the balance of this account.
	 * @param balance The new balance
	 * @return This account
	 */
	public Account setBalance(double balance) {
		this.balance = balance;
		setDirty(true);
		return this;
	}

	/**
	 * Adds an amount to this account's balance.
	 * @param amount Amount to add
	 * @return This account
	 */
	public Account add(double amount) {
		return setBalance(balance + amount);
	}

	/**
	 * Wipes the balance on this account.
	 * @return This account
	 */
	public Account wipe() {
		return setBalance(0);
	}

	/**
	 * Returns if the account is dirty or not.
	 * @return True if dirty, false if not
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the account's dirty status. Great caution should be used as unintentionally setting a dirty account to false means
	 * the storage system won't save it. On the flip side, setting an account to always dirty will kill SSD computers. In short,
	 * if un-decided, leave it alone.
	 * @param dirty Flag indicating true or false dirty status
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Account)) {
			return false;
		}

		final Account account = (Account) other;
		return account.getType().equals(type) && account.getName().equalsIgnoreCase(name) && account.getHolder().equalsIgnoreCase(holder) && account.getBalance() == balance;
	}

	@Override
	public String toString() {
		return "Account{type= {" + type.toString() + "}, name= " + name + ", holder= " + holder + ", balance= " + balance + ", dirty= " + dirty + "} ";
	}
}
