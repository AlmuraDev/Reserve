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
package com.almuradev.reserve.storage;

import java.util.Collections;
import java.util.List;

import org.bukkit.World;

public class Bank {
	private final String holder;
	private final List<String> users;
	private final World world;
	private double balance;

	public Bank(World world, String holder, List<String> users) {
		this.world = world;
		this.holder = holder;
		this.users = users;
	}

	public Bank(World world, String holder) {
		this(world, holder, Collections.<String>emptyList());
	}

	/**
	 * Adds the specified amount to this bank's total.
	 * @param amount amount to add.
	 */
	public void add(double amount) {
		balance = balance + amount;
	}

	/**
	 * Sets the balance in this bank to the amount specified.
	 * @param balance The amount to set.
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * Returns the current balance within this Bank.
	 * @return The current amount this bank has.
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Returns the world in-which this bank is in.
	 * @return The current world.
	 */
	public World getWorld() {
		return world;
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
	 * Returns the users that can access this bank.
	 * @return A list of names of users that can access this bank.
	 */
	public List<String> getUsers() {
		return users;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (!(other instanceof Bank)) {
			return false;
		}

		final Bank bank = (Bank) other;
		if (!bank.getHolder().equals(holder) || !bank.getWorld().equals(world)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Bank{holder= " + holder + ", world= " + world + ", balance= " + balance + "}";
	}

	// Needs rest of method.
	public double getTotalBalance() {
		
		return 123456789;
	}
}
