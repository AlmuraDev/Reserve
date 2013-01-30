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
	public Bank addAccount(World world, String holder, double amount) {
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
			bank = new Bank(world, holder);
			BANKS.add(bank);
		}
		bank.add(amount);
		return bank;
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
}
