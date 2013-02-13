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
	private AccountType type;
	private String name;
	private String holder;
	private double balance = 0;
	private double interestRate = 0;
	private double taxRate = 0;
	private boolean dirty = false;

	public Account(AccountType type, String name, String holder) {
		this.type = type;
		this.name = name;
		this.holder = holder;
		setDirty(true);
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
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
	public Account setName(String name) {
		this.name = name;
		setDirty(true);
		return this;
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
	public Account setHolder(String holder) {
		this.holder = holder;
		setDirty(true);
		return this;
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
	public Account setBalance(double balance) {
		this.balance = balance;
		setDirty(true);
		return this;
	}

	/**
	 * @param amount
	 */
	public Account add(double amount) {
		return setBalance(balance + amount);
	}

	/**
	 * Wipes the balance on this account.
	 */
	public Account wipe() {
		return setBalance(0);
	}

	/**
	 * @return
	 */
	public double getInterestRate() {
		return interestRate;
	}

	/**
	 * @param interestRate
	 */
	public Account setInterestRate(double interestRate) {
		this.interestRate = interestRate;
		setDirty(true);
		return this;
	}

	/**
	 * @return
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/**
	 * @param taxRate
	 */
	public Account setTaxRate(double taxRate) {
		this.taxRate = taxRate;
		setDirty(true);
		return this;
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
		return account.getType().equals(type) && account.getName().equalsIgnoreCase(name) && account.getHolder().equalsIgnoreCase(holder) && account.getInterestRate() == interestRate && account.getTaxRate() == taxRate && account.getBalance() == balance;
	}

	@Override
	public String toString() {
		return "Account{type= " + type.name().toLowerCase() + ", name= " + name + ", holder= " + holder + ", interest= " + interestRate + ", tax= " + taxRate + ", balance= " + balance + ", dirty= " + dirty + "} ";
	}
}
