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

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ComboBox;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class WithdrawGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final ComboBox box;
	private final GenericTextField depositAmountField;
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public WithdrawGUI(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(255).setHeight(150);
		border.shiftXPos(-105).shiftYPos(-80);

		GenericLabel gl = new GenericLabel("Reserve");
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(-10).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.shiftXPos(-45).shiftYPos(-55).setMaxWidth(130);
		gg.setWidth(130).setHeight(1);

		GenericLabel cl = new GenericLabel("Select Account: ");
		cl.setScale(1.0F);
		cl.setAnchor(WidgetAnchor.CENTER_CENTER);
		cl.setHeight(15).setWidth(GenericLabel.getStringWidth(cl.getText()));
		cl.shiftXPos(-95).shiftYPos(-42);

		box = new AccountWithdrawCombo(this);
		box.setText("Accounts");
		box.setAnchor(WidgetAnchor.CENTER_CENTER);
		box.setWidth(GenericLabel.getStringWidth("12345678901234567890123459"));
		box.setHeight(18);
		box.shiftXPos(-15).shiftYPos(-47);
		box.setAuto(true);
		box.setPriority(RenderPriority.Low);

		GenericLabel an = new GenericLabel("Withdraw Amount: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-95).shiftYPos(-10);

		depositAmountField = new GenericTextField();
		depositAmountField.setWidth(110).setHeight(16);
		depositAmountField.setAnchor(WidgetAnchor.CENTER_CENTER);
		depositAmountField.shiftXPos(-10).shiftYPos(-13);
		depositAmountField.setText("0.00");
		depositAmountField.setMaximumCharacters(15);
		depositAmountField.setMaximumLines(1);

		GenericButton depositButton = new CommandButton(this, 1, "Withdraw");
		GenericButton close = new CommandButton(this, 2, "Close");

		depositButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		depositButton.setHeight(16).setWidth(50).shiftXPos(30).shiftYPos(47);
		close.setHeight(16).setWidth(40).shiftXPos(85).shiftYPos(47);

		attachWidgets(plugin, border, gl, gg, box, cl, depositAmountField, an, depositButton, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1: // Ok
				if (box.getSelectedItem() == null) {
					new AckGUI(plugin, sPlayer, selectedBank, "Please specify account.", "depositgui");
				} else {

					Account myAccount = ReservePlugin.getReserve().getAccountFromNameIn(selectedBank, box.getSelectedItem(), sPlayer.getName());
					double withdraw = 0;
					try {
						double value = Math.abs(Double.parseDouble(depositAmountField.getText()));
						withdraw = value;
					} catch (Exception e) {
						//do nothing
					}
					if (withdraw == 0) {
						sPlayer.getMainScreen().closePopup();
						new AckGUI(plugin, sPlayer, selectedBank, "Deposit amount has to be more than zero.", "withdrawgui");
					} else {
						// Remove from Users Economy
						if (myAccount.getBalance() < withdraw) {
							new AckGUI(plugin, sPlayer, selectedBank, "Withdraw amount cannot be greater than current balance.", "withdrawgui");
						} else {
							myAccount.add(withdraw);
							sPlayer.getMainScreen().closePopup();
							new AckGUI(plugin, sPlayer, selectedBank, "Funds Deposited Successfully", "depositgui");
						}
					}
				}
				break;
			case 2: // Close
				sPlayer.getMainScreen().closePopup();
				new BankMainGUI(plugin, sPlayer, selectedBank);
				break;
		}
	}

	void onSelect(int i, String text) {
		// set Current loaded econ
	}
}
