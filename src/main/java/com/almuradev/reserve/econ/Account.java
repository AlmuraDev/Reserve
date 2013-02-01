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

import java.io.Serializable;

public class Account implements Serializable {
	private String name;
	private double balance;
	private boolean dirty = false;

	public Account(String name, double balance) {
		this.name = name;
		this.balance = balance;
		setDirty(true);
	}

	public Account(String name) {
		this(name, 0);
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		setDirty(true);
	}

	/**
	 * @param balance
	 */
	public void setBalance(double balance) {
		this.balance = balance;
		setDirty(true);
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param amount
	 */
	public void add(double amount) {
		setBalance(balance + amount);
	}

	/**
	 * Wipes the balance on this account.
	 */
	public void wipe() {
		balance = 0;
	}

	/**
	 * @return
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (!(other instanceof Account)) {
			return false;
		}

		final Account account = (Account) other;
		if (!account.getName().equals(name) || account.getBalance() != balance) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Account{name= " + name + ", balance= " + balance + ", dirty= " + dirty + "} ";
	}
}
