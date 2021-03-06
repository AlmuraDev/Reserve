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

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.gui.button.CommandButton;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class AckPopup extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final String ackMessage;
	private final String previousWindow;
	private final Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public AckPopup(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank, String ackMessage, String prevWindow) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;
		this.ackMessage = ackMessage;
		this.previousWindow = prevWindow;

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText("Message Center");
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericLabel an = new GenericLabel(ackMessage);
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos((GenericLabel.getStringWidth(an.getText()) / 2) * -1).shiftYPos(-40);

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);

		if (an.getWidth() < gl.getWidth()) {
			border.setWidth(gl.getWidth() + 25).setHeight(90);
			border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-80);
		} else {
			border.setWidth(an.getWidth() + 25).setHeight(90);
			border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-80);
		}

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(border.getWidth() - 25).setHeight(1);
		gg.shiftXPos(0 - gg.getWidth() / 2).shiftYPos(-55);
		GenericButton close = new CommandButton(this, 1, "OK");

		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		close.setHeight(16).setWidth(40).shiftXPos(-20).shiftYPos(-20);

		attachWidgets(plugin, border, gl, gg, an, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				sPlayer.getMainScreen().closePopup();
				if (ackMessage.equalsIgnoreCase("Please specify account.") && previousWindow.equalsIgnoreCase("depositgui")) {
					new DepositPopup(plugin, sPlayer, selectedBank, null);
				} else if (ackMessage.equalsIgnoreCase("Please select bank.") && previousWindow.equalsIgnoreCase("reservemaingui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Deposit amount has to be more than zero.") && previousWindow.equalsIgnoreCase("depositgui")) {
					new DepositPopup(plugin, sPlayer, selectedBank, null);
				} else if (ackMessage.equalsIgnoreCase("Withdraw amount has to be more than zero.") && previousWindow.equalsIgnoreCase("withdrawgui")) {
					new DepositPopup(plugin, sPlayer, selectedBank, null);
				} else if (ackMessage.equalsIgnoreCase("Insuffient funds available for deposit.") && previousWindow.equalsIgnoreCase("depositgui")) {
					new DepositPopup(plugin, sPlayer, selectedBank, null);
				} else if (ackMessage.equalsIgnoreCase("Withdraw amount cannot be greater than current balance.") && previousWindow.equalsIgnoreCase("withdrawgui")) {
					new WithdrawPopup(plugin, sPlayer, selectedBank, null);
				} else if (ackMessage.equalsIgnoreCase("Please specify name.") && previousWindow.equalsIgnoreCase("createaccountgui")) {
					new CreateAccountPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Account with that name already exists.") && previousWindow.equalsIgnoreCase("createaccountgui")) {
					new CreateAccountPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Invalid Characters in Name.") && previousWindow.equalsIgnoreCase("createaccountgui")) {
					new CreateAccountPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("You already have an account of that type.") && previousWindow.equalsIgnoreCase("createaccountgui")) {
					new CreateAccountPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Account balance is not zero.") && previousWindow.equalsIgnoreCase("deleteaccountgui")) {
					new DeleteAccountPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Please specify account.") && previousWindow.equalsIgnoreCase("deleteaccountgui")) {
					new DepositPopup(plugin, sPlayer, selectedBank, null);
				} else if (ackMessage.equalsIgnoreCase("Bank Created Successfully.") && previousWindow.equalsIgnoreCase("createbankgui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Please specify name.") && previousWindow.equalsIgnoreCase("createbankgui")) {
					new CreateBankPopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Bank already exists.") && previousWindow.equalsIgnoreCase("createbankgui")) {
					new CreateBankPopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Bank Removed.") && previousWindow.equalsIgnoreCase("deletebankgui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Please Select Bank.") && previousWindow.equalsIgnoreCase("deletebankgui")) {
					new DeleteBankPopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Changes Saved.") && previousWindow.equalsIgnoreCase("accounttypesgui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Account Type Added.") && previousWindow.equalsIgnoreCase("accounttypesgui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Specify Name.") && previousWindow.equalsIgnoreCase("accounttypesgui")) {
					new AccountTypesPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("That name already exists.") && previousWindow.equalsIgnoreCase("accounttypesgui")) {
					new AccountTypesPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Please specify new name.") && previousWindow.equalsIgnoreCase("renamebankgui")) {
					new RenameBankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Changes saved.") && previousWindow.equalsIgnoreCase("renamebankgui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("You cannot rename Bank Vault.") && previousWindow.equalsIgnoreCase("bankmaingui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Please specify a new name.") && previousWindow.equalsIgnoreCase("renameaccountgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("That name already exists.") && previousWindow.equalsIgnoreCase("renameaccountgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Changes Saved.") && previousWindow.equalsIgnoreCase("renameaccountgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("You cannot rename Bank Vault.") && previousWindow.equalsIgnoreCase("renameaccountgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Reserve Configuration Saved.") && previousWindow.equalsIgnoreCase("reserveconfiggui")) {					
					new OptionsPopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Account Created Successfully.") && previousWindow.equalsIgnoreCase("createaccountgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Funds Deposited Successfully.") && previousWindow.equalsIgnoreCase("depositgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Funds Withdrawn Successfully.") && previousWindow.equalsIgnoreCase("withdrawgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Account Removed.") && previousWindow.equalsIgnoreCase("deleteaccountgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Invalid Characters in Name.") && previousWindow.equalsIgnoreCase("renameaccountgui")) {
					new BankPopup(plugin, sPlayer, selectedBank);
				} else if (ackMessage.equalsIgnoreCase("Invalid Characters in Name.") && previousWindow.equalsIgnoreCase("renamebankgui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Invalid Characters in Name.") && previousWindow.equalsIgnoreCase("createbankgui")) {
					new ReservePopup(plugin, sPlayer);
				} else if (ackMessage.equalsIgnoreCase("Please specify new name.") && previousWindow.equalsIgnoreCase("renamebankgui")) {
					new ReservePopup(plugin, sPlayer);
				} else {
					// unhandled return, don't start another screen.
					System.out.println("Window: " + previousWindow);
				}
				break;
		}
	}
}
