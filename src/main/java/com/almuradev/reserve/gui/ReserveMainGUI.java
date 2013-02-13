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
import org.getspout.spoutapi.gui.ListWidget;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ReserveMainGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	private ListWidget list;
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public ReserveMainGUI(ReservePlugin plugin, SpoutPlayer sPlayer) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(250).setHeight(250);
		border.shiftXPos(-125).shiftYPos(-120);

		GenericLabel gl = new GenericLabel("Reserve");
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(((GenericLabel.getStringWidth(gl.getText()) / 2) * -1) - 4).shiftYPos(-110);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.shiftXPos(-115).shiftYPos(-95).setMaxWidth(230);
		gg.setWidth(230).setHeight(1);

		GenericGradient gb = new GenericGradient();
		gb.setBottomColor(bottom).setTopColor(bottom);
		gb.setAnchor(WidgetAnchor.CENTER_CENTER);
		gb.shiftXPos(-115).shiftYPos(-5).setMaxWidth(230);
		gb.setWidth(230).setHeight(1);

		list = new BankListApplet();
		list.setAnchor(WidgetAnchor.CENTER_CENTER);
		list.shiftXPos(-80).shiftYPos(-90);
		list.setWidth(155).setHeight(80);
		list.setPriority(RenderPriority.Lowest);

		GenericButton createNewBank = new CommandButton(this, 1, "Make New Bank");
		GenericButton renameBank = new CommandButton(this, 2, "Rename Bank");
		GenericButton deleteBank = new CommandButton(this, 3, "Delete Bank");
		GenericButton openBank = new CommandButton(this, 4, "Open Bank");
		GenericButton options = new CommandButton(this, 5, "Options");
		GenericButton close = new CommandButton(this, 6, "Close");

		createNewBank.setAnchor(WidgetAnchor.CENTER_CENTER).setVisible(false);
		renameBank.setAnchor(WidgetAnchor.CENTER_CENTER).setVisible(false);
		deleteBank.setAnchor(WidgetAnchor.CENTER_CENTER).setVisible(false);
		openBank.setAnchor(WidgetAnchor.CENTER_CENTER).setVisible(false);
		options.setAnchor(WidgetAnchor.CENTER_CENTER).setVisible(false);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		createNewBank.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(0);
		renameBank.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(20);
		deleteBank.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(40);
		openBank.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(60);
		options.setHeight(16).setWidth(50).shiftXPos(-60).shiftYPos(88);
		close.setHeight(16).setWidth(40).shiftXPos(20).shiftYPos(88);
		
		if (sPlayer.hasPermission("reserve.admin") || sPlayer.hasPermission("reserve.addbank")) {
			createNewBank.setVisible(true);
			renameBank.setVisible(true);
		}
		
		if (sPlayer.hasPermission("reserve.admin") || sPlayer.hasPermission("reserve.removebank")) {
			renameBank.setVisible(true);
			deleteBank.setVisible(true);			
		}
		
		if (sPlayer.hasPermission("reserve.admin")) {
			options.setVisible(true);			
		}
		
		if (sPlayer.hasPermission("reserve.viewbank")) {
			openBank.setVisible(true);			
		}

		if (list.getItems() == null) {
			openBank.setEnabled(false);
		} else {
			openBank.setEnabled(true);
		}

		attachWidgets(plugin, border, gl, gg, gb, createNewBank, list, renameBank, deleteBank, openBank, options, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				sPlayer.getMainScreen().closePopup();
				new CreateBankGUI(plugin, sPlayer);
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				//new DepositGUI(plugin, sPlayer);
				break;
			case 3:
				sPlayer.getMainScreen().closePopup();
				//new WithdrawGUI(plugin, sPlayer);
				break;
			case 4:
				if (list.getSelectedItem() == null) {
					new AckGUI(plugin, sPlayer, null, "Please Select Bank.", "reservemaingui");
				} else {
					sPlayer.getMainScreen().closePopup();
					new BankMainGUI(plugin, sPlayer, ReservePlugin.getReserve().get(list.getSelectedItem().getTitle(), list.getSelectedItem().getText()));
				}
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
