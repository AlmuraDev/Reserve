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
package com.almuradev.reserve.gui.button;

import com.almuradev.reserve.gui.popup.AccountTypesPopup;
import com.almuradev.reserve.gui.popup.AckPopup;
import com.almuradev.reserve.gui.popup.BankConfigPopup;
import com.almuradev.reserve.gui.popup.BankPopup;
import com.almuradev.reserve.gui.popup.BankStatusPopup;
import com.almuradev.reserve.gui.popup.ConfigureNPCPopup;
import com.almuradev.reserve.gui.popup.CreateAccountPopup;
import com.almuradev.reserve.gui.popup.CreateBankPopup;
import com.almuradev.reserve.gui.popup.DeleteAccountPopup;
import com.almuradev.reserve.gui.popup.DeleteBankPopup;
import com.almuradev.reserve.gui.popup.DepositPopup;
import com.almuradev.reserve.gui.popup.OptionsPopup;
import com.almuradev.reserve.gui.popup.RenameAccountPopup;
import com.almuradev.reserve.gui.popup.RenameBankPopup;
import com.almuradev.reserve.gui.popup.ReserveConfigPopup;
import com.almuradev.reserve.gui.popup.ReservePopup;
import com.almuradev.reserve.gui.popup.WithdrawPopup;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

public class CommandButton extends GenericButton {
	private Object gui;
	private int commandGoal;

	public CommandButton(Object gui, int commandGoal, String text) {
		super(text);
		this.gui = gui;
		this.commandGoal = commandGoal;
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (gui instanceof BankPopup) {
			((BankPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof ReservePopup) {
			((ReservePopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof CreateAccountPopup) {
			((CreateAccountPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof AckPopup) {
			((AckPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof DepositPopup) {
			((DepositPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof WithdrawPopup) {
			((WithdrawPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof ReserveConfigPopup) {
			((ReserveConfigPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof BankConfigPopup) {
			((BankConfigPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof OptionsPopup) {
			((OptionsPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof DeleteAccountPopup) {
			((DeleteAccountPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof CreateBankPopup) {
			((CreateBankPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof DeleteBankPopup) {
			((DeleteBankPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof BankStatusPopup) {
			((BankStatusPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof AccountTypesPopup) {
			((AccountTypesPopup) gui).onClickCommand(commandGoal);
		}

		if (gui instanceof RenameBankPopup) {
			((RenameBankPopup) gui).onClickCommand(commandGoal);
		}
		
		if (gui instanceof RenameAccountPopup) {
			((RenameAccountPopup) gui).onClickCommand(commandGoal);
		}
		
		if (gui instanceof ConfigureNPCPopup) {
			((ConfigureNPCPopup) gui).onClickCommand(commandGoal);
		}
	}
}