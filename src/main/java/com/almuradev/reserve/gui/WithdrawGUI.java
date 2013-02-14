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

public class WithdrawGUI extends GenericPopup {
	private final ReservePlugin plugin;
	private final SpoutPlayer sPlayer;
	private final Bank selectedBank;
	private final Account selectedAccount;
	private final ComboBox box;
	private final GenericLabel at, att;
	private final GenericTextField depositAmountField;
	private static NumberFormat numForm;
	private static Locale caLoc = new Locale("en", "US");
	Color bottom = new Color(1.0F, 1.0F, 1.0F, 0.50F);

	public WithdrawGUI(ReservePlugin plugin, SpoutPlayer sPlayer, Bank bank, Account account) {
		this.plugin = plugin;
		this.sPlayer = sPlayer;
		this.selectedBank = bank;
		this.selectedAccount = account;

		GenericTexture border = new GenericTexture("http://www.almuramc.com/images/playerplus.png");
		border.setAnchor(WidgetAnchor.CENTER_CENTER);
		border.setPriority(RenderPriority.High);
		border.setWidth(255).setHeight(150);
		border.shiftXPos(0-(border.getWidth()/2)).shiftYPos(-80);

		GenericLabel gl = new GenericLabel();
		gl.setScale(1.4F).setText(selectedBank.getName());		
		gl.setAnchor(WidgetAnchor.CENTER_CENTER);
		gl.setHeight(15).setWidth(GenericLabel.getStringWidth(gl.getText(), gl.getScale()));
		gl.shiftXPos((GenericLabel.getStringWidth(gl.getText(), gl.getScale()) / 2) * -1).shiftYPos(-70);

		GenericGradient gg = new GenericGradient();
		gg.setBottomColor(bottom).setTopColor(bottom);
		gg.setAnchor(WidgetAnchor.CENTER_CENTER);
		gg.setWidth(200).setHeight(1);
		gg.shiftXPos(0-(gg.getWidth()/2)).shiftYPos(-55);
		

		GenericLabel cl = new GenericLabel("Select Account: ");		
		cl.setAnchor(WidgetAnchor.CENTER_CENTER);
		cl.setHeight(15).setWidth(GenericLabel.getStringWidth(cl.getText()));
		cl.shiftXPos(-115).shiftYPos(-42);
		
		GenericGradient gm = new GenericGradient();
		gm.setBottomColor(bottom).setTopColor(bottom);
		gm.setAnchor(WidgetAnchor.CENTER_CENTER);
		gm.setWidth(200).setHeight(1);
		gm.shiftXPos(0-(gm.getWidth()/2)).shiftYPos(-25);

		box = new AccountWithdrawCombo(this);
		box.setText("Account");
		box.setAnchor(WidgetAnchor.CENTER_CENTER);
		box.setWidth(GenericLabel.getStringWidth("12345678901234567890123459"));
		box.setHeight(18);
		box.shiftXPos(-35).shiftYPos(-47);
		box.setAuto(true);
		box.setPriority(RenderPriority.Low);
			

		at = new GenericLabel("Account Balance: ");
		at.setScale(1.0F).setVisible(false);
		at.setAnchor(WidgetAnchor.CENTER_CENTER);
		at.setHeight(15).setWidth(GenericLabel.getStringWidth(at.getText()));
		at.shiftXPos(-95).shiftYPos(-10);
		
		att = new GenericLabel();
		att.setText(ChatColor.GREEN + "0.00").setVisible(false);		
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
		
		GenericLabel an = new GenericLabel("Withdraw Amount: ");
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

		GenericButton depositButton = new CommandButton(this, 1, "Withdraw");
		GenericButton close = new CommandButton(this, 2, "Close");
		GenericButton test = new CommandButton(this, 3, "Test");

		depositButton.setAnchor(WidgetAnchor.CENTER_CENTER);
		close.setAnchor(WidgetAnchor.CENTER_CENTER);
		test.setAnchor(WidgetAnchor.CENTER_CENTER);

		depositButton.setHeight(16).setWidth(50).shiftXPos(20).shiftYPos(50);
		close.setHeight(16).setWidth(40).shiftXPos(75).shiftYPos(50);
		test.setHeight(16).setWidth(40).shiftXPos(-20).shiftYPos(50);

		populateList();
		
		attachWidgets(plugin, border, gl, gg, ag, ab, at, att, gm, box, cl, depositAmountField, an, depositButton, close, test);

		sPlayer.getMainScreen().closePopup();
		sPlayer.getMainScreen().attachPopupScreen(this);		
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable () {
			@Override
			public void run() {				
				setDirty(true);
			}			 
		} ,5L);
		                                                                                  
	}

	public void onClickCommand(int commandGoal) {
		switch (commandGoal) {
		case 1: // Ok
			if (box.getSelectedItem() == null) {
				new AckGUI(plugin, sPlayer, selectedBank, "Please specify account.", "depositgui");
			} else {

				Account myAccount = selectedBank.getAccount(box.getSelectedItem(), sPlayer.getName());
				double withdraw = 0;
				try {
					withdraw = Math.abs(Double.parseDouble(depositAmountField.getText()));										
				} catch (Exception e) {
					//do nothing
				}
				if (withdraw == 0) {
					sPlayer.getMainScreen().closePopup();
					new AckGUI(plugin, sPlayer, selectedBank, "Deposit amount has to be more than zero.", "withdrawgui");
				} else {				
					if (myAccount.getBalance()<withdraw) {
						new AckGUI(plugin, sPlayer, selectedBank, "Withdraw amount cannot be greater than current balance.", "withdrawgui");
					} else {
						myAccount.add(0-withdraw);
						VaultUtil.add(sPlayer.getName(), withdraw);
						sPlayer.getMainScreen().closePopup();				
						new AckGUI(plugin, sPlayer, selectedBank, "Funds Withdrawn Successfully.", "withdrawgui");
					}
				}
			}
			break;
		case 2: // Close
			sPlayer.getMainScreen().closePopup();
			new BankMainGUI(plugin, sPlayer, selectedBank);
			break;
			
		case 3:
			//box.setText(box.getSelectedItem());
			box.setDirty(true);
		}
	}

	private void populateList() {	
		List<String> items = new ArrayList<String>();
		List<Account> accountNames = selectedBank.getAccountsFor(sPlayer.getName());
		int selectionID = 0;		
		for (Account account: accountNames) {
			items.add(account.getName());			
		}
	
		if (items != null) {	
			Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
			box.setItems(items);			
		}
		
		if (items != null && selectedAccount != null) {
			for (int i=0; i<items.size(); i++) {
				if (items.get(i).equalsIgnoreCase(selectedAccount.getName())) {
					selectionID = i;
					break;
				}
			}	
		}
		if (selectedAccount	!= null) {
			box.setSelection(selectionID);			
			box.setText(null);
		}		
	}
	
	void onSelect(int i, String text) {
		if (box.getSelectedItem() != null) {
			numForm = NumberFormat.getCurrencyInstance(caLoc);
			Account myAccount = selectedBank.getAccount(box.getSelectedItem(), sPlayer.getName());
			att.setText(ChatColor.GREEN + numForm.format(myAccount.getBalance()));
			att.setVisible(true);
			at.setVisible(true);
			box.setText(null);
		}
	}
}
