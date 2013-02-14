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
package com.almuradev.reserve.econ.type;

public class AccountType {
	private String name;
	private boolean hasInterest = false;
	private double interestRate = 0.0;
	private boolean dirty = false;

	public AccountType(String name) {
		this.name = name;
		setDirty(true);
	}

	/**
	 * Gets the name of this account type.
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this account type.
	 * @param name The new name
	 * @return This account type
	 */
	public AccountType setName(String name) {
		this.name = name;
		setDirty(true);
		return this;
	}

	/**
	 * Returns whether this account type applies interest or not.
	 * @return True if it applies interest, false if not
	 */
	public boolean recievesInterest() {
		return hasInterest;
	}

	/**
	 * Sets if this account type applies interest or not.
	 * @param hasInterest Flag where true means to apply interest, false if not
	 * @return This account type
	 */
	public AccountType shouldReceiveInterest(boolean hasInterest) {
		this.hasInterest = hasInterest;
		setDirty(true);
		return this;
	}

	/**
	 * Gets the interest rate for this account type.
	 * @return The interest rate
	 */
	public double getInterestRate() {
		return interestRate;
	}

	/**
	 * Sets the interest rate for this account type.
	 * @param interestRate The new interest rate
	 * @return This account type
	 */
	public AccountType setInterestRate(double interestRate) {
		this.interestRate = interestRate;
		setDirty(true);
		return this;
	}

	/**
	 * Returns if the account type is dirty or not.
	 * @return True if dirty, false if not
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * Sets the account type's dirty status. Great caution should be used as unintentionally setting a dirty account type to false means
	 * the storage system won't save it. On the flip side, setting an account type to always dirty will kill SSD computers. In short,
	 * if un-decided, leave it alone.
	 * @param dirty Flag indicating true or false dirty status
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AccountType)) {
			return false;
		}

		final AccountType type = (AccountType) other;
		return type.getName().equalsIgnoreCase(name) && type.recievesInterest() == hasInterest && type.getInterestRate() == interestRate;
	}

	@Override
	public String toString() {
		return "AccountType{name= " + name + ", hasInterest= " + hasInterest + ", interestRate= " + interestRate + ", dirty= " + dirty + "}";
	}
}
