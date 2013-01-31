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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.World;

//TODO Testing SQL code...hopefully World is defaulty Serializable...
public class Bank implements Serializable {
	private String holder;
	private World world;
	private List<Account> accounts;
	private boolean dirty = false;

	public Bank(String holder, World world, Account account) {
		this.holder = holder;
		this.world = world;
		accounts = new ArrayList<>();
		if (account != null) {
			addAccount(account);
		}
	}

	public Bank(String holder, World world) {
		this(holder, world, null);
	}

	public void setHolder(String holder) {
		this.holder = holder;
		setDirty(true);
	}

	public void setWorld(World world) {
		this.world = world;
		setDirty(true);
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
	 * @return
	 */
	public List<Account> getAccounts() {
		return Collections.unmodifiableList(accounts);
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
		if (!bank.getHolder().equals(holder) || !bank.getWorld().equals(world) || !bank.getAccounts().equals(accounts)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Bank{holder= " + holder + ", world= " + world + ", accounts= {" + accounts.toString() + "}} ";
	}
}
