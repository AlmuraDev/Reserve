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
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.econ.type.AccountType;
import com.almuradev.reserve.gui.button.CommandButton;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CreateBankPopup extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private GenericTextField bankNameField;
	private final Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public CreateBankPopup(ReservePlugin plugin, SpoutPlayer sPlayer) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(225).setHeight(100);
		border.shiftXPos(-105).shiftYPos(-80);

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText("Reserve");
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(130).setHeight(1);
		gg.shiftXPos(0 - (gg.getWidth() / 2)).shiftYPos(-55).setMaxWidth(130);

		GenericLabel cl = new GenericLabel("-- Create New Bank --");
		cl.setScale(1.0F);
		cl.setAnchor(WidgetAnchor.CENTER_CENTER);
		cl.setHeight(15).setWidth(GenericLabel.getStringWidth(cl.getText()));
		cl.shiftXPos(0 - (cl.getWidth() / 2)).shiftYPos(-47);

		GenericLabel an = new GenericLabel("Name: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-90).shiftYPos(-25);

		bankNameField = new GenericTextField();
		bankNameField.setWidth(150).setHeight(16);
		bankNameField.setAnchor(WidgetAnchor.CENTER_CENTER);
		bankNameField.shiftXPos(-50).shiftYPos(-28);
		bankNameField.setMaximumCharacters(25);
		bankNameField.setMaximumLines(1);

		GenericButton createAccount = new CommandButton(this, 1, "Create");
		GenericButton close = new CommandButton(this, 2, "Close");

		createAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		createAccount.setHeight(16).setWidth(50).shiftXPos(7).shiftYPos(0);
		close.setHeight(16).setWidth(40).shiftXPos(62).shiftYPos(0);

		attachWidgets(plugin, border, gl, gg, cl, an, bankNameField, createAccount, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);

		bankNameField.setFocus(true);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				if (bankNameField.getText().isEmpty()) {
					new AckPopup(plugin, sPlayer, null, "Please specify name.", "createbankgui");
				} else {
					final String input = bankNameField.getText().trim();
					if (!input.matches(plugin.INPUT_REGEX)) {
						new AckPopup(plugin, sPlayer, null, "Invalid characters entered for bank name.", "createbankgui");
					} else if (ReservePlugin.getReserve().get(input, sPlayer.getWorld().getName()) != null) {
						new AckPopup(plugin, sPlayer, null, "Bank name already exists.", "createbankgui");
					} else {
						Bank selectedBank = ReservePlugin.getReserve().add(input, sPlayer.getName(), sPlayer.getWorld().getName());
						selectedBank.setBalance(Double.parseDouble("0.0"));

						// Create Bank Vault & Vault Type.						
						AccountType newAccountType = selectedBank.addType(new AccountType("Vault"));
						newAccountType.setInterestRate(Double.parseDouble("0.0"));
						newAccountType.setImagePath("http://www.almuramc.com/images/vault.png");
						newAccountType.shouldReceiveInterest(false);

						AccountType newAccountType1 = selectedBank.addType(new AccountType("Savings"));
						newAccountType1.setInterestRate(Double.parseDouble("0.0"));
						newAccountType1.setImagePath("http://www.almuramc.com/images/savings.png");
						newAccountType1.shouldReceiveInterest(false);

						AccountType newAccountType2 = selectedBank.addType(new AccountType("Checking"));
						newAccountType2.setInterestRate(Double.parseDouble("0.0"));
						newAccountType2.setImagePath("http://www.almuramc.com/images/check.png");
						newAccountType2.shouldReceiveInterest(false);

						selectedBank.addAccount(new Account(selectedBank.getType("Vault"), "Bank Vault", sPlayer.getName()));

						sPlayer.getMainScreen().closePopup();
						new AckPopup(plugin, sPlayer, null, "Bank Created Successfully.", "createbankgui");
					}
				}
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new ReservePopup(plugin, sPlayer);
				break;
		}
	}
}
