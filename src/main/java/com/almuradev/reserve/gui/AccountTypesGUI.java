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

import java.awt.Checkbox;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.econ.type.AccountType;

import org.bukkit.ChatColor;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ComboBox;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericCheckBox;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.ListWidget;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Screen;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class AccountTypesGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final GenericCheckBox intsetting;
	private final GenericTexture myImage;
	private final ComboBox box;
	private final GenericTextField accountTypeName, intCycleField, imageField;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);	

	public AccountTypesGUI(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;		

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(250).setHeight(250);
		border.shiftXPos(0-(border.getWidth()/2)).shiftYPos(-120);

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.2F).setText(selectedBank.getName() + " - AccountTypes");		
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-110);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);		
		gg.setWidth(200).setHeight(1);
		gg.shiftXPos(0-(gg.getWidth()/2)).shiftYPos(-95);

		GenericGradient gb = new GenericGradient();
		gb.setBottomColor(bottom).setTopColor(bottom);
		gb.setAnchor(WidgetAnchor.CENTER_CENTER);		
		gb.setWidth(200).setHeight(1);
		gb.shiftXPos(0-(gb.getWidth()/2)).shiftYPos(-65);

		box = new AccountTypesEditCombo(this);
		box.setText("Accounts");
		box.setAnchor(WidgetAnchor.CENTER_CENTER);
		box.setWidth(GenericLabel.getStringWidth("12345678901234567890123459"));
		box.setHeight(18);
		box.shiftXPos(0-(box.getWidth()/2)).shiftYPos(-90);
		box.setAuto(true);
		box.setPriority(RenderPriority.Low);
		populateList();		
		box.setSelection(0);
		AccountType selectedAccountType = selectedBank.getType(box.getSelectedItem());
		
		GenericLabel nameLabel = new GenericLabel();
		nameLabel.setScale(1.0F).setText("Type Name:");		
		nameLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		nameLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		nameLabel.shiftXPos(-100).shiftYPos(-50);
		
		accountTypeName = new GenericTextField();
		accountTypeName.setText(selectedBank.getType(box.getSelectedItem()).getName());
		accountTypeName.setWidth(110).setHeight(16);
		accountTypeName.setAnchor(WidgetAnchor.CENTER_CENTER);
		accountTypeName.shiftXPos(-39).shiftYPos(-53);
		accountTypeName.setMaximumCharacters(30);
		accountTypeName.setTooltip("Account Name - no more than 30 characters.");		
		accountTypeName.setMaximumLines(1);
		
		GenericLabel intNameLabel = new GenericLabel();
		intNameLabel.setScale(1.0F).setText("Interest per Cycle:");		
		intNameLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		intNameLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		intNameLabel.shiftXPos(-100).shiftYPos(-27);
		
		intCycleField = new GenericTextField();
		intCycleField.setText(Double.toString(selectedBank.getType(box.getSelectedItem()).getInterestRate()));
		intCycleField.setWidth(40).setHeight(16);
		intCycleField.setAnchor(WidgetAnchor.CENTER_CENTER);
		intCycleField.shiftXPos(0).shiftYPos(-30);
		intCycleField.setMaximumCharacters(6);
		intCycleField.setTooltip("Specified Interest Rate for this account type.");
		intCycleField.setMaximumLines(1);
		
		GenericLabel imageNameLabel = new GenericLabel();
		imageNameLabel.setScale(1.0F).setText("Image:");		
		imageNameLabel.setAnchor(WidgetAnchor.CENTER_CENTER);
		imageNameLabel.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		imageNameLabel.shiftXPos(-100).shiftYPos(-7);
		
		imageField = new GenericTextField();
		imageField.setText(selectedBank.getType(box.getSelectedItem()).getImagePath());		
		imageField.setWidth(170).setHeight(48);
		imageField.setAnchor(WidgetAnchor.CENTER_CENTER);
		imageField.shiftXPos(-60).shiftYPos(-10);
		imageField.setMaximumCharacters(100);
		imageField.setTooltip("Type the images HTTP link in this field.");
		imageField.setMaximumLines(4);
		
		intsetting = new GenericCheckBox();
		intsetting.setText("Interest Enabled");
		intsetting.setAnchor(WidgetAnchor.CENTER_CENTER);
		intsetting.setChecked(selectedBank.getType(box.getSelectedItem()).receivesInterest());		
		intsetting.setWidth(40).setHeight(16);
		intsetting.shiftXPos(-110).shiftYPos(85);		
		
		if (selectedAccountType.getImagePath().isEmpty()) {
			myImage = new GenericTexture("http://www.almuramc.com/images/almuralogo.png");
		} else {
			myImage = new GenericTexture(selectedBank.getType(box.getSelectedItem()).getImagePath());
		}
		
		myImage.setAnchor(WidgetAnchor.CENTER_CENTER);
		myImage.setPriority(RenderPriority.Low);
		myImage.setWidth(20).setHeight(20);
		myImage.shiftXPos(-90).shiftYPos(10);			
		
		GenericButton save = new CommandButton(this, 1, "Save");
		GenericButton testImage = new CommandButton(this, 2, "Test Image");
		GenericButton close = new CommandButton(this, 6, "Close");
		
		save.setAnchor(WidgetAnchor.CENTER_CENTER);
		testImage.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		save.setHeight(16).setWidth(40).shiftXPos(20).shiftYPos(95);
		testImage.setHeight(16).setWidth(70).shiftXPos(40).shiftYPos(42);
		close.setHeight(16).setWidth(40).shiftXPos(70).shiftYPos(95);

		attachWidgets(plugin, border, gl, gg, box, nameLabel, accountTypeName, intNameLabel, intCycleField, imageNameLabel, imageField, intsetting, gb, myImage, save, testImage, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
			case 1:
				AccountType selectedAccountType = selectedBank.getType(box.getSelectedItem());
				if (selectedAccountType != null) {
					selectedAccountType.setName(accountTypeName.getText().trim());
					selectedAccountType.setInterestRate(Double.parseDouble((intCycleField.getText().trim())));
					selectedAccountType.setImagePath(imageField.getText().trim());
					sPlayer.getMainScreen().closePopup();
					new AckGUI(plugin, sPlayer, selectedBank, "Changes Saved.", "accounttypesgui");
				}
				break;
			case 2:
				if (!imageField.getText().isEmpty()) {
					myImage.setUrl(imageField.getText().trim());
					setDirty(true);
				}
				
				break;
			case 3:				
				
				break;
			case 4:
				
				break;
			case 5:
				
				break;
			case 6:
				sPlayer.getMainScreen().closePopup();
				new BankStatusGUI(plugin, sPlayer, selectedBank);
				break;
		}
	}
		
	private void populateList() {
		List<String> items = new ArrayList<String>();
		List<AccountType> accountTypes = selectedBank.retrieveTypes();
		
		for (AccountType allAccountTypes: accountTypes) {
			items.add(allAccountTypes.getName());			
		}
	
		if (items != null) {	
			Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
			box.setItems(items);			
		}
				
				
	}
	
	void onSelect(int i, String text) {
				
	}
}
