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
import java.util.LinkedList;
import java.util.List;

import com.almuradev.reserve.econ.Bank;

import org.bukkit.World;

public class Reserve implements Runnable {
	private static final LinkedList<Bank> BANKS = new LinkedList<>();
	private static final LinkedList<Bank> REMOVED = new LinkedList<>();

	/**
	 * Adds a new bank to the reserve.
	 * @param holder The name of the holder of the bank.
	 * @param world The world where the bank is located at.
	 * @return The bank econ that was added.
	 */
	public Bank addBank(String holder, World world) {
		if (world == null || holder == null || holder.isEmpty()) {
			throw new NullPointerException("Specified world or holder is null!");
		}
		Bank bank = null;
		for (Bank temp : BANKS) {
			if (temp.getWorld().equals(world) && temp.getHolder().equals(holder)) {
				return temp;
			}
		}
		if (bank == null) {
			bank = new Bank(holder, world);
			BANKS.add(bank);
		}
		bank.setDirty(true);
		return bank;
	}

	/**
	 * Gets the bank assigned to this world and holder.
	 * @param holder The name of the holder of the bank.
	 * @param world The world where the bank is located at.
	 * @return The Bank of the holder, for manipulation.
	 */
	public Bank getBank(String holder, World world) {
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
	 * Removes an econ for the holder specified in the World specified.
	 * @param holder The name of the holder of the bank.
	 * @param world The world where the bank is located at.
	 * @return The bank econ removed.
	 */
	public Bank removeBank(String holder, World world) {
		final Bank bank = getBank(holder, world);
		BANKS.remove(bank);
		REMOVED.add(bank);
		return bank;
	}

	/**
	 * Retrieves all banks in the reserve.
	 * @return All banks in the reserve.
	 */
	public List<Bank> retrieveBanks() {
		return Collections.unmodifiableList(BANKS);
	}

	@Override
	public void run() {
		final List<Bank> banks = retrieveBanks();
		for (Bank bank : banks) {
			if (!bank.isDirty()) {
				continue;
			}
			//Bank is dirty and was removed last tick
			if (REMOVED.contains(bank)) {
				REMOVED.remove(bank);
				//Remove from SQL
			} else {
				//Update SQL
			}

			bank.setDirty(false);
		}
	}
}
