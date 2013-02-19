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

public class RenameBankPopup extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final GenericTextField bankNameField;
	private final Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public RenameBankPopup(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText("Rename Bank");
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(gl.getWidth() + 80).setHeight(90);
		border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-80);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(border.getWidth() - 25).setHeight(1);
		gg.shiftXPos(0 - gg.getWidth() / 2).shiftYPos(-55);

		bankNameField = new GenericTextField();
		bankNameField.setText(selectedBank.getName().trim());
		bankNameField.setWidth(140).setHeight(16);
		bankNameField.setAnchor(WidgetAnchor.CENTER_CENTER);
		bankNameField.shiftXPos(-70).shiftYPos(-45);
		bankNameField.setMaximumCharacters(25);
		bankNameField.setMaximumLines(1);

		GenericButton close = new CommandButton(this, 1, "Close");
		GenericButton save = new CommandButton(this, 2, "Save");

		save.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		save.setHeight(16).setWidth(40).shiftXPos(-30).shiftYPos(-10);
		close.setHeight(16).setWidth(40).shiftXPos(20).shiftYPos(-10);

		attachWidgets(plugin, border, gl, gg, bankNameField, save, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				sPlayer.getMainScreen().closePopup();
				new ReservePopup(plugin, sPlayer);
				break;
			case 2:
				if (bankNameField.getText().isEmpty()) {
					sPlayer.getMainScreen().closePopup();
					new AckPopup(plugin, sPlayer, null, "Please specify new name.", "renamebankgui");
				} else {
					final String input = bankNameField.getText().trim();
					if (!input.matches(plugin.INPUT_REGEX)) {
						new AckPopup(plugin, sPlayer, null, "Invalid characters entered for bank name.", "renamebankgui");
					} else {
						selectedBank.setName(input);
						sPlayer.getMainScreen().closePopup();
						new AckPopup(plugin, sPlayer, null, "Changes Saved.", "renamebankgui");
					}
				}
				break;
		}
	}
}
