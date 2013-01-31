package com.almuradev.reserve.econ;

import java.io.Serializable;

public class Account implements Serializable {
	private String name;
	private double balance;
	private boolean dirty = false;

	public Account(String name, double balance) {
		this.name = name;
		this.balance = balance;
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
		return "Account{name= " + name + ", balance= " + balance + "} ";
	}
}
