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

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class MainGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public MainGUI(ReservePlugin plugin, SpoutPlayer sPlayer) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(170).setHeight(170);
		border.shiftXPos(-85).shiftYPos(-80);

		GenericLabel gl = new GenericLabel("Almura Bank");
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(-35).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.shiftXPos(-65).shiftYPos(-55).setMaxWidth(130);
		gg.setWidth(130).setHeight(1);

		GenericGradient gb = new GenericGradient();
		gb.setBottomColor(bottom).setTopColor(bottom);
		gb.setAnchor(WidgetAnchor.CENTER_CENTER);
		gb.shiftXPos(-65).shiftYPos(-25).setMaxWidth(130);
		gb.setWidth(130).setHeight(1);

		//if (playerBank != null) {
		//final String plat = numForm.format(playerBank.getTotalBalance());
		GenericLabel balanceLabel = new GenericLabel();
		balanceLabel.setScale(1.0F);
		balanceLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		balanceLabel.setText("Bank Balance: $2,154,225.00");
		//gl.setText("Bank Balance: " + plat);
		balanceLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		balanceLabel.shiftXPos(-70).shiftYPos(-44);
		//}

		GenericButton createAccount = new CommandButton(this, 1, "Open New Account");
		GenericButton makeDeposit = new CommandButton(this, 2, "Make Deposit");
		GenericButton makeWithdraw = new CommandButton(this, 3, "Make Withdraw");
		GenericButton closeAccount = new CommandButton(this, 4, "Close Account");
		GenericButton config = new CommandButton(this, 5, "Config");
		GenericButton close = new CommandButton(this, 6, "Close");

		createAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		makeDeposit.setAnchor(WidgetAnchor.CENTER_CENTER);
		makeWithdraw.setAnchor(WidgetAnchor.CENTER_CENTER);
		closeAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		config.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		createAccount.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(-20);
		makeDeposit.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(0);
		makeWithdraw.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(20);
		closeAccount.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(40);
		config.setHeight(16).setWidth(40).shiftXPos(-60).shiftYPos(68);
		close.setHeight(16).setWidth(40).shiftXPos(20).shiftYPos(68);

		//createAccount.setEnabled(sPlayer.hasPermission("reserve.createaccount") && playerBank == null);
		//makeDeposit.setEnabled(sPlayer.hasPermission("reserve.deposit") && playerBank != null);
		//makeWithdraw.setEnabled(sPlayer.hasPermission("reserve.withdraw") && playerBank != null);
		//closeAccount.setEnabled(sPlayer.hasPermission("reserve.closeaccount") && playerBank != null) ;

		attachWidgets(plugin, border, gl, balanceLabel, gg, gb, createAccount, makeDeposit, makeWithdraw, closeAccount, config, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				sPlayer.getMainScreen().closePopup();
				new CreateAccountGUI(plugin, sPlayer);
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new DepositGUI(plugin, sPlayer);
				break;
			case 3:
				sPlayer.getMainScreen().closePopup();
				new WithdrawGUI(plugin, sPlayer);
				break;
			case 4:
				sPlayer.getMainScreen().closePopup();
				//new closeAccountGUI(mainGUI, sPlayer, true);
				break;
			case 5:
				sPlayer.getMainScreen().closePopup();
				new BankConfigGUI(plugin, sPlayer);
				break;
			case 6:
				Screen screen = sPlayer.getMainScreen();
				screen.removeWidget(this);
				sPlayer.closeActiveWindow();
				break;
		}
	}
}
