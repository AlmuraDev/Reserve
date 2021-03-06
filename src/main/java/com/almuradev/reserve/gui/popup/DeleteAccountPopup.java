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
package com.almuradev.reserve.gui.popup;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.gui.button.CommandButton;
import com.almuradev.reserve.gui.combobox.AccountDeleteCombo;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ComboBox;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.ChatColor;

public class DeleteAccountPopup extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final GenericLabel an;
	private final GenericButton deleteButton;
	private final ComboBox box;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	private final Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public DeleteAccountPopup(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(255).setHeight(150);
		border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-80);

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText(selectedBank.getName());
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.shiftXPos(-100).shiftYPos(-55).setMaxWidth(200);
		gg.setWidth(200).setHeight(1);

		GenericLabel windowLabel = new GenericLabel("- Close Account -");
		windowLabel.setScale(1.0F);
		windowLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		windowLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(windowLabel.getText()));
		windowLabel.shiftXPos(((GenericLabel.getStringWidth(windowLabel.getText()) / 2) * -1)).shiftYPos(-52);

		GenericLabel cl = new GenericLabel("Account: ");
		cl.setScale(1.0F);
		cl.setAnchor(WidgetAnchor.CENTER_CENTER);
		cl.setHeight(15).setWidth(GenericLabel.getStringWidth(cl.getText()));
		cl.shiftXPos(-110).shiftYPos(-32);

		box = new AccountDeleteCombo(this);
		box.setText("");
		box.setAnchor(WidgetAnchor.CENTER_CENTER);
		box.setWidth(GenericLabel.getStringWidth("12345678901234567890123459"));
		box.setHeight(18);
		box.shiftXPos(-58).shiftYPos(-37);
		box.setAuto(true);
		box.setPriority(RenderPriority.Low);

		an = new GenericLabel();
		numForm = NumberFormat.getCurrencyInstance(caLoc);
		an.setText("Current Balance: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-110).shiftYPos(-10);

		deleteButton = new CommandButton(this, 1, "Close Account");
		GenericButton close = new CommandButton(this, 2, "Close");

		deleteButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		deleteButton.setHeight(16).setWidth(80).shiftXPos(-10).shiftYPos(47);
		close.setHeight(16).setWidth(40).shiftXPos(75).shiftYPos(47);

		populateList();
		attachWidgets(plugin, border, gl, gg, box, cl, windowLabel, an, deleteButton, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);

		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				setDirty(true);
			}
		}, 5L);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				if (box.getSelectedItem() == null) {
					new AckPopup(plugin, sPlayer, selectedBank, "Please specify account.", "deleteaccountgui");
				} else {
					Account myAccount = selectedBank.getAccount(box.getSelectedItem(), sPlayer.getName());
					if (myAccount.getBalance() != 0) {
						sPlayer.getMainScreen().closePopup();
						new AckPopup(plugin, sPlayer, selectedBank, "Account balance is not zero.", "deleteaccountgui");
					} else {
						selectedBank.removeAccount(box.getSelectedItem(), sPlayer.getName());
						sPlayer.getMainScreen().closePopup();
						new AckPopup(plugin, sPlayer, selectedBank, "Account Removed.", "deleteaccountgui");
					}
				}
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new BankPopup(plugin, sPlayer, selectedBank);
				break;
		}
	}

	private void populateList() {
		List<String> items = new ArrayList<String>();
		List<Account> accountNames = selectedBank.getAccountsFor(sPlayer.getName());
		for (Account account : accountNames) {
			items.add(account.getName());
		}
		if (items != null) {
			Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
			box.setItems(items);
			box.setDirty(true);
			box.setSelection(0);
			if (box.getSelectedItem().equalsIgnoreCase("Bank Vault")) {
				deleteButton.setEnabled(false);
				deleteButton.setTooltip("Cannot Remove Bank Vault.");
			} else {
				deleteButton.setEnabled(true);
				deleteButton.setTooltip("");
			}
		}
	}

	public void onSelect() {
		Account myAccount = selectedBank.getAccount(box.getSelectedItem(), sPlayer.getName());
		double balance = 0;
		if (box.getSelectedItem().equalsIgnoreCase("Bank Vault")) {
			deleteButton.setEnabled(false);
			deleteButton.setTooltip("Cannot Remove Bank Vault.");
		} else if (myAccount.getBalance() > 0) {
			an.setText("Current Balance: " + ChatColor.RED + numForm.format(myAccount.getBalance()));
			deleteButton.setEnabled(false);
			deleteButton.setTooltip("Cannot Remove accounts that have a positive balance.");
		} else {
			an.setText("Current Balance: " + ChatColor.GREEN + numForm.format(myAccount.getBalance()));
			deleteButton.setEnabled(true);
			deleteButton.setTooltip("");
		}
	}
}
