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

import org.getspout.spoutapi.gui.CheckBox;
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

public class BankConfigGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public BankConfigGUI(ReservePlugin plugin, SpoutPlayer sPlayer) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;

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

		CheckBox multipleCheckbox = new ConfigMultipleCheckBox(sPlayer, this);
		multipleCheckbox.setText("Allow Multiple Accounts");
		multipleCheckbox.setAnchor(WidgetAnchor.CENTER_CENTER);
		multipleCheckbox.setHeight(20).setWidth(19);
		multipleCheckbox.shiftXPos(-45).shiftYPos(-42);

		CheckBox shareCheckbox = new ConfigShareCheckBox(sPlayer, this);
		shareCheckbox.setText("Allow Multiple Accounts");
		shareCheckbox.setAnchor(WidgetAnchor.CENTER_CENTER);
		shareCheckbox.setHeight(20).setWidth(19);
		shareCheckbox.shiftXPos(-45).shiftYPos(-17);

		GenericLabel an = new GenericLabel("Interest Calc: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-45).shiftYPos(16);

		GenericTextField interestAmountField = new GenericTextField();
		interestAmountField.setWidth(50).setHeight(16);
		interestAmountField.setAnchor(WidgetAnchor.CENTER_CENTER);
		interestAmountField.shiftXPos(30).shiftYPos(13);
		interestAmountField.setText("0.00");
		interestAmountField.setMaximumCharacters(5);
		interestAmountField.setMaximumLines(1);

		GenericButton depositButton = new CommandButton(this, 1, "Save");
		GenericButton close = new CommandButton(this, 2, "Close");

		depositButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		depositButton.setHeight(16).setWidth(50).shiftXPos(30).shiftYPos(47);
		close.setHeight(16).setWidth(40).shiftXPos(85).shiftYPos(47);

		attachWidgets(plugin, border, gl, gg, shareCheckbox, multipleCheckbox, interestAmountField, an, depositButton, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1: //Create
				sPlayer.getMainScreen().closePopup();
				new AckGUI(plugin, sPlayer, "Bank Configuration Saved");
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new MainGUI(plugin, sPlayer);
				break;
		}
	}

	void onSelect(int i, String text) {
		// set Current loaded econ
	}
}
