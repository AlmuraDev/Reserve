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
import com.almuradev.reserve.storage.Bank;
import com.almuradev.reserve.storage.Reserve;

import org.bukkit.Bukkit;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class MainGUI extends GenericPopup {

	private ReservePlugin mainGUI;
	private SpoutPlayer sPlayer;
	private Bank playerBank = ReservePlugin.getReserve().getAccount(sPlayer.getWorld(), sPlayer.getName());	

	public MainGUI(ReservePlugin main, SpoutPlayer who) {

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(170).setHeight(150);
		border.shiftXPos(-85).shiftYPos(-80);

		GenericLabel gl = new GenericLabel("Almura Bank");
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(-70).shiftYPos(-70);
		
		GenericButton createAccount = new CommandButton(this, 1, "Open New Account");
		GenericButton makeDeposit = new CommandButton(this, 2, "Make Deposit");
		GenericButton makeWithdraw = new CommandButton(this, 3, "Make Withdraw");
		GenericButton closeAccount = new CommandButton(this, 4, "Close Account");
		GenericButton close = new CommandButton(this, 5, "Close");
		
		createAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		makeDeposit.setAnchor(WidgetAnchor.CENTER_CENTER);
		makeWithdraw.setAnchor(WidgetAnchor.CENTER_CENTER);
		closeAccount.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		
		createAccount.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(-40);
		makeDeposit.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(-20);
		makeWithdraw.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(0);
		closeAccount.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(20);
		close.setHeight(16).setWidth(40).shiftXPos(20).shiftYPos(48);
	
		createAccount.setEnabled(sPlayer.hasPermission("reserve.createaccount") && playerBank == null);
		makeDeposit.setEnabled(sPlayer.hasPermission("reserve.deposit") && playerBank != null);
		makeWithdraw.setEnabled(sPlayer.hasPermission("reserve.withdraw") && playerBank != null);
		closeAccount.setEnabled(sPlayer.hasPermission("reserve.closeaccount") && playerBank != null) ;
		
		attachWidgets(mainGUI, border, gl, createAccount, makeDeposit, makeWithdraw, closeAccount, close);
		
		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
		case 1: //Create
			sPlayer.getMainScreen().closePopup();
			//new createAccountGUI(mainGUI, sPlayer, true);
			break;
		case 2: //Edit
			sPlayer.getMainScreen().closePopup();
			//new depositGUI(mainGUI, sPlayer, true);				
			break;
		case 3: //View
			sPlayer.getMainScreen().closePopup();
			//new withdrawGUI(mainGUI, sPlayer, true);				
			break;
		case 4: //View
			sPlayer.getMainScreen().closePopup();
			//new closeAccountGUI(mainGUI, sPlayer, true);				
			break;
		case 5:
			Screen screen = ((SpoutPlayer) getPlayer()).getMainScreen();
			screen.removeWidget(this);				
			((SpoutPlayer) getPlayer()).closeActiveWindow();
			break;
		}
	}
}
