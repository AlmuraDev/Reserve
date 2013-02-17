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

import org.bukkit.ChatColor;

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

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText("Reserve");
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-110);

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
		list.shiftXPos(-105).shiftYPos(-90);
		list.setWidth(210).setHeight(80);
		list.setPriority(RenderPriority.Lowest);
		list.setSelection(0);

		GenericButton createNewBank = new CommandButton(this, 1, "New");
		GenericButton renameBank = new CommandButton(this, 2, "Rename");
		GenericButton deleteBank = new CommandButton(this, 3, "Delete");
		GenericButton openBank = new CommandButton(this, 4, "Access Bank");
		GenericButton bankStatus = new CommandButton(this, 5, "Bank Status");
		GenericButton accountTypes = new CommandButton(this, 6, "Account Types");
		GenericButton options = new CommandButton(this, 7, "Options");
		GenericButton close = new CommandButton(this, 8, "Close");

		createNewBank.setAnchor(WidgetAnchor.CENTER_CENTER);
		renameBank.setAnchor(WidgetAnchor.CENTER_CENTER);
		deleteBank.setAnchor(WidgetAnchor.CENTER_CENTER);
		openBank.setAnchor(WidgetAnchor.CENTER_CENTER);
		bankStatus.setAnchor(WidgetAnchor.CENTER_CENTER);
		accountTypes.setAnchor(WidgetAnchor.CENTER_CENTER);
		options.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		createNewBank.setHeight(16).setWidth(40).shiftXPos(-75).shiftYPos(0);
		renameBank.setHeight(16).setWidth(50).shiftXPos(-25).shiftYPos(0);
		deleteBank.setHeight(16).setWidth(40).shiftXPos(35).shiftYPos(0);
		openBank.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(20);
		bankStatus.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(40);
		accountTypes.setHeight(16).setWidth(120).shiftXPos(-60).shiftYPos(60);
		options.setHeight(16).setWidth(50).shiftXPos(-110).shiftYPos(95);
		close.setHeight(16).setWidth(40).shiftXPos(70).shiftYPos(95);
		
		createNewBank.setTooltip("Creates New Bank in your current world.");
		renameBank.setTooltip("Renames the specified bank.");
		deleteBank.setTooltip("Deletes the specified bank and all accounts.");
		options.setTooltip("Displays Configuration options for Reserve.");
		openBank.setTooltip("Opens and Accesses selected bank.");
		accountTypes.setTooltip("Account Types Configuration per Bank");
		bankStatus.setTooltip("Displays selected bank status.");

		createNewBank.setEnabled(sPlayer.hasPermission("reserve.addbank"));
		renameBank.setEnabled(sPlayer.hasPermission("reserve.addbank"));
		deleteBank.setEnabled(sPlayer.hasPermission("reserve.removebank"));
		options.setEnabled(sPlayer.hasPermission("reserve.admin"));
		openBank.setEnabled(sPlayer.hasPermission("reserve.viewbank"));
		bankStatus.setEnabled(sPlayer.hasPermission("reserve.viewbank"));
		accountTypes.setEnabled(sPlayer.hasPermission("reserve.admin"));		

		if (list.getSelectedItem() == null) {
			openBank.setEnabled(false);
			deleteBank.setEnabled(false);
			bankStatus.setEnabled(false);
			renameBank.setEnabled(false);
			accountTypes.setEnabled(false);			
		} else {			
			openBank.setEnabled(true);
			deleteBank.setEnabled(true);
			bankStatus.setEnabled(true);
			renameBank.setEnabled(true);
			accountTypes.setEnabled(true);
		}

		attachWidgets(plugin, border, gl, gg, gb, createNewBank, list, renameBank, deleteBank, openBank, accountTypes, bankStatus, options, close);

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
				if (list.getSelectedItem() == null) {
					new AckGUI(plugin, sPlayer, null, "Please Select Bank.", "reservemaingui");
				} else {
					String[] split = list.getSelectedItem().getTitle().split("\\/");
					sPlayer.getMainScreen().closePopup();
					new RenameBankGUI(plugin, sPlayer, ReservePlugin.getReserve().get(split[0].trim(), ChatColor.stripColor(split[1].trim())));
				}
				break;
			case 3:
				sPlayer.getMainScreen().closePopup();
				new DeleteBankGUI(plugin, sPlayer);
				break;
			case 4:
				sPlayer.getMainScreen().closePopup();
				if (list.getSelectedItem() == null) {
					new AckGUI(plugin, sPlayer, null, "Please Select Bank.", "reservemaingui");
				} else {
					sPlayer.getMainScreen().closePopup();
					String[] split = list.getSelectedItem().getTitle().split("\\/");
					new BankMainGUI(plugin, sPlayer, ReservePlugin.getReserve().get(split[0].trim(), ChatColor.stripColor(split[1].trim())));
				}
				break;
			case 5:
				sPlayer.getMainScreen().closePopup();
				if (list.getSelectedItem() == null) {
					new AckGUI(plugin, sPlayer, null, "Please Select Bank.", "reservemaingui");
				} else {
					sPlayer.getMainScreen().closePopup();
					String[] split = list.getSelectedItem().getTitle().split("\\/");
					new BankStatusGUI(plugin, sPlayer, ReservePlugin.getReserve().get(split[0].trim(), ChatColor.stripColor(split[1].trim())));					
				}
				break;
			case 6:
				if (list.getSelectedItem() == null) {
					new AckGUI(plugin, sPlayer, null, "Please Select Bank.", "reservemaingui");
				} else {
				sPlayer.getMainScreen().closePopup();
				String [] split = list.getSelectedItem().getTitle().split("\\/");
				new AccountTypesGUI(plugin, sPlayer, ReservePlugin.getReserve().get(split[0].trim(), ChatColor.stripColor(split[1].trim())));
				}
				break;
			case 7:
				sPlayer.getMainScreen().closePopup();
				new OptionsGUI(plugin, sPlayer);
				break;
			case 8:
				Screen screen = sPlayer.getMainScreen();
				screen.removeWidget(this);
				sPlayer.closeActiveWindow();
				break;
		}
	}
}
