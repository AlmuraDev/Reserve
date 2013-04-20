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
import com.almuradev.reserve.gui.checkbox.ConfigMultipleCheckBox;
import com.almuradev.reserve.gui.checkbox.ConfigShareCheckBox;

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

public class ReserveConfigPopup extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final GenericTextField saveTime, intTime, deathTax;
	private final GenericCheckBox deathCheckBox, gainInterest;
	private final Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public ReserveConfigPopup(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(255).setHeight(200);
		border.shiftXPos(0 - (border.getWidth() / 2)).shiftYPos(-130);

		GenericLabel gl = new GenericLabel("Reserve Configuration");
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(0 - (gl.getWidth() / 2)).shiftYPos(-120);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(130).setHeight(1);
		gg.shiftXPos(0 - (gg.getWidth() / 2)).shiftYPos(-105).setMaxWidth(130);

		deathCheckBox = new ConfigMultipleCheckBox(sPlayer, plugin);
		deathCheckBox.setText("Global Player Death Penalty");
		deathCheckBox.setChecked(ReservePlugin.getConfiguration().shouldTaxDeath());
		deathCheckBox.setAnchor(WidgetAnchor.CENTER_CENTER);
		deathCheckBox.setHeight(20).setWidth(19);
		deathCheckBox.shiftXPos(-55).shiftYPos(-92);

		gainInterest = new ConfigShareCheckBox(sPlayer, plugin);
		gainInterest.setText("Global Account Interest");
		gainInterest.setChecked(ReservePlugin.getConfiguration().shouldInterest());
		gainInterest.setAnchor(WidgetAnchor.CENTER_CENTER);
		gainInterest.setHeight(20).setWidth(19);
		gainInterest.shiftXPos(-55).shiftYPos(-70);

		GenericLabel an = new GenericLabel("Interest Calculator Time(ms): ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-115).shiftYPos(4);

		intTime = new GenericTextField();
		intTime.setWidth(60).setHeight(16);
		intTime.setText(Long.toString(ReservePlugin.getConfiguration().getInterestInterval()));
		intTime.setAnchor(WidgetAnchor.CENTER_CENTER);
		intTime.shiftXPos(40).shiftYPos(0);
		intTime.setMaximumCharacters(10);
		intTime.setMaximumLines(1);
		intTime.setTooltip("Value Range: 1200 - 12000 ms");

		GenericLabel ana = new GenericLabel("Save DB Time(ms): ");
		ana.setScale(1.0F);
		ana.setAnchor(WidgetAnchor.CENTER_CENTER);
		ana.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		ana.shiftXPos(-55).shiftYPos(30);

		saveTime = new GenericTextField();
		saveTime.setWidth(60).setHeight(16);
		saveTime.setText(Long.toString(ReservePlugin.getConfiguration().getSaveInterval()));
		saveTime.setAnchor(WidgetAnchor.CENTER_CENTER);
		saveTime.shiftXPos(40).shiftYPos(25);
		saveTime.setMaximumCharacters(10);
		saveTime.setMaximumLines(1);
		saveTime.setTooltip("Value Range: 1200 - 12000 ms");

		GenericLabel anb = new GenericLabel("Death Tax Range: ");
		anb.setScale(1.0F);
		anb.setAnchor(WidgetAnchor.CENTER_CENTER);
		anb.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		anb.shiftXPos(-55).shiftYPos(-20);

		deathTax = new GenericTextField();
		deathTax.setWidth(60).setHeight(16);
		deathTax.setText(ReservePlugin.getConfiguration().getDeathTaxCfg());
		deathTax.setAnchor(WidgetAnchor.CENTER_CENTER);
		deathTax.shiftXPos(40).shiftYPos(-25);
		deathTax.setMaximumCharacters(10);
		deathTax.setMaximumLines(1);

		GenericButton depositButton = new CommandButton(this, 1, "Save");
		GenericButton close = new CommandButton(this, 2, "Close");

		depositButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		depositButton.setHeight(16).setWidth(50).shiftXPos(20).shiftYPos(47);
		close.setHeight(16).setWidth(40).shiftXPos(75).shiftYPos(47);

		attachWidgets(plugin, border, gl, gg, deathCheckBox, gainInterest, saveTime, intTime, deathTax, anb, an, ana, depositButton, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1: //Create				
				sPlayer.getMainScreen().closePopup();
				ReservePlugin.getConfiguration().setDeathTaxCfg(deathTax.getText());
				ReservePlugin.getConfiguration().setShouldTaxDeath(deathCheckBox.isChecked());
				ReservePlugin.getConfiguration().setShouldInterest(gainInterest.isChecked());
				
				if (Long.parseLong(saveTime.getText()) > 1200 && Long.parseLong(saveTime.getText()) < 12000) {
					ReservePlugin.getConfiguration().setSaveInterval(Long.parseLong(saveTime.getText()));
				}
				
				if (Long.parseLong(intTime.getText()) > 1200 && Long.parseLong(intTime.getText()) < 12000) {
					ReservePlugin.getConfiguration().setInterestInterval(Long.parseLong(saveTime.getText()));
				}
				
				new AckPopup(plugin, sPlayer, selectedBank, "Reserve Configuration Saved.", "reserveconfiggui");
				break;
			case 2:
				sPlayer.getMainScreen().closePopup();
				new OptionsPopup(plugin, sPlayer);
				break;
		}
	}

	public void onSelect() {
	}
}
