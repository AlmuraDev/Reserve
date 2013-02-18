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
package com.almuradev.reserve.gui.applet;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Bank;

import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.ListWidgetItem;

import org.bukkit.ChatColor;

public class BankListApplet extends GenericListWidget {
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");

	public BankListApplet() {
		Map<String, List<Bank>> allBanks = ReservePlugin.getReserve().retrieveBanks();
		numForm = NumberFormat.getCurrencyInstance(caLoc);
		for (String world : allBanks.keySet()) {
			for (Bank bank : allBanks.get(world)) {
				this.addItem(new ListWidgetItem(bank.getName() + " / " + ChatColor.AQUA + world, ChatColor.DARK_GREEN + bank.getHolder().trim() + ChatColor.RESET + " - " + ChatColor.GOLD + numForm.format(bank.getAccountsBalance()), "http://face.mmo.me.uk/" + bank.getHolder().trim() + ".png"));
			}
		}
	}
}