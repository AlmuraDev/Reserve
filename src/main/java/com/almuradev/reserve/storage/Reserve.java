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

import com.almuradev.reserve.econ.Bank;

import org.getspout.spoutapi.event.spout.ServerTickEvent;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class Reserve implements Listener {
	private static final ArrayList<Bank> BANKS = new ArrayList<>();
	private static final ArrayList<Bank> REMOVED = new ArrayList<>();

	/**
	 * Adds a new bank to the reserve.
	 * @param world The world where the bank is located at.
	 * @param holder The name of the holder of the bank.
	 * @return The bank econ that was added.
	 */
	public Bank addBank(World world, String holder) {
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
			bank = new Bank(world, holder);
			BANKS.add(bank);
		}
		bank.setDirty(true);
		return bank;
	}

	/**
	 * Gets the bank assigned to this world and holder.
	 * @param world The world where the bank is located at.
	 * @param holder The name of the holder of the bank.
	 * @return The Bank of the holder, for manipulation.
	 */
	public Bank getBank(World world, String holder) {
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
	 * @param world The world where the bank is located at.
	 * @param holder The name of the holder of the bank.
	 * @return The bank econ removed.
	 */
	public Bank removeBank(World world, String holder) {
		final Bank bank = getBank(world, holder);
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

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTick(ServerTickEvent event) {
		//Check DT and delay...don't need a SQL ping each tick.
		//Map has at least ONE dirty value.
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
		}
	}
}
