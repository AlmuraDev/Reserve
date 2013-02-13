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

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtil {
	private static final Economy economy;

	static {
		final RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		economy = rsp == null ? null : rsp.getProvider() == null ? null : rsp.getProvider();
	}

	public static double getBalance(String name) {
		if (!hasEconomy()) {
			throw new IllegalStateException("Tried to perform economy actions but no economy service installed!");
		}
		return economy.getBalance(name);
	}

	public static void add(String name, double amount) {
		if (!hasEconomy()) {
			throw new IllegalStateException("Tried to perform economy actions but no economy service installed!");
		}
		if (amount > 0) {
			economy.depositPlayer(name, amount);
		} else if (amount < 0) {
			economy.withdrawPlayer(name, amount);
		}
	}

	public static boolean hasBalance(String name, double balance) {
		if (!hasEconomy()) {
			throw new IllegalStateException("Tried to perform economy actions but no economy service installed!");
		}
		return economy.has(name, balance);
	}

	public static boolean hasEconomy() {
		return economy != null;
	}
}
