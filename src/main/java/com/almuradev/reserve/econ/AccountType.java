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

public class AccountType {
	private String name;
	private boolean hasInterest = false;
	private double interestRate = 0.0;
	private boolean dirty = false;

	public AccountType(String name) {
		this.name = name;
		setDirty(true);
	}

	public String getName() {
		return name;
	}

	public AccountType setName(String name) {
		this.name = name;
		setDirty(true);
		return this;
	}

	public boolean recievesInterest() {
		return hasInterest;
	}

	public AccountType shouldReceiveInterest(boolean hasInterest) {
		this.hasInterest = hasInterest;
		setDirty(true);
		return this;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public AccountType setInterestRate(double interestRate) {
		this.interestRate = interestRate;
		setDirty(true);
		return this;
	}

	public boolean isDirty() {
		return dirty;
	}

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
