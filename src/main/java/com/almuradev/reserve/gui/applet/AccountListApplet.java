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

import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;

import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.ChatColor;

public class AccountListApplet extends GenericListWidget {
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");

	public AccountListApplet(Bank mySelectedBank, SpoutPlayer player) {
		this.selectedBank = mySelectedBank;
		this.sPlayer = player;
		numForm = NumberFormat.getCurrencyInstance(caLoc);
		List<Account> accountNames = selectedBank.getAccountsFor(sPlayer.getName());
		for (Account account : accountNames) {			
			this.addItem(new ListWidgetItem(account.getName() + " / " + ChatColor.AQUA + account.getType().getName(), ChatColor.GREEN + numForm.format(account.getBalance()), account.getType().getImagePath().trim()));
		}
	}
}