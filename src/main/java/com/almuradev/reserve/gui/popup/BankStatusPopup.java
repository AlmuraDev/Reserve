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
import java.util.Locale;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.gui.applet.MasterAccountListApplet;
import com.almuradev.reserve.gui.button.CommandButton;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.ListWidget;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.ChatColor;

public class BankStatusPopup extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private ListWidget list;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	private final Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public BankStatusPopup(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

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

		list = new MasterAccountListApplet(selectedBank);
		list.setAnchor(WidgetAnchor.CENTER_CENTER);
		list.shiftXPos(-100).shiftYPos(-60);
		list.setWidth(200).setHeight(140);
		list.setPriority(RenderPriority.Lowest);
		list.setSelection(0);

		GenericLabel bankAccountsLabel = new GenericLabel();
		bankAccountsLabel.setScale(1.0F);
		bankAccountsLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		bankAccountsLabel.setText("Total Accounts: " + ChatColor.YELLOW + selectedBank.getAmountOfAccounts());
		bankAccountsLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(bankAccountsLabel.getText()));
		bankAccountsLabel.shiftXPos((GenericLabel.getStringWidth(bankAccountsLabel.getText()) / 2) * -1).shiftYPos(-89);

		GenericLabel bankNameLabel = new GenericLabel();
		numForm = NumberFormat.getCurrencyInstance(caLoc);
		bankNameLabel.setScale(1.0F);
		bankNameLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		bankNameLabel.setText("Total Balance: " + ChatColor.GOLD + numForm.format(selectedBank.getAccountsBalance()));
		bankNameLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(bankNameLabel.getText()));
		bankNameLabel.shiftXPos((GenericLabel.getStringWidth(bankNameLabel.getText()) / 2) * -1).shiftYPos(-79);

		GenericButton close = new CommandButton(this, 2, "Close");
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setHeight(16).setWidth(40).shiftXPos(70).shiftYPos(95);

		attachWidgets(plugin, border, gl, bankNameLabel, gg, gb, list, bankAccountsLabel, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				sPlayer.getMainScreen().closePopup();
				new AccountTypesPopup(plugin, sPlayer, selectedBank);
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new ReservePopup(plugin, sPlayer);
				break;
		}
	}
}
