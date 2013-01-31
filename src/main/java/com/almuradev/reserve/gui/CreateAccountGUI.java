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
import com.almuradev.reserve.storage.Bank;
import com.almuradev.reserve.storage.Reserve;
import com.almuradev.reserve.gui.MainGUI;

import org.bukkit.Bukkit;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CreateAccountGUI extends GenericPopup {

	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank playerBank;
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);
	
	public CreateAccountGUI(ReservePlugin plugin, SpoutPlayer sPlayer) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;		
		//Check if playerBank is null here and handle appropriately. May want to check this BEFORE you get to actually
		//constructing the GUI (ie in the right click of a NPC).
		
		this.playerBank = plugin.getReserve().getAccount(sPlayer.getWorld(), sPlayer.getName());
		
		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(225).setHeight(100);
		border.shiftXPos(-105).shiftYPos(-80);

		GenericLabel gl = new GenericLabel("Almura Bank");
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(-30).shiftYPos(-70);
		
		GenericGradient gg =  new GenericGradient(); 
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.shiftXPos(-65).shiftYPos(-55).setMaxWidth(130);
		gg.setWidth(130).setHeight(1);
		
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
		
		GenericTextField accountNameField = new GenericTextField();
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
			sPlayer.getMainScreen().closePopup();
			new AckGUI(plugin, sPlayer, "Account Created Successfully");
			break;	
		case 2:
			sPlayer.getMainScreen().closePopup();
			new MainGUI(plugin, sPlayer);
			break;
		}
	}
}