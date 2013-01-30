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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.World;

public class Reserve {
	private static final ArrayList<Bank> BANKS = new ArrayList<>();

	/**
	 * Adds a new account to the reserve. If the account exists, it adds the specified balance to
	 * the account instead.
	 * @param world The world where the bank is located at.
	 * @param holder The name of the holder of the bank.
	 * @param amount The amount to add.
	 * @return The bank account that was added.
	 */
	public Bank addAccount(World world, String holder, List<String> users, double amount) {
		if (world == null || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		Bank bank = null;
		for (Bank temp : BANKS) {
			if (temp.getWorld().equals(world) && temp.getHolder().equals(holder)) {
				bank = temp;
				break;
			}
		}
		if (bank == null) {
			bank = new Bank(world, holder, users);
			BANKS.add(bank);
		}
		bank.add(amount);
		return bank;
	}

	public Bank addAccount(World world, String holder, double amount) {
		return addAccount(world, holder, Collections.<String>emptyList(), amount);
	}

	/**
	 * Gets an account for the holder specified in the World specified.
	 * @param world The world where the bank is located at.
	 * @param holder The name of the holder of the bank.
	 * @return The Bank of the holder, for manipulation.
	 */
	public Bank getAccount(World world, String holder) {
		if (world == null || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		for (Bank bank : BANKS) {
			if (bank.getWorld().equals(world) && bank.getHolder().equals(holder)) {
				return bank;
			}
		}
		return null;
	}

	/**
	 * Removes an account for the holder specified in the World specified.
	 * @param world The world where the bank is located at.
	 * @param holder The name of the holder of the bank.
	 * @return The bank account removed.
	 */
	public Bank removeAccount(World world, String holder) {
		final Bank bank = getAccount(world, holder);
		BANKS.remove(bank);
		return bank;
	}

	/**
	 * Transfers account from one holder to another. If the new holder already has an account, this will merge the
	 * balances together.
	 *
	 * If mergeUsers is set to true, this will also merge in the users from the current holder's account into the users
	 * of the new account holder.
	 * @param oldWorld The current world this account is in.
	 * @param currentHolder The current holder's name of the account.
	 * @param newWorld The new world that the new holder has the account in.
	 * @param newholder The new holder of this account's name.
	 * @param mergeBalances True to merge the two balances. If false, the new account's balance will only be applied.
	 * @param mergeUsers If true, will merge in users from the old account in with the new one.
	 * @param transferWorld If true, the old world associated with the account will be changed to the new one.
	 */
	public void transferAccount(World oldWorld, String currentHolder, World newWorld, String newholder, boolean mergeBalances, boolean mergeUsers, boolean transferWorld) {
		if (oldWorld == null || currentHolder == null || newholder == null || currentHolder.isEmpty() || newholder.isEmpty()) {
			throw new NullPointerException("The previous world is null or the previous holder or the current holder's names are either null or empty!");
		}

		final Bank old = getAccount(oldWorld, currentHolder);
		if (old == null) {
			throw new NullPointerException("Previous account holder didn't exist!");
		}
		final Bank existing = getAccount(newWorld, newholder);
		final World transferedWorld = transferWorld == true ? newWorld : oldWorld;
		final List<String> transferedUsers = old.getUsers();
		double balance = old.getBalance();
		if (existing != null) {
			if (mergeUsers) {
				transferedUsers.addAll(old.getUsers());
			}
			if (mergeBalances) {
				balance = balance + old.getBalance();
			}
		}
		forceAdd(transferedWorld, newholder, transferedUsers, balance);
	}

	/**
	 * Retrieves all accounts in the reserve.
	 * @return All accounts in the reserve.
	 */
	public List<Bank> retrieveAccounts() {
		return Collections.unmodifiableList(BANKS);
	}

	private void forceAdd(World world, String holder, List<String> users, double balance) {
		removeAccount(world, holder);
		addAccount(world, holder, users, balance);
	}
}
