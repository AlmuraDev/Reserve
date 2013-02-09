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

public class Account {
	private String holder;
	private String name;
	private double balance;
	private double interestRate = 0;
	private double taxRate = 0;
	private boolean dirty = false;

	public Account(String holder, String name, double balance) {
		this.holder = holder;
		this.name = name;
		this.balance = balance;
		setDirty(true);
	}

	public Account(String holder, String name) {
		this(holder, name, 0);
	}

	/**
	 * @return
	 */
	public String getHolder() {
		return holder;
	}

	/**
	 * @param holder
	 */
	public void setHolder(String holder) {
		this.holder = holder;
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
	public void setName(String name) {
		this.name = name;
		setDirty(true);
	}

	/**
	 * @return
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 */
	public void setBalance(double balance) {
		this.balance = balance;
		setDirty(true);
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
		setBalance(0);
	}

	/**
	 *
	 * @return
	 */
	public double getInterestRate() {
		return interestRate;
	}

	/**
	 *
	 * @param interestRate
	 */
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 *
	 * @return
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/**
	 *
	 * @param taxRate
	 */
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
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
		if (other == null || !(other instanceof Account)) {
			return false;
		}

		final Account account = (Account) other;
		return account.getHolder().equalsIgnoreCase(holder) && account.getName().equals(name) && account.getInterestRate() == interestRate && account.getTaxRate() == taxRate && account.getBalance() == balance;
	}

	@Override
	public String toString() {
		return "Account{holder= " + holder + ", name= " + name + ", interest= " + interestRate + ", tax= " + taxRate + ", balance= " + balance + ", dirty= " + dirty + "} ";
	}
}
