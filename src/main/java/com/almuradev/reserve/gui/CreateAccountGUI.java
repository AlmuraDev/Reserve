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
import com.almuradev.reserve.econ.type.AccountType;

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

public class CreateAccountGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private Bank selectedBank;
	private GenericTextField accountNameField;
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public CreateAccountGUI(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(225).setHeight(100);
		border.shiftXPos(-105).shiftYPos(-80);

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText(selectedBank.getName());
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(200).setHeight(1);
		gg.shiftXPos(-100).shiftYPos(-55);

		GenericLabel cl = new GenericLabel("-- Create Account --");
		cl.setScale(1.0F);
		cl.setAnchor(WidgetAnchor.CENTER_CENTER);
		cl.setHeight(15).setWidth(GenericLabel.getStringWidth(cl.getText()));
		cl.shiftXPos(-45).shiftYPos(-47);

		GenericLabel an = new GenericLabel("Account Name: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-90).shiftYPos(-25);

		accountNameField = new GenericTextField();
		accountNameField.setWidth(110).setHeight(16);
		accountNameField.setAnchor(WidgetAnchor.CENTER_CENTER);
		accountNameField.shiftXPos(-10).shiftYPos(-28);
		accountNameField.setMaximumCharacters(30);
		accountNameField.setMaximumLines(1);

		GenericButton createAccount = new CommandButton(this, 1, "Create");
		GenericButton close = new CommandButton(this, 2, "Close");

		createAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		createAccount.setHeight(16).setWidth(50).shiftXPos(7).shiftYPos(0);
		close.setHeight(16).setWidth(40).shiftXPos(62).shiftYPos(0);

		attachWidgets(plugin, border, gl, gg, cl, an, accountNameField, createAccount, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1: //Create
				if (accountNameField.getText().isEmpty()) {
					new AckGUI(plugin, sPlayer, selectedBank, "Please specify name.", "createaccountgui");
				} else {
					if (selectedBank.getAccount(accountNameField.getText(), sPlayer.getName()) != null) {
						new AckGUI(plugin, sPlayer, selectedBank, "Account already exists.", "createaccountgui");
					} else {
						//TODO TESTING CODE - REMOVE DOCKTER
						selectedBank.addType(new AccountType("checking"));
						//TODO NEED PROPER GUI
						selectedBank.addAccount(new Account(selectedBank.getType("checking"), accountNameField.getText(), sPlayer.getName()));
						//TODO END OF TESTING CODE
						sPlayer.getMainScreen().closePopup();
						new AckGUI(plugin, sPlayer, selectedBank, "Account Created Successfully", "createaccountgui");
					}
				}
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new BankMainGUI(plugin, sPlayer, selectedBank);
				break;
		}
	}
}
