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
package com.almuradev.reserve.gui.popup;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
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

public class RenameAccountPopup extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final Account myAccount;
	private final GenericTextField accountNameField;
	private final Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public RenameAccountPopup(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank, Account account) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;
		this.myAccount = account;

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText("Rename Account");
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(gl.getWidth() + 50).setHeight(90);
		border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-80);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(border.getWidth() - 25).setHeight(1);
		gg.shiftXPos(0 - gg.getWidth() / 2).shiftYPos(-55);

		accountNameField = new GenericTextField();
		accountNameField.setPlaceholder(myAccount.getName().trim());
		accountNameField.setWidth(110).setHeight(16);
		accountNameField.setAnchor(WidgetAnchor.CENTER_CENTER);
		accountNameField.shiftXPos(-55).shiftYPos(-45);
		accountNameField.setMaximumCharacters(30);
		accountNameField.setMaximumLines(1);		

		GenericButton close = new CommandButton(this, 1, "Close");
		GenericButton save = new CommandButton(this, 2, "Save");

		save.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		save.setHeight(16).setWidth(40).shiftXPos(-30).shiftYPos(-10);
		close.setHeight(16).setWidth(40).shiftXPos(20).shiftYPos(-10);

		attachWidgets(plugin, border, gl, gg, accountNameField, save, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				sPlayer.getMainScreen().closePopup();
				new BankPopup(plugin, sPlayer, selectedBank);
				break;
			case 2:
				if (accountNameField.getText().isEmpty()) {
					new AckPopup(plugin, sPlayer, selectedBank, "Please specify a new name.", "renameaccountgui");
				} else if (accountNameField.getText().trim().equalsIgnoreCase("Bank Vault")) {
					sPlayer.getMainScreen().closePopup();
					new AckPopup(plugin, sPlayer, selectedBank, "You cannot rename Bank Vault.", "renameaccountgui");
				} else if (selectedBank.getAccount(accountNameField.getText().trim(), sPlayer.getName()) != null) {
					sPlayer.getMainScreen().closePopup();
					new AckPopup(plugin, sPlayer, selectedBank, "That name already exists.", "renameaccountgui");
				} else {
					myAccount.setName(accountNameField.getText().trim());
					sPlayer.getMainScreen().closePopup();
					new AckPopup(plugin, sPlayer, selectedBank, "Changes Saved.", "renameaccountgui");
				}
				break;
		}
	}
}
