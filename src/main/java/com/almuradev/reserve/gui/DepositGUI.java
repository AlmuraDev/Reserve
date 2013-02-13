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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.almuradev.reserve.ReservePlugin;
import com.almuradev.reserve.econ.Account;
import com.almuradev.reserve.econ.Bank;
import com.almuradev.reserve.econ.VaultUtil;

import org.bukkit.ChatColor;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.ComboBox;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTextField;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public class DepositGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final GenericTextField depositAmountField;
	private final GenericLabel att, at;
	private final ComboBox box;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public DepositGUI(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(255).setHeight(150);
		border.shiftXPos(0-(border.getWidth()/2)).shiftYPos(-80);

		GenericLabel gl = new GenericLabel();
		gl.setText(selectedBank.getName());
		gl.setScale(1.2F);
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText()));
		gl.shiftXPos(((GenericLabel.getStringWidth(gl.getText()) / 2) * -1) - 4).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(200).setHeight(1);
		gg.shiftXPos(0-(gg.getWidth()/2)).shiftYPos(-55);
		

		GenericLabel cl = new GenericLabel("Select Account: ");
		cl.setScale(1.0F);
		cl.setAnchor(WidgetAnchor.CENTER_CENTER);
		cl.setHeight(15).setWidth(GenericLabel.getStringWidth(cl.getText()));
		cl.shiftXPos(-115).shiftYPos(-42);
		
		GenericGradient gm = new GenericGradient();
		gm.setBottomColor(bottom).setTopColor(bottom);
		gm.setAnchor(WidgetAnchor.CENTER_CENTER);
		gm.setWidth(200).setHeight(1);
		gm.shiftXPos(0-(gm.getWidth()/2)).shiftYPos(-25);

		box = new AccountDepositCombo(this);
		box.setText("Accounts");
		box.setAnchor(WidgetAnchor.CENTER_CENTER);
		box.setWidth(GenericLabel.getStringWidth("12345678901234567890123459"));
		box.setHeight(18);
		box.shiftXPos(-35).shiftYPos(-47);
		box.setAuto(true);
		box.setPriority(RenderPriority.Low);
		populateList();

		at = new GenericLabel("Account Balance: ");
		at.setScale(1.0F).setVisible(false);
		at.setAnchor(WidgetAnchor.CENTER_CENTER);
		at.setHeight(15).setWidth(GenericLabel.getStringWidth(at.getText()));
		at.shiftXPos(-95).shiftYPos(-10);
		
		att = new GenericLabel();
		att.setText(ChatColor.GREEN + "0.00").setVisible(false);
		att.setScale(1.0F);
		att.setAnchor(WidgetAnchor.CENTER_CENTER);
		att.setHeight(15).setWidth(GenericLabel.getStringWidth(att.getText()));
		att.shiftXPos(0).shiftYPos(-10);
		
		GenericLabel ag = new GenericLabel("Carrying Balance: ");
		ag.setScale(1.0F);
		ag.setAnchor(WidgetAnchor.CENTER_CENTER);
		ag.setHeight(15).setWidth(GenericLabel.getStringWidth(ag.getText()));
		ag.shiftXPos(-95).shiftYPos(10);
		
		GenericLabel ab = new GenericLabel();
		numForm = NumberFormat.getCurrencyInstance(caLoc);
		ab.setText(ChatColor.YELLOW + numForm.format(VaultUtil.getBalance(sPlayer.getName())));
		ab.setScale(1.0F);
		ab.setAnchor(WidgetAnchor.CENTER_CENTER);
		ab.setHeight(15).setWidth(GenericLabel.getStringWidth(ab.getText()));
		ab.shiftXPos(0).shiftYPos(10);
		
		GenericLabel an = new GenericLabel("Deposit Amount: ");
		an.setScale(1.0F);
		an.setAnchor(WidgetAnchor.CENTER_CENTER);
		an.setHeight(15).setWidth(GenericLabel.getStringWidth(an.getText()));
		an.shiftXPos(-95).shiftYPos(31);

		depositAmountField = new GenericTextField();
		depositAmountField.setWidth(110).setHeight(16);
		depositAmountField.setAnchor(WidgetAnchor.CENTER_CENTER);
		depositAmountField.shiftXPos(-10).shiftYPos(27);
		depositAmountField.setText("0.00");
		depositAmountField.setMaximumCharacters(15);
		depositAmountField.setMaximumLines(1);

		GenericButton depositButton = new CommandButton(this, 1, "Deposit");
		GenericButton close = new CommandButton(this, 2, "Close");

		depositButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);

		depositButton.setHeight(16).setWidth(50).shiftXPos(20).shiftYPos(50);
		close.setHeight(16).setWidth(40).shiftXPos(75).shiftYPos(50);

		attachWidgets(plugin, border, gl, gg, ag, ab, at, att, gm, box, cl, depositAmountField, an, depositButton, close);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
		case 1: // Ok
			if (box.getSelectedItem() == null) {
				new AckGUI(plugin, sPlayer, selectedBank, "Please specify account.", "depositgui");
			} else {

				Account myAccount = ReservePlugin.getReserve().getAccountFromNameIn(selectedBank, box.getSelectedItem(), sPlayer.getName());
				double deposit = 0;
				try {
					deposit = Math.abs(Double.parseDouble(depositAmountField.getText()));												
				} catch (Exception e) {
					//do nothing
				}
				if (deposit == 0) {
					sPlayer.getMainScreen().closePopup();					
					new AckGUI(plugin, sPlayer, selectedBank, "Deposit amount has to be more than zero.", "depositgui");
				} else {
				// Remove from Users Economy
					if (VaultUtil.getBalance(sPlayer.getName()) < deposit) {
						sPlayer.getMainScreen().closePopup();					
						new AckGUI(plugin, sPlayer, selectedBank, "Insuffient funds available for deposit.", "depositgui");
					} else {
						myAccount.add(deposit);
						VaultUtil.add(sPlayer.getName(), 0-deposit);
						sPlayer.getMainScreen().closePopup();
						new AckGUI(plugin, sPlayer, selectedBank, "Funds Deposited Successfully", "depositgui");
					}
				}
			}
				break;
			case 2: // Close
				sPlayer.getMainScreen().closePopup();
				new BankMainGUI(plugin, sPlayer, selectedBank);
				break;
		}
	}

	public void onSelect(int i, String text) {
		if (box.getSelectedItem() != null) {
			numForm = NumberFormat.getCurrencyInstance(caLoc);
			Account myAccount = selectedBank.getAccount(box.getSelectedItem(), sPlayer.getName());
			att.setText(ChatColor.GREEN + numForm.format(myAccount.getBalance()));
			att.setVisible(true);
			at.setVisible(true);
		}
	}

	private void populateList() {
		List<String> items = new ArrayList<String>();
		List<Account> accountNames = ReservePlugin.getReserve().getAccountsInBankFor(sPlayer.getName(), selectedBank);
		for (Account account : accountNames) {
			items.add(account.getName());
		}
		if (items != null) {	
			Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
			box.setItems(items);
			box.setDirty(true);
		}
	}
}
