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
import com.almuradev.reserve.econ.Bank;

import org.getspout.spoutapi.gui.CheckBox;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericCheckBox;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ReserveConfigGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final GenericTextField saveTime, intTime;
	private final GenericCheckBox deathCheckBox, gainInterest;
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public ReserveConfigGUI(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(255).setHeight(150);
		border.shiftXPos(-105).shiftYPos(-80);

		GenericLabel gl = new GenericLabel("Reserve Configuration");
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(-60).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.shiftXPos(-45).shiftYPos(-55).setMaxWidth(130);
		gg.setWidth(130).setHeight(1);

		deathCheckBox = new ConfigMultipleCheckBox(sPlayer, plugin);
		deathCheckBox.setText("Player Death Penalty");		
		deathCheckBox.setEnabled(ReservePlugin.getConfiguration().shouldTaxDeath());
		deathCheckBox.setAnchor(WidgetAnchor.CENTER_CENTER);
		deathCheckBox.setHeight(20).setWidth(19);
		deathCheckBox.shiftXPos(-45).shiftYPos(-42);

		gainInterest = new ConfigShareCheckBox(sPlayer, plugin);
		gainInterest.setText("Account Gain Interest");
		gainInterest.setEnabled(ReservePlugin.getConfiguration().shouldInterest());
		gainInterest.setAnchor(WidgetAnchor.CENTER_CENTER);
		gainInterest.setHeight(20).setWidth(19);
		gainInterest.shiftXPos(-45).shiftYPos(-17);

		GenericLabel an = new GenericLabel("Interest Time Interval: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-65).shiftYPos(16);

		saveTime = new GenericTextField();
		saveTime.setWidth(50).setHeight(16);
		saveTime.setText(Double.toString(ReservePlugin.getConfiguration().getSaveInterval()));
		saveTime.setAnchor(WidgetAnchor.CENTER_CENTER);
		saveTime.shiftXPos(30).shiftYPos(13);		
		saveTime.setMaximumCharacters(5);
		saveTime.setMaximumLines(1);
		
		intTime = new GenericTextField();
		intTime.setWidth(50).setHeight(16);
		intTime.setText(Double.toString(ReservePlugin.getConfiguration().getInterestInterval()));
		intTime.setAnchor(WidgetAnchor.CENTER_CENTER);
		intTime.shiftXPos(30).shiftYPos(33);		
		intTime.setMaximumCharacters(5);
		intTime.setMaximumLines(1);

		GenericButton depositButton = new CommandButton(this, 1, "Save");
		GenericButton close = new CommandButton(this, 2, "Close");

		depositButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		depositButton.setHeight(16).setWidth(50).shiftXPos(30).shiftYPos(47);
		close.setHeight(16).setWidth(40).shiftXPos(85).shiftYPos(47);

		attachWidgets(plugin, border, gl, gg, deathCheckBox, gainInterest, saveTime, intTime, an, depositButton, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1: //Create
				sPlayer.getMainScreen().closePopup();
				new AckGUI(plugin, sPlayer, selectedBank, "Reserve Configuration Saved.", "reserveconfiggui");
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new OptionsGUI(plugin, sPlayer);
				break;
		}
	}

	void onSelect(int i, String text) {
		// set Current loaded econ
	}
}
