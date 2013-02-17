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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almuradev.reserve.gui;

import java.text.NumberFormat;
import java.util.Locale;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.ListWidget;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.ChatColor;

public class BankMainGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final Account selectedAccount;
	private ListWidget list;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public BankMainGUI(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;
		this.selectedAccount = null;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(250).setHeight(250);
		border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-120);

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText(selectedBank.getName());
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-110);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(200).setHeight(1);
		gg.shiftXPos(0 - (gg.getWidth() / 2)).shiftYPos(-95);

		GenericGradient gb = new GenericGradient();
		gb.setBottomColor(bottom).setTopColor(bottom);
		gb.setAnchor(WidgetAnchor.CENTER_CENTER);
		gb.setWidth(200).setHeight(1);
		gb.shiftXPos(0 - (gb.getWidth() / 2)).shiftYPos(-65);

		list = new AccountListApplet(selectedBank, sPlayer);
		list.setAnchor(WidgetAnchor.CENTER_CENTER);
		list.shiftXPos(-80).shiftYPos(-60);
		list.setWidth(155).setHeight(70);
		list.setPriority(RenderPriority.Lowest);

		GenericLabel bankAccountsLabel = new GenericLabel();
		bankAccountsLabel.setScale(1.0F);
		bankAccountsLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		bankAccountsLabel.setText("Accounts: " + ChatColor.YELLOW + selectedBank.getAmountOfAccountsFor(sPlayer.getName()));
		bankAccountsLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(bankAccountsLabel.getText()));
		bankAccountsLabel.shiftXPos((GenericLabel.getStringWidth(bankAccountsLabel.getText()) / 2) * -1).shiftYPos(-89);

		GenericLabel bankNameLabel = new GenericLabel();
		numForm = NumberFormat.getCurrencyInstance(caLoc);
		bankNameLabel.setScale(1.0F);
		bankNameLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		bankNameLabel.setText("Total Balance: " + ChatColor.GOLD + numForm.format(selectedBank.getAccountsBalanceFor(sPlayer.getName())));
		bankNameLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(bankNameLabel.getText()));
		bankNameLabel.shiftXPos((GenericLabel.getStringWidth(bankNameLabel.getText()) / 2) * -1).shiftYPos(-79);

		GenericButton createAccount = new CommandButton(this, 1, "Open New Account");
		GenericButton makeDeposit = new CommandButton(this, 2, "Make Deposit");
		GenericButton makeWithdraw = new CommandButton(this, 3, "Make Withdraw");
		GenericButton closeAccount = new CommandButton(this, 4, "Close Account");
		GenericButton options = new CommandButton(this, 5, "Options");
		GenericButton faq = new CommandButton(this, 5, "?");
		GenericButton close = new CommandButton(this, 6, "Close");

		createAccount.setTooltip("Creates a New Bank Accounts");
		makeDeposit.setTooltip("Takes money you are carrying and deposits into specified account.");
		makeWithdraw.setTooltip("Removed money from your specified account.");
		closeAccount.setTooltip("Closes & Deletes the specified bank account.");
		faq.setTooltip("Frequently Asked Questions Here");
		
		createAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		makeDeposit.setAnchor(WidgetAnchor.CENTER_CENTER);
		makeWithdraw.setAnchor(WidgetAnchor.CENTER_CENTER);
		closeAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		options.setAnchor(WidgetAnchor.CENTER_CENTER);
		faq.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		createAccount.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(15);
		makeDeposit.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(35);
		makeWithdraw.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(55);
		closeAccount.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(75);
		options.setHeight(16).setWidth(50).shiftXPos(-110).shiftYPos(95);
		faq.setHeight(16).setWidth(20).shiftXPos(-10).shiftYPos(95);
		close.setHeight(16).setWidth(40).shiftXPos(70).shiftYPos(95);

		createAccount.setEnabled(sPlayer.hasPermission("reserve.accountadd"));
		makeDeposit.setEnabled(sPlayer.hasPermission("reserve.deposit") && (selectedBank.getAmountOfAccountsFor(sPlayer.getName()) > 0));
		makeWithdraw.setEnabled(sPlayer.hasPermission("reserve.withdraw") && (selectedBank.getAmountOfAccountsFor(sPlayer.getName()) > 0));
		closeAccount.setEnabled(sPlayer.hasPermission("reserve.accountremove") && (selectedBank.getAmountOfAccountsFor(sPlayer.getName()) > 0));
		options.setEnabled(sPlayer.hasPermission("reserve.admin"));

		attachWidgets(plugin, border, gl, bankNameLabel, gg, gb, createAccount, list, bankAccountsLabel, makeDeposit, makeWithdraw, closeAccount, faq, options, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				sPlayer.getMainScreen().closePopup();
				new CreateAccountGUI(plugin, sPlayer, selectedBank);
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				if (list.getSelectedItem() == null) {
					new DepositGUI(plugin, sPlayer, selectedBank, selectedAccount);
				} else {
					Account myAccount = selectedBank.getAccount(list.getSelectedItem().getTitle(), sPlayer.getName());
					new DepositGUI(plugin, sPlayer, selectedBank, myAccount);
				}
				break;
			case 3:
				sPlayer.getMainScreen().closePopup();
				if (list.getSelectedItem() == null) {
					new WithdrawGUI(plugin, sPlayer, selectedBank, selectedAccount);
				} else {
					Account myAccount = selectedBank.getAccount(list.getSelectedItem().getTitle(), sPlayer.getName());
					new WithdrawGUI(plugin, sPlayer, selectedBank, myAccount);
				}

				break;
			case 4:
				sPlayer.getMainScreen().closePopup();
				new DeleteAccountGUI(plugin, sPlayer, selectedBank);
				break;
			case 5:
				sPlayer.getMainScreen().closePopup();
				new OptionsGUI(plugin, sPlayer);
				break;
			case 6:
				Screen screen = sPlayer.getMainScreen();
				screen.removeWidget(this);
				sPlayer.closeActiveWindow();
				break;
		}
	}
}
