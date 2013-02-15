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
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.ChatColor;

public class DeleteBankGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final GenericLabel an;
	private ListWidget list;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public DeleteBankGUI(ReservePlugin plugin, SpoutPlayer sPlayer) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(255).setHeight(150);
		border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-80);

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText("Reserve");
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.shiftXPos(-100).shiftYPos(-55).setMaxWidth(200);
		gg.setWidth(200).setHeight(1);

		GenericLabel windowLabel = new GenericLabel("- Remove Bank -");
		windowLabel.setScale(1.0F);
		windowLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		windowLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(windowLabel.getText()));
		windowLabel.shiftXPos(((GenericLabel.getStringWidth(windowLabel.getText()) / 2) * -1)).shiftYPos(-52);

		GenericLabel cl = new GenericLabel("Bank: ");
		cl.setScale(1.0F);
		cl.setAnchor(WidgetAnchor.CENTER_CENTER);
		cl.setHeight(15).setWidth(GenericLabel.getStringWidth(cl.getText()));
		cl.shiftXPos(-110).shiftYPos(-32);

		list = new BankListApplet();
		list.setAnchor(WidgetAnchor.CENTER_CENTER);
		list.shiftXPos(-80).shiftYPos(-35);
		list.setWidth(155).setHeight(60);
		list.setPriority(RenderPriority.Lowest);

		an = new GenericLabel();
		numForm = NumberFormat.getCurrencyInstance(caLoc);
		an.setText("Bank Balance: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-110).shiftYPos(30);

		GenericButton depositButton = new CommandButton(this, 1, "Remove Bank");
		GenericButton close = new CommandButton(this, 2, "Close");

		depositButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		depositButton.setHeight(16).setWidth(80).shiftXPos(-10).shiftYPos(47);
		close.setHeight(16).setWidth(40).shiftXPos(75).shiftYPos(47);

		attachWidgets(plugin, border, gl, gg, list, cl, windowLabel, an, depositButton, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1: // Ok
				if (list.getSelectedItem() == null) {
					new AckGUI(plugin, sPlayer, null, "Please Select Bank.", "deletebankgui");
				} else {
					String[] split = list.getSelectedItem().getTitle().split("\\/");
					ReservePlugin.getReserve().remove(split[0].trim(), ChatColor.stripColor(split[1].trim()));
					new AckGUI(plugin, sPlayer, null, "Bank Removed.", "deletebankgui");
				}
				break;
			case 2: // Close
				sPlayer.getMainScreen().closePopup();
				new ReserveMainGUI(plugin, sPlayer);
				break;
		}
	}
}
